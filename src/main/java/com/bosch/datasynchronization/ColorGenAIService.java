package com.bosch.datasynchronization;

import com.bosch.datasynchronization.client.OpenAIRestClient;
import com.bosch.datasynchronization.model.EnrichedProduct;
import com.bosch.datasynchronization.model.OpenAIApiRequest;
import com.bosch.datasynchronization.repository.EnrichedProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ColorGenAIService {

    static final String SYSTEM = "system";
    static final String GPT_3_5_TURBO = "gpt-3.5-turbo-0125";

    @Autowired
    private OpenAIRestClient _openAIRestClient;

    @Autowired
    private EnrichedProductRepository _enrichedProductRepository;

    static private final String endpointUrl = "https://api-inference.huggingface.co/models/gpt2";

    public Map<Integer, String> getColorForProducts() {

        List<EnrichedProduct> allEnrichedProducts = _enrichedProductRepository.findAll();

        String prompt = "{\"inputs\": \"I want you to act as an api endpoint which returns all its responses in json. I will supply a list of products with ids and you will return in the following json format: {colors: [{id: <id>, color: <color>}, ...]} Here is the list of products with ids: ";
        List<String> productsString = allEnrichedProducts.stream().map(product -> product.getProductId() + " - " + product.getName()).toList();
        prompt += productsString + "\"";
        prompt += "}";
        String responseBody = _openAIRestClient.sendPrompt(createOpenAiRequestBody(prompt));
        return Map.of();
    }

    private String createOpenAiRequestBody(String prompt) {
        List<OpenAIApiRequest.Message> messages = new ArrayList<>();
        messages.add(new OpenAIApiRequest.Message(SYSTEM, prompt));

        OpenAIApiRequest openAIApiRequest = new OpenAIApiRequest(GPT_3_5_TURBO, messages, 0.2);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(openAIApiRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while writing the request object into a json body", e);
        }
    }

}
