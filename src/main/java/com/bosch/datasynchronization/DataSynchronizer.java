package com.bosch.datasynchronization;

import com.bosch.datasynchronization.client.FruitShopRestClient;
import com.bosch.datasynchronization.model.*;
import com.bosch.datasynchronization.repository.EnrichedProductRepository;
import com.bosch.datasynchronization.repository.ParentChildRelationsRepository;
import com.bosch.datasynchronization.repository.SynchronizationRunsRepository;
import org.bson.types.Binary;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataSynchronizer {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DataSynchronizer.class);
    @Autowired
    private FruitShopRestClient _fruitShopRestClient;
    @Autowired
    private ParentChildRelationsRepository _parentChildRelationShipRepository;
    @Autowired
    private EnrichedProductRepository _enrichedProductRepository;
    @Autowired
    private SynchronizationRunsRepository _synchronizationRunsRepository;

    public void synchronizeData() {
        SynchronizationRun lastRun = _synchronizationRunsRepository.findTopByOrderByIdDesc();
        Date lastModified = lastRun == null ? null : lastRun.getLastRun();

        List<EnrichedProduct> enrichedProducts = _enrichedProductRepository.findAll();
        Map<Integer, EnrichedProduct> productIdToEnrichedProduct = enrichedProducts.stream().collect(Collectors.toMap(EnrichedProduct::getProductId, Function.identity()));

        Map<Integer, Product> productIdToProduct = _fruitShopRestClient.getAllProducts(lastRun == null ? null : lastModified).stream().collect(Collectors.toMap(product -> Integer.parseInt(product.getId()), Function.identity()));
        Map<Integer, Vendor> vendorIdToVendor = _fruitShopRestClient.getAllVendors().stream().collect(Collectors.toMap(vendor -> Integer.parseInt(vendor.getId()), Function.identity()));

        Set<Integer> vendorIds = vendorIdToVendor.keySet();
        HashMap<Integer, Set<Integer>> productIdToVendorIds = new HashMap<>();
        HashMap<Integer, Set<Product>> vendorIdToProducts = new HashMap<>();
        for (int vendorId : vendorIds) {
            List<Product> productsByVendor = _fruitShopRestClient.getProductsByVendor(vendorId);
            Set<Product> productsOfferedByVendor = new HashSet<>(productsByVendor);
            vendorIdToProducts.put(vendorId, productsOfferedByVendor);
            productsOfferedByVendor.forEach(product -> productIdToVendorIds.computeIfAbsent(Integer.parseInt(product.getId()), _ -> new HashSet<>(vendorId)).add(vendorId));
        }

        List<Vendor> changedVendors = getChangedVendors(vendorIdToVendor, enrichedProducts);
        Set<Integer> productIdsWhichWereModifiedSinceTheLastRun = productIdToProduct.values().stream().map(Product::getId).map(Integer::parseInt).collect(Collectors.toSet());
        List<EnrichedProduct> updatedVendorsEnrichedProducts = new ArrayList<>();
        for (Vendor changedVendor : changedVendors) {
            Set<Product> products = vendorIdToProducts.get(Integer.parseInt(changedVendor.getId()));
            if (products == null) {
                continue;
            }

            updatedVendorsEnrichedProducts.addAll(products.stream().filter(product ->
                    !productIdsWhichWereModifiedSinceTheLastRun.contains(Integer.parseInt(product.getId()))
            ).map(product -> {
                EnrichedProduct enrichedProduct = productIdToEnrichedProduct.get(Integer.parseInt(product.getId()));
                if (enrichedProduct == null) {
                    return null;
                }
                enrichedProduct.setVendors(productIdToVendorIds.get(enrichedProduct.getProductId()).stream().map(vendorIdToVendor::get).toList());

                return enrichedProduct;
            }).filter(Objects::nonNull).toList());
        }
        List<ParentChildRelation> parentChildRelationShips = _parentChildRelationShipRepository.findAll();
        Map<Integer, ParentChildRelation> productIdToParent = parentChildRelationShips.stream().collect(Collectors.toMap(ParentChildRelation::getProductId, Function.identity()));

        List<EnrichedProduct> updatedAndNewEnrichedProducts = productIdToProduct.values().stream().map(product -> {
            try {
                int productId = Integer.parseInt(product.getId());
                ParentChildRelation parentChildRelation = productIdToParent.get(productId);
                byte[] image = _fruitShopRestClient.getImageForProduct(productId);
                ProductDetail productDetails = _fruitShopRestClient.getProductDetails(productId);

                Set<Integer> vendorIdSet = productIdToVendorIds.get(productId);
                List<Integer> vendorIdsForProduct = vendorIdSet == null ? List.of() : vendorIdSet.stream().sorted().toList();

                EnrichedProduct existingEnrichedProduct = productIdToEnrichedProduct.get(productId);
                if (existingEnrichedProduct == null) {
                    try {
                        EnrichedProduct enrichedProduct = new EnrichedProduct();
                        enrichedProduct.setProductId(productId);
                        enrichedProduct.setName(product.getName());
                        enrichedProduct.setVendors(vendorIdsForProduct.stream().map(vendorIdToVendor::get).toList());
                        enrichedProduct.setParentId(parentChildRelation == null ? null : parentChildRelation.getParentId());
                        enrichedProduct.setImage(new Binary(image));
                        enrichedProduct.setPrice(Double.parseDouble(productDetails.getPrice()));

                        return enrichedProduct;
                    } catch (Exception e) {
                        log.error("Error while creating enriched product for productId: {}. Maybe the parsing did not work as expected?", product.getId(), e);

                        return null;
                    }
                }

                try {
                    EnrichedProduct enrichedProduct = new EnrichedProduct();
                    enrichedProduct.setId(existingEnrichedProduct.getId());
                    enrichedProduct.setProductId(productId);
                    enrichedProduct.setName(product.getName());
                    enrichedProduct.setImage(new Binary(image));
                    enrichedProduct.setVendors(vendorIdsForProduct.stream().map(vendorIdToVendor::get).toList());
                    enrichedProduct.setParentId(parentChildRelation == null ? null : parentChildRelation.getParentId());
                    enrichedProduct.setPrice(Double.parseDouble(productDetails.getPrice()));

                    return enrichedProduct;

                } catch (Exception e) {
                    log.error("Error while creating enriched product for productId: {}. Maybe the parsing did not work as expected?", product.getId(), e);

                    return null;
                }
            } catch (Exception e) {
                log.error("Error while processing product with id: {}", product.getId(), e);
                return null;
            }

        }).filter(Objects::nonNull).collect(Collectors.toList());

        updatedAndNewEnrichedProducts.addAll(updatedVendorsEnrichedProducts);

        _enrichedProductRepository.saveAll(updatedAndNewEnrichedProducts);
        _synchronizationRunsRepository.save(new SynchronizationRun());
    }

    private List<Vendor> getChangedVendors(Map<Integer, Vendor> vendorIdToVendor, List<EnrichedProduct> enrichedProducts) {
        ArrayList<Vendor> changedVendors = new ArrayList<>();
        Collection<Vendor> vendors = vendorIdToVendor.values();

        for (Vendor vendor : vendors) {
            Optional<EnrichedProduct> enrichedProductWithVendor = enrichedProducts.stream().filter(enrichedProduct -> enrichedProduct.getVendors().stream().map(Vendor::getId).toList().contains(vendor.getId())).findFirst();
            if (enrichedProductWithVendor.isPresent()) {
                List<Vendor> vendorsForProduct = enrichedProductWithVendor.get().getVendors();
                Vendor foundVendor = vendorsForProduct.stream().filter(item -> Objects.equals(item.getId(), vendor.getId())).toList().get(0);
                if (!Objects.equals(foundVendor.getName(), vendor.getName()) || !Objects.equals(foundVendor.getSelfLink(), vendor.getSelfLink())) {
                    changedVendors.add(vendor);
                }
            }
        }

        return changedVendors;
    }
}
