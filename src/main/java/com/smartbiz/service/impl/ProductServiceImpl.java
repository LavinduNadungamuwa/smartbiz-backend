package com.smartbiz.service.impl;

import com.smartbiz.entity.Product;
import com.smartbiz.repository.ProductRepository;
import com.smartbiz.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existing = productRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setProductName(product.getProductName());
        existing.setCategory(product.getCategory());
        existing.setDescription(product.getDescription());
        existing.setUnitPrice(product.getUnitPrice());
        existing.setStockQuantity(product.getStockQuantity());

        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}