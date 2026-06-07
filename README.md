
Employee Service with Redis Cache

This project demonstrates how to implement Redis caching in a Spring Boot Employee Service to improve performance and reduce database load.

🚀 Features
CRUD operations for Employee
Redis-based caching using Spring Cache
Cache abstraction with annotations
Improved API performance
Clean layered architecture (Controller → Service → Repository)
🛠️ Tech Stack
Java 8+
Spring Boot
Spring Data JPA
Spring Cache
Redis
Maven
MySQL / H2 (configurable)
📁 Project Structure
employee-service
│── controller
│   └── EmployeeController.java
│── service
│   └── EmployeeService.java
│── repository
│   └── EmployeeRepository.java
│── entity
│   └── Employee.java
│── config
│   └── RedisConfig.java
│── application.yml
⚙️ Redis Setup
1. Install Redis
On Windows:

Use Redis via Docker or WSL

Using Docker:
docker run -d -p 6379:6379 redis
Verify Redis:
redis-cli ping

Expected output:

PONG
🔧 Configuration
application.yml
spring:
  cache:
    type: redis
  redis:
    host: localhost
    port: 6379

  datasource:
    url: jdbc:mysql://localhost:3306/employee_db
    username: root
    password: password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
🧠 Enable Caching
Main Class
@SpringBootApplication
@EnableCaching
public class EmployeeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}
🧩 Redis Configuration
@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).build();
    }
}
📌 Caching Implementation
Service Layer
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Cacheable(value = "employees", key = "#id")
    public Employee getEmployeeById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @CachePut(value = "employees", key = "#employee.id")
    public Employee updateEmployee(Employee employee) {
        return repository.save(employee);
    }

    @CacheEvict(value = "employees", key = "#id")
    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
}
📡 API Endpoints
Method	Endpoint	Description
GET	/employees/{id}	Get employee by ID
POST	/employees	Create employee
PUT	/employees	Update employee
DELETE	/employees/{id}	Delete employee
⚡ How Caching Works
First API call → Data fetched from DB → Stored in Redis
Next calls → Data served from Redis (faster ⚡)
Update/Delete → Cache is updated/evicted automatically
🧪 Testing Cache
Call:
GET /employees/1

👉 First time → DB hit

Call again:
GET /employees/1

👉 Second time → Redis cache hit

🔍 Verify in Redis
redis-cli
keys *
📈 Benefits
🚀 Faster response time
🧠 Reduced database load
🔄 Automatic cache synchronization
📉 Scalable system design
⚠️ Best Practices
Use meaningful cache names
Avoid caching frequently changing data
Set TTL (Time-To-Live) for cache if needed
Handle cache invalidation properly
🔮 Future Enhancements
Add TTL configuration
Use Redis Cluster
Implement distributed locking
Add cache monitoring
👨‍💻 Author

Pankaj Waghmare
Senior Java Developer | Spring Boot | Microservices


@EnableCaching on main class 
implements serializable for DTO

GET @Cacheable(cacheNames = "employee", key = "#id")
POST @CachePut(cacheNames = "employee", key = "#result.id")
PUT @CachePut(cacheNames = "employee", key = "#id")
DELETE @CacheEvict(cacheNames = "employee", key = "#id")


For extra properties like timetolive ,enabletimetoIdle ,and key ad value serialized to store in proper format like string for key or json for response [data] in redis cache 

@Configuration
public class cacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("my-redis-cache")
                .entryTtl(Duration.ofSeconds(120))
                .enableTimeToIdle()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }


}