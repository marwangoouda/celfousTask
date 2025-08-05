package com.celfocus.hiring.kickstarter.db.repo;


import com.celfocus.hiring.kickstarter.db.entity.ProductEntity;
import com.celfocus.hiring.kickstarter.util.ProductsLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ProductRepository extends Repository<ProductEntity, String> {
    List<ProductEntity> findAll();

    Optional<ProductEntity> findBySku(String sku);

    ProductEntity save(ProductEntity productEntity);

    static ProductRepository create(ObjectMapper objectMapper) {
        return new ProductRepositoryImpl(objectMapper);
    }
}

class ProductRepositoryImpl implements ProductRepository {

    private final List<ProductEntity> products;

    public ProductRepositoryImpl(@Autowired ObjectMapper objectMapper) {
        products = ProductsLoader.loadProducts(objectMapper);
    }

    @Override
    public List<ProductEntity> findAll() {
        return products;
    }

    @Override
    public Optional<ProductEntity> findBySku(String sku) {
        return products.stream()
                .filter(product -> product.getSku().equals(sku))
                .findFirst();
    }

    @Override
    public ProductEntity save(ProductEntity productEntity) {
        products.removeIf(p -> p.getSku().equals(productEntity.getSku()));
        products.add(productEntity);
        return productEntity;
    }
}
