package com.bosch.datasynchronization.client;

import com.alibaba.fastjson.JSON;
import com.bosch.datasynchronization.model.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class FruitShopRestClient {

    @Value("${fruitshop.api.base-url}")
    private String baseUrl;

    @Autowired
    private final HttpClient _httpClient;
    private static final Logger _log = Logger.getLogger(FruitShopRestClient.class.getName());

    @Autowired
    public FruitShopRestClient(HttpClient httpClient) {
        this._httpClient = httpClient;
    }

    public List<Product> getAllProducts(Date lastRunTimestamp) {
        List<Product> products;
        try {
            products = fetchProducts(lastRunTimestamp);
        } catch (IOException | InterruptedException e) {
            _log.log(Level.SEVERE, "Exception occurred while fetching products", e);
            return List.of();
        }

        return products;
    }

    public List<Product> getProductsByVendor(int i) {
        List<Product> products;
        try {
            products = fetchProductsForVendor(i);
        } catch (IOException | InterruptedException e) {
            _log.log(Level.SEVERE, "Exception occured while fetching products for a vendor!", e);
            throw new RuntimeException(e);
        }
        return products;
    }

    public List<Vendor> getAllVendors() {
        List<Vendor> vendors;
        try {
            vendors = fetchVendors();
        } catch (IOException | InterruptedException e) {
            _log.log(Level.SEVERE, "Exception occured while fetching vendors!", e);
            return List.of();
        }

        return vendors;
    }

    private List<Product> fetchProducts(Date lastRunTimestamp) throws IOException, InterruptedException {

        String url = baseUrl + "/shop/v2/products?sort=id&order=asc";
        if (lastRunTimestamp != null) {
            Instant instant = lastRunTimestamp.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            String formattedZonedDatetime = formatter.format(zonedDateTime);
            url += "&modified_since=" + formattedZonedDatetime;
        }

        return getPaginatedData(url, (responseBody) -> JSON.parseObject(responseBody, GetProductsResponse.class));
    }

    private List<Vendor> fetchVendors() throws IOException, InterruptedException {
        String url = baseUrl + "/shop/v2/vendors?sort=id&order=asc";

        return getPaginatedData(url, (responseBody) -> JSON.parseObject(responseBody, GetAllVendorsResponse.class));
    }

    private List<Product> fetchProductsForVendor(int id) throws IOException, InterruptedException {
        String url = baseUrl + "/shop/v2/vendors/" + id + "/products?sort=id&order=asc";

        return getPaginatedData(url, (responseBody) -> JSON.parseObject(responseBody, GetProductsResponse.class));
    }

    private <R extends FruitShopResponse<E>, E> List<E> getPaginatedData(@NotNull String url, @NotNull Function<String, R> parseFunction) throws IOException, InterruptedException {
        ArrayList<E> data = new ArrayList<>();
        while (url != null) {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response;
            try {
                response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException exception) {
                _log.log(Level.SEVERE, "HTTP request failed");
                throw exception;
            }
            if (response.statusCode() != 200) {
                _log.log(Level.SEVERE, "Failed to fetch products: {0}", response.statusCode());
                break;
            }

            R parsedResponse;
            try {
                parsedResponse = parseFunction.apply(response.body());
            } catch (Exception exception) {
                _log.log(Level.SEVERE, "Failed to parse the response body");
                throw exception;
            }

            if (parsedResponse == null || parsedResponse.getMeta() == null) {
                _log.log(Level.SEVERE, "Invalid response structure: {0}", response.body());
                throw new JsonMappingException(null, "The response should have a meta field and be nonnull!. Response body: " + response.body());
            }

            data.addAll(parsedResponse.getData());
            url = parsedResponse.getMeta().getNext_link() != null ? baseUrl + parsedResponse.getMeta().getNext_link() : null;
        }

        return data;
    }

    public ProductDetail getProductDetails(int productId) {
        String url = baseUrl + "/shop/v2/products/" + productId;

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return JSON.parseObject(response.body(), ProductDetail.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getImageForProduct(int productId) {
        String url = baseUrl + "/shop/v2/products/" + productId + "/image";
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .GET()
                    .header("Content-Type", "application/json")
                    .header("accept", "image/*")
                    .build();

            HttpResponse<byte[]> response = _httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            boolean isValidImage = isValidImage(response.body());
            if (!isValidImage) {
                throw new IllegalStateException("The response could not be converted into an image");
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidImage(byte[] imageBytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(byteArrayInputStream);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }
}
