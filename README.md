# Microservices-Based REST API Backend
This project is a REST API backend service developed with Java Spring Boot, utilizing a microservice architecture to ensure scalability, efficiency, and reliability. The architecture incorporates an API gateway, RabbitMQ for load balancing, and multiple specialized microservices.
![image](https://github.com/user-attachments/assets/6450f3d3-6f0c-4a96-a88b-d9879df407e1)

## Features
* __Microservice Architecture__:
  The project is divided into four dedicated microservices for better modularity and maintainability:
* __Product__: Manages product-related operations.
* __User__: Handles authentication, login, and user management.
* __Media Storage__: Manages media files and storage operations.
* __Downloads__: Handles file downloads and related tasks.
* __API Gateway__: Centralized gateway for routing requests to appropriate microservices, ensuring seamless communication.

## Load Balancing:
* RabbitMQ is used to distribute workloads effectively, enhancing performance and reliability.

## Thread Pooling:
* Implemented thread pooling for efficient resource management and to optimize concurrent request handling.

## Controller Service:
* Manages the lifecycle and instances of the microservices, ensuring availability and scalability.

## Technologies Used
* Backend Framework: Java Spring Boot
* Messaging System: RabbitMQ
* API Gateway: Spring Cloud Gateway
* Thread Management: Java Thread Pooling
* Database: Compatible with various databases (e.g., MySQL, PostgreSQL).

## Installation
1. Clone the repository:
  ```bash
  git clone https://github.com/AbdalrahmanHafez/Scalable
  ```

2. Navigate to the project directory:
  ```bash
  cd Scalable
  ```

3. Build the project using Maven:
  ```mvn clean install```


## Start the microservices:

Navigate to each microservice directory (e.g., Product, User) and start them individually:
    ```mvn spring-boot:run -f pom.xml```

## Start the RabbitMQ server:
Ensure RabbitMQ is installed and running.
or
```docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management```  

```docker run -it --rm --name some-redis -p 6379:6379 redis```

## Access the API Gateway to route requests:
Open your browser or API client and use the gateway's base URL: http://localhost:<gateway-port>


## Contributing
Contributions are welcome! Fork the repository, submit issues, and open pull requests to suggest new features or improvements.





