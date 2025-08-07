package com.celfocus.hiring.kickstarter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.celfocus.hiring.kickstarter.db.repo.ProductRepository;
import com.celfocus.hiring.kickstarter.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "products", key = "'ALL_PRODUCTS'")
    public List<? extends Product> getProducts() {
        log.info("Fetching all products");
        List<? extends Product> products = productRepository.findAll();
        log.info("Successfully fetched {} products", products.size());
        return products;
    }

    @Cacheable(value = "products", key = "#sku")
    public Optional<? extends Product> getProduct(String sku) {
        log.info("Fetching product with SKU={}", sku);
        Optional<? extends Product> productOptional = productRepository.findBySku(sku);
        if (productOptional.isPresent()) {
            log.info("Product with SKU={} was found", sku);
        } else {
            log.warn("Product with SKU={} was not found", sku);
        }
        return productOptional;
    }
}
