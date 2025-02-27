# **Ehcache in Spring Boot: Beginner to Advanced Guide**

## **Introduction to Ehcache**

### **What is Ehcache?**
Ehcache is a widely used, Java-based caching library designed to improve application performance by reducing database access and computational load. It can be used as a local in-memory cache or as a distributed cache in multi-node architectures.

### **Why Use Ehcache?**
- **Performance Improvement**: Reduces database queries and computational overhead.
- **Scalability**: Supports local, distributed, and persistent caching.
- **Spring Boot Integration**: Works seamlessly with Spring Boot’s caching abstraction.
- **Flexibility**: Provides fine-grained control over cache behavior with customizable configurations.

### **How Ehcache Works**
- **Key-Value Storage**: Data is stored in a key-value structure.
- **Eviction Policies**: Defines when and how cache entries are removed (LRU, LFU, etc.).
- **Expiration Policies**: Specifies time-based cache expiration.
- **Persistence Options**: Supports in-memory, disk-based, and clustered caching.

---

## **Setting Up Ehcache in Spring Boot**

### **Step 1: Add Dependencies**
Add Ehcache and Spring Boot caching dependencies to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>
    <dependency>
        <groupId>net.sf.ehcache</groupId>
        <artifactId>ehcache</artifactId>
        <version>2.10.9.2</version>
    </dependency>
</dependencies>
```

### **Step 2: Enable Caching in Spring Boot**
Annotate your Spring Boot main class with `@EnableCaching`:

```java
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCaching
public class EhcacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(EhcacheApplication.class, args);
    }
}
```

### **Step 3: Configure Ehcache (`ehcache.xml`)**
Create an `ehcache.xml` file in `src/main/resources`:

```xml
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd">

    <diskStore path="java.io.tmpdir"/>

    <cache name="studentsCache"
           maxEntriesLocalHeap="1000"
           timeToLiveSeconds="600"
           overflowToDisk="true"
           diskPersistent="true"/>
</ehcache>
```

### **Step 4: Configure Ehcache in Spring Boot**
Define a cache manager bean in `CacheConfig.java`:

```java
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import net.sf.ehcache.CacheManager;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public EhCacheCacheManager cacheManager() {
        return new EhCacheCacheManager(CacheManager.create(new ClassPathResource("ehcache.xml").getURL()));
    }
}
```

---

## **Using Ehcache in Spring Boot**

### **1. Caching with `@Cacheable`**

```java
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Cacheable("studentsCache")
    public String getStudentById(Long id) {
        simulateSlowService();
        return "Student " + id;
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### **2. Removing Entries with `@CacheEvict`**

```java
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @CacheEvict(value = "studentsCache", key = "#id")
    public void deleteStudent(Long id) {
        System.out.println("Deleting student with ID: " + id);
    }
}
```

### **3. Updating Cache with `@CachePut`**

```java
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @CachePut(value = "studentsCache", key = "#id")
    public String updateStudent(Long id, String name) {
        return "Updated Student " + id + " - " + name;
    }
}
```

---

## **Advanced Topics**

### **1. Custom Cache Configuration**
Configure cache programmatically:

```java
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomCacheConfig {
    @Bean
    public EhCacheCacheManager cacheManager() {
        net.sf.ehcache.CacheManager cacheManager = new net.sf.ehcache.CacheManager();
        cacheManager.addCache(new net.sf.ehcache.Cache(
            new CacheConfiguration("customCache", 1000)
                .timeToLiveSeconds(600)
                .memoryStoreEvictionPolicy("LFU")));
        return new EhCacheCacheManager(cacheManager);
    }
}
```

### **2. Distributed Caching with Terracotta**

Add the dependency:

```xml
<dependency>
    <groupId>org.terracotta</groupId>
    <artifactId>ehcache-clustered</artifactId>
    <version>2.10.9.2</version>
</dependency>
```

Modify `ehcache.xml`:

```xml
<cache name="distributedCache"
       maxEntriesLocalHeap="1000"
       eternal="false"
       timeToLiveSeconds="600"
       memoryStoreEvictionPolicy="LFU">
    <terracotta clustered="true"/>
</cache>
```

### **3. Persistent Caching**

```xml
<cache name="persistentCache"
       maxEntriesLocalHeap="500"
       eternal="false"
       timeToIdleSeconds="300"
       overflowToDisk="true"
       diskPersistent="true"/>
```

### **4. Performance Optimization**
- Use **LFU eviction policy**.
- Set **TTL and TTI** appropriately.
- Tune **heap size and overflow-to-disk settings**.

### **5. Debugging and Monitoring**

Enable Ehcache statistics in `ehcache.xml`:

```xml
<ehcache>
    <monitoring>autodetect</monitoring>
</ehcache>
```

---

## **Hands-on Exercises**
- Implement a REST API that caches user details.
- Benchmark application performance with and without caching.
- Configure a multi-node Ehcache setup.
- Integrate Ehcache with PostgreSQL.

---

## **Conclusion**
Ehcache is a powerful caching solution that integrates well with Spring Boot. Mastering its annotations, configurations, and optimizations can significantly enhance application performance and scalability.
