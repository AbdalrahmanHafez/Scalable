package com.example.productApp.services;

import com.example.productApp.logs.logsSender;
import com.example.productApp.models.Product;
import com.example.productApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    @Autowired
    private MongoTemplate mongoTemplate;
    private final ProductRepository productRepository;

    @Autowired
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    //private final Logger logger;

    @Autowired
    public ProductService(ProductRepository productRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.productRepository = productRepository;
        //this.logger = LoggerFactory.getLogger(ProductService.class);
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public CompletableFuture<List<Product>> getProducts() {

        CompletableFuture<List<Product>> products = CompletableFuture.supplyAsync(() -> {
//            System.out.println("Thread: " + Thread.currentThread().getName());
//            try {
//                Thread.sleep(10000); // Wait for tasks to start executing
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return productRepository.findAll();
        }, threadPoolTaskExecutor);
        return products;
    }
    public List<Product> getProductsByNameRegex(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productName").regex(name));
        return mongoTemplate.find(query, Product.class);
    }
    public Product getProductById(String productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            String errorMessage = "App with id" + " " + productId + " " + "is not here";
            logsSender.sendErrorMessage(errorMessage);
            return null;
        } else {
            String message = "App with id" + " " + productId + " " + "has been found successfully";
            logsSender.sendLogMessage(message);
        }
        return productOptional.get();
    }

    public CompletableFuture<Product> getProductByName(String productName) {
        CompletableFuture<Product> productOptionalCF = CompletableFuture.supplyAsync(() -> {
            Optional<Product> productOptional = Optional.ofNullable(productRepository
                    .findProductByName(productName));

            if (productOptional.isEmpty()) {
                String errorMessage = "App with name" + " " + productName + " " + "is not here";
                logsSender.sendErrorMessage(errorMessage);
                return null;
            } else {
                String message = "App with name" + " " + productName + " " + "has been found successfully";
                logsSender.sendLogMessage(message);
            }
            return productOptional.get();

        }, threadPoolTaskExecutor);
        return productOptionalCF;
    }


    public CompletableFuture<List<Product>> getProductsByCategoryId(String category_id) {
        CompletableFuture<List<Product>> productsCF = CompletableFuture.supplyAsync(() -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("category_id").is(category_id));

            List<Product> products = mongoTemplate.find(query, Product.class);
            if (products.isEmpty()) {
                String errorMessage = "No Apps in this category" + category_id;
                logsSender.sendErrorMessage(errorMessage);
            } else {
                String message = "Apps in category" + " " + category_id + " " + "have been found successfully";
                logsSender.sendLogMessage(message);
            }
            return products;
        }, threadPoolTaskExecutor);
        return productsCF;
    }

    public CompletableFuture<String> addNewProduct(Product product) {
        CompletableFuture<String> messageCF = CompletableFuture.supplyAsync(() -> {
            Optional<Product> productOptional =
                    Optional.ofNullable(productRepository.findProductByName(product.getProductName()));
            String message;
            if (productOptional.isPresent()) {
                message = "Name" + " " + product.getProductName() + " " + "is taken";
                logsSender.sendErrorMessage(message);
            } else {
                productRepository.save(product);
                message = "App with id" + " " + product.getProductId() + " " + "has been added successfully with id";
                logsSender.sendLogMessage(message);
            }
            return message;
        }, threadPoolTaskExecutor);
        return messageCF;
    }


    public CompletableFuture<String> deleteProduct(String productId) {
        CompletableFuture<String> messageCF = CompletableFuture.supplyAsync(() -> {
            Optional<Product> productOptional = productRepository
                    .findById(productId);
            String message;
            if (productOptional.isEmpty()) {
                message = "App with id" + " " + productId + " " + "does not exists";
                logsSender.sendErrorMessage(message);
            } else {
                productRepository.deleteById(productId);
                message = "App with id" + " " + productId + " " + "deleted successfully";
                logsSender.sendLogMessage(message);
            }
            return message;

        }, threadPoolTaskExecutor);
        return messageCF;
    }

    public CompletableFuture<String> updateProduct(String productId,
                                String productName,
                                String description,
                                String version) {

        CompletableFuture<String> messageCF = CompletableFuture.supplyAsync(() -> {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() ->
//                        new IllegalStateException(
//                                "App with id" + " " + productId + " " + "does not exists"
//                        ));
//        ;
            Optional<Product> productOptional = productRepository
                    .findById(productId);
            String message;
            if (productOptional.isEmpty()) {
                message = "App with id" + " " + productId + " " + "does not exists";
                logsSender.sendErrorMessage(message);
            } else {
                if (productName != null &&
                        productName.length() > 0 &&
                        !Objects.equals(productOptional.get().getProductName(), productName)) {
                    productOptional.get().setProductName(productName);
                }
                if (description != null &&
                        description.length() > 0 &&
                        !Objects.equals(productOptional.get().getDescription(), description)) {
                    productOptional.get().setDescription(description);
                }
                if (version != null &&
                        version.length() > 0 &&
                        !Objects.equals(productOptional.get().getVersion(), version)) {
                    productOptional.get().setVersion(version);
                }
                productRepository.save(productOptional.get());
                message = "App with id" + " " + productId + " " + "has been updated successfully";
                logsSender.sendLogMessage(message);
            }
            return message;
        }, threadPoolTaskExecutor);
        return messageCF;

    }

    public String updateAverageRating(String productId) {
        String message;
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            message = "App with id" + " " + productId + " " + "does not exists";
            logsSender.sendErrorMessage(message);
        } else {
            List<Integer> TheRating = productOptional.get().getRating();

            if (TheRating != null) {
                int zeroStaramount = TheRating.get(0);
                int oneStaramount = TheRating.get(1);
                int twoStaramount = TheRating.get(2);
                int threeStaramount = TheRating.get(3);
                int fourStaramount = TheRating.get(4);

                int totalamount = zeroStaramount + oneStaramount + twoStaramount + threeStaramount + fourStaramount;

                double newaveragerating = ((double) ((zeroStaramount * 0) + (oneStaramount) + (twoStaramount * 2) + (threeStaramount * 3) + (fourStaramount * 4)) / totalamount);

                productOptional.get().setaveragerating(newaveragerating);
                productRepository.save(productOptional.get());
            }

            message = "App with id" + " " + productId + " " + "has updated average rating successfully";
            logsSender.sendLogMessage(message);
        }
        return message;
    }

    public String increase0starrating(String productId){
        String message;
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            message = "App with id" + " " + productId + " " + "does not exists";
            logsSender.sendErrorMessage(message);
        }
        else{
            List<Integer> TheRating= productOptional.get().getRating();

            if(TheRating!=null){
                int zeroStar;
                zeroStar = TheRating.get(0);
                zeroStar=zeroStar+1;
        
                TheRating.set(0, zeroStar);
                TheRating= productOptional.get().getRating();
                int zeroStaramount = TheRating.get(0);
                int oneStaramount = TheRating.get(1);
                int twoStaramount = TheRating.get(2);
                int threeStaramount = TheRating.get(3);
                int fourStaramount = TheRating.get(4);
        
                int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;
        
                double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
        
                productOptional.get().setaveragerating(newaveragerating);
        
        
                productRepository.save(productOptional.get());
            }

            message = "App with id" + " " + productId + " " + "has increases 0star successfully";
            logsSender.sendLogMessage(message);
        }
        return message;
        
      

    }

    public String increase1starrating(String productId){
        String message;
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            message = "App with id" + " " + productId + " " + "does not exists";
            logsSender.sendErrorMessage(message);
        }
        else{
            List<Integer> TheRating= productOptional.get().getRating();

            if(TheRating!=null){
                int oneStar;
                oneStar = TheRating.get(1);
                oneStar=oneStar+1;
               
        
                TheRating.set(1, oneStar);
        
        
                TheRating= productOptional.get().getRating();
                int zeroStaramount = TheRating.get(0);
                int oneStaramount = TheRating.get(1);
                int twoStaramount = TheRating.get(2);
                int threeStaramount = TheRating.get(3);
                int fourStaramount = TheRating.get(4);
        
                int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;
        
                double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
        
                productOptional.get().setaveragerating(newaveragerating);
        
                productRepository.save(productOptional.get());
            }
            message = "App with id" + " " + productId + " " + "has increases 1star successfully";
            logsSender.sendLogMessage(message);
        }
       
        return message;

    }


    public String increase2starrating(String productId){
        String message;
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            message = "App with id" + " " + productId + " " + "does not exists";
            logsSender.sendErrorMessage(message);
        }
        else{
            List<Integer> TheRating= productOptional.get().getRating();

            if(TheRating!=null){

                int twoStar;
                twoStar = TheRating.get(2);
                twoStar=twoStar+1;
        
                TheRating.set(2, twoStar);
        
        
                TheRating = productOptional.get().getRating();
                int zeroStaramount = TheRating.get(0);
                int oneStaramount = TheRating.get(1);
                int twoStaramount = TheRating.get(2);
                int threeStaramount = TheRating.get(3);
                int fourStaramount = TheRating.get(4);
        
                int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;
        
                double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
        
                productOptional.get().setaveragerating(newaveragerating);
        
                productRepository.save(productOptional.get());

            }
            message = "App with id" + " " + productId + " " + "has increases 2star successfully";
            logsSender.sendLogMessage(message);

        }
       
        return message;

    }

    public String increase3starrating(String productId) {
        String message;
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            message = "App with id" + " " + productId + " " + "does not exists";
            logsSender.sendErrorMessage(message);
        }
        else{
            List<Integer> TheRating = productOptional.get().getRating();
            if(TheRating!=null){
                int threeStar;
                threeStar = TheRating.get(3);
                threeStar = threeStar + 1;
        
                TheRating.set(3, threeStar);
        
        
                TheRating = productOptional.get().getRating();
                int zeroStaramount = TheRating.get(0);
                int oneStaramount = TheRating.get(1);
                int twoStaramount = TheRating.get(2);
                int threeStaramount = TheRating.get(3);
                int fourStaramount = TheRating.get(4);
        
                int totalamount = zeroStaramount + oneStaramount + twoStaramount + threeStaramount + fourStaramount;
        
                double newaveragerating = ((double) ((zeroStaramount * 0) + (oneStaramount) + (twoStaramount * 2) + (threeStaramount * 3) + (fourStaramount * 4)) / totalamount);
        
                productOptional.get().setaveragerating(newaveragerating);
        
                productRepository.save(productOptional.get());

            }
            message = "App with id" + " " + productId + " " + "has increases 3star successfully";
            logsSender.sendLogMessage(message);
        }
       
        return message;

    }

    public String increase4starrating(String productId) {
        String message;
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            message = "App with id" + " " + productId + " " + "does not exists";
            logsSender.sendErrorMessage(message);
        }
        else{
            List<Integer> TheRating = productOptional.get().getRating();
            if(TheRating!=null){
                int fourStar;
                fourStar = TheRating.get(4);
                fourStar = fourStar + 1;
        
                TheRating.set(4, fourStar);
        
        
                TheRating = productOptional.get().getRating();
                int zeroStaramount = TheRating.get(0);
                int oneStaramount = TheRating.get(1);
                int twoStaramount = TheRating.get(2);
                int threeStaramount = TheRating.get(3);
                int fourStaramount = TheRating.get(4);
        
                int totalamount = zeroStaramount + oneStaramount + twoStaramount + threeStaramount + fourStaramount;
        
                double newaveragerating = ((double) ((zeroStaramount * 0) + (oneStaramount) + (twoStaramount * 2) + (threeStaramount * 3) + (fourStaramount * 4)) / totalamount);
        
                productOptional.get().setaveragerating(newaveragerating);
        
                productRepository.save(productOptional.get());
            }
            message = "App with id" + " " + productId + " " + "has increases 4star successfully";
            logsSender.sendLogMessage(message);
        }
       
        return message;

    }




    public String increasedownloadcount(String productId) {
        String message;
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            message = "App with id" + " " + productId + " " + "does not exists";
            logsSender.sendErrorMessage(message);
        }
        else{
            int downloadcountamount = productOptional.get().getDownload_count();

            downloadcountamount=downloadcountamount+1;
            productOptional.get().setDownload_count(downloadcountamount);
           
            productRepository.save(productOptional.get());
            message = "App with id" + " " + productId + " " + "has downloadcount successfully";
            logsSender.sendLogMessage(message);
        }
       
        return message;

    }

}
