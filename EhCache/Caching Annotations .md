# Mastering Caching Annotations and Strategies in Spring Boot Using Ehcache

## 1. Introduction to Caching in Spring Boot
Caching improves application performance by reducing redundant computations and database queries. Spring Boot provides a powerful caching abstraction with built-in support for Ehcache.

## 2. Caching Annotations in Spring Boot
Spring Boot offers several caching annotations for fine-grained cache control:

### 2.1 `@Cacheable`
- **Purpose:** Stores method results in the cache to avoid redundant executions.
- **Usage:**
  ```java
  @Service
  public class ProductService {
      @Cacheable(value = "products", key = "#id")
      public Product getProductById(Long id) {
          return productRepository.findById(id).orElse(null);
      }
  }
  ```
- **Explanation:** If the cache contains the value for `id`, the method is skipped, and the cached value is returned.

### 2.2 `@CachePut`
- **Purpose:** Updates the cache with the latest method execution result.
- **Usage:**
  ```java
  @CachePut(value = "products", key = "#product.id")
  public Product updateProduct(Product product) {
      return productRepository.save(product);
  }
  ```
- **Explanation:** Ensures the cache is updated every time the method executes.

### 2.3 `@CacheEvict`
- **Purpose:** Removes data from the cache when it becomes outdated.
- **Usage:**
  ```java
  @CacheEvict(value = "products", key = "#id")
  public void deleteProduct(Long id) {
      productRepository.deleteById(id);
  }
  ```
- **Explanation:** Ensures that outdated data does not persist in the cache.

### 2.4 `@Caching`
- **Purpose:** Combines multiple cache annotations for complex scenarios.
- **Usage:**
  ```java
  @Caching(
      put = { @CachePut(value = "products", key = "#product.id") },
      evict = { @CacheEvict(value = "products", key = "#product.id") }
  )
  public Product saveOrUpdateProduct(Product product) {
      return productRepository.save(product);
  }
  ```

## 3. Custom Key Generation and Conditional Caching

### 3.1 Custom Key Generation
By default, Spring uses method parameters as cache keys. You can define custom keys using `keyGenerator`.
```java
@Bean
public KeyGenerator customKeyGenerator() {
    return (target, method, params) -> "CUSTOM_" + Arrays.toString(params);
}
```
```java
@Cacheable(value = "products", keyGenerator = "customKeyGenerator")
public Product getProduct(Long id) {
    return productRepository.findById(id).orElse(null);
}
```

### 3.2 Conditional Caching
- **Usage:** Cache only when specific conditions are met.
```java
@Cacheable(value = "products", key = "#id", condition = "#id > 10")
public Product getProduct(Long id) {
    return productRepository.findById(id).orElse(null);
}
```

## 4. Hands-On Coding Example
- **Project Setup:**
  - Add `spring-boot-starter-cache` and `ehcache` dependencies.
  - Enable caching in the main class:
    ```java
    @SpringBootApplication
    @EnableCaching
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }
    ```
  - Configure `ehcache.xml`:
    ```xml
    <ehcache>
        <cache name="products" maxEntriesLocalHeap="1000" timeToLiveSeconds="600"/>
    </ehcache>
    ```

## 5. Performance Considerations and Trade-Offs
- Use `@Cacheable` for read-heavy operations.
- Use `@CachePut` cautiously to avoid frequent cache updates.
- Optimize eviction policies to balance memory usage and performance.

## 6. Common Mistakes and Debugging Techniques
- **Forgetting `@EnableCaching`** → Caching will not work.
- **Incorrect cache key usage** → Data retrieval inconsistencies.
- **Cache not updating** → Use `@CachePut` instead of `@Cacheable`.
- **Debugging:** Enable logging:
  ```properties
  logging.level.org.springframework.cache=DEBUG
  ```

## 7. Advanced Strategies for Large-Scale Applications
### 7.1 Cache Tuning
- Adjust heap size and expiration policies.
- Use `maxEntriesLocalHeap` wisely.

### 7.2 Eviction Policies
- **Least Recently Used (LRU)** → Removes least accessed items.
- **Time-To-Live (TTL)** → Automatically expires after a set duration.

### 7.3 Persistence Strategies
- Store cache in a persistent medium (e.g., disk, Redis) for durability.

## 8. Conclusion
Mastering caching in Spring Boot with Ehcache enhances application performance. By leveraging the right annotations, fine-tuning configurations, and implementing best practices, you can build efficient and scalable applications.

