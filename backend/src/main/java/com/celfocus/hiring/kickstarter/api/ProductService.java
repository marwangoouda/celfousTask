package com.celfocus.hiring.kickstarter.api;

import com.celfocus.hiring.kickstarter.db.repo.ProductRepository;
import com.celfocus.hiring.kickstarter.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<? extends Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<? extends Product> getProduct(String sku) {
        return productRepository.findBySku(sku);
    }
}
