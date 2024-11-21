package com.celfocus.hiring.kickstarter.util;

import com.celfocus.hiring.kickstarter.db.entity.ProductEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

public class ProductsLoader {
    private static final String PRODUCTS_JSON_PATH = "products.json";

    public static List<ProductEntity> loadProducts(ObjectMapper objectMapper) {

        try {
            ClassPathResource classPathResource = new ClassPathResource(PRODUCTS_JSON_PATH);
            InputStream in = classPathResource.getInputStream();
            return objectMapper.readValue(in, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error reading products.json", e);
        }
    }
}
