package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        log.debug("Creating a new product {}", productRequest.name());


        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        /*
        can use both
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());*/

        // persist a product
        productRepository.save(product);

        log.info("Product {} is saved", product.getId());
        return new ProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice());


    }

    @Override
    public List<ProductResponse> getAllProducts() {

        log.debug("Getting all products");
        List<Product> products = productRepository.findAll();
/*
        return products.stream().map(product -> mapToProductResponse(product)).toList();
*/

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice());
    }

    @Override
    public String updateProduct(String id, ProductRequest productRequest) {
        log.debug("Updating product {}", id);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Product product = mongoTemplate.findOne(query, Product.class);

        if(product != null) {
            product.setName(productRequest.name());
            product.setDescription(productRequest.description());
            product.setPrice(productRequest.price());
            return productRepository.save(product).getId();
        }
        return id;

    }

    @Override
    public void deleteProduct(String id) {
        log.debug("Deleting product with id {}", id);
        productRepository.deleteById(id);
    }
}
