package com.ecommerce.shoppingcart.service.impl;

import com.ecommerce.shoppingcart.dto.ProductDTO;
import com.ecommerce.shoppingcart.exception.ProductNotFoundException;
import com.ecommerce.shoppingcart.repository.ProductRepository;
import com.ecommerce.shoppingcart.service.ProductService;
import com.ecommerce.shoppingcart.entity.Product;
import com.ecommerce.shoppingcart.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  @Override
  public ProductDTO findProductByID(Integer id) {
    logger.info("Find Product by Id with id:" + id);
    Product product = productRepository.findProductById(id);
    if (Optional.ofNullable(product).isEmpty()) {
      throw new ProductNotFoundException("No product of id " + id);
    }
    return productMapper.fromEntity(product);
  }

  @Override
  public List<ProductDTO> filterProduct(String keyword, Float price) {
    List<Product> products = productRepository.filter(keyword, price.doubleValue());
    return products.stream().map(productMapper::fromEntity).collect(Collectors.toList());
  }

  /**
   * Decrease Product quantity in case update Cart.
   *
   * @param id The Product id
   * @param number The decrease number
   * @return The ProductDTO has been decreased.
   * @author ManhTuan
   */
  @Override
  @Transactional
  public ProductDTO decreaseProductQuantity(Product product, Integer number) {
    int currentQuantity = product.getQuantity();
    if (currentQuantity < number) {
      logger.error("Quantity to be reduced is more than existing quantity.");
      throw new IllegalArgumentException("Quantity to be reduced is more than existing quantity.");
    }
    logger.info("Decrease Product quantity by " + number + " unit(s).");
    product.setQuantity(currentQuantity - number);
    Product decreasedQuantityProduct = productRepository.save(product);
    logger.info("Product quantity has been decreased " + number + " unit(s).");
    return productMapper.fromEntity(decreasedQuantityProduct);
  }

  /**
   * Increase Product quantity in case update Cart.
   *
   * @param id The Product id
   * @param number The increase number
   * @return The ProductDTO has been increased.
   * @author ManhTuan
   */
  @Override
  @Transactional
  public ProductDTO increaseProductQuantity(Product product, Integer number) {
    int currentQuantity = product.getQuantity();
    logger.info("Increase Product quantity by " + number + " unit(s).");
    product.setQuantity(currentQuantity + number);
    Product decreasedQuantityProduct = productRepository.save(product);
    logger.info("Product quantity has been increased " + number + " unit(s).");
    return productMapper.fromEntity(decreasedQuantityProduct);
  }

  @Override
  public List<ProductDTO> filterProduct(String keyword, Double price) {
    List<Product> products = productRepository.filter(keyword, price);
    return products.stream().map(productMapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  public List<ProductDTO> findProductByPriceWithPaging(Double price, Integer paging) {
    Pageable firstPageWithPagingElement = PageRequest.of(0, paging);
    Page<Product> allProduct = productRepository.findProductsByPrice(price, firstPageWithPagingElement);
    return allProduct.stream().map(productMapper::fromEntity).collect(Collectors.toList());
  }

  /**
   * Get Product Entity by id
   *
   * @param id The Integer id input
   * @return A Product Entity
   * @author ManhTuan
   */
  @Override
  public Product findProductEntityByID(Integer id) {
    logger.info("Find Product by Id with id:" + id);
    Product product = productRepository.findProductById(id);
    if (product == null) {
      logger.error("Product not found with id :" + id);
      throw new IllegalArgumentException("Product not found with id :" + id);
    }
    return product;
  }

}
