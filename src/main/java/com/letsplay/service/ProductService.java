package com.letsplay.service;

import com.letsplay.dto.ProductRequest;
import com.letsplay.dto.ProductResponse;
import com.letsplay.exception.ResourceNotFoundException;
import com.letsplay.exception.UnauthorizedException;
import com.letsplay.model.Product;
import com.letsplay.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getOwner()))
                .toList();
    }

    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getOwner());
    }

    public ProductResponse create(ProductRequest request, String ownerId) {
        Product product = new Product(request.getName(), request.getDescription(), request.getPrice(), ownerId);
        Product saved = productRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(), saved.getOwner());
    }

    public ProductResponse update(String id, ProductRequest request, String ownerId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getOwner().equals(ownerId)) {
            throw new UnauthorizedException("Not authorized to update this product");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        Product saved = productRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(), saved.getOwner());
    }

    public void delete(String id, String ownerId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getOwner().equals(ownerId)) {
            throw new RuntimeException("Not authorized to delete this product");
        }

        productRepository.deleteById(id);
    }

    public List<ProductResponse> getByOwnerId(String ownerId) {
        return productRepository.findByOwner(ownerId).stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getOwner()))
                .toList();
    }
}

