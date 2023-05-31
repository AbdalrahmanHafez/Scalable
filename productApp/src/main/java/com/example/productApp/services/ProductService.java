package com.example.productApp.services;

import com.example.productApp.models.Product;
import com.example.productApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private MongoTemplate mongoTemplate;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String productId) {
        Optional<Product> productOptional =
                productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            throw new IllegalStateException("App is not here");

        }

        return productOptional.get();
    }

    public Product getProductByName(String productName) {
        Optional<Product> productOptional = Optional.ofNullable(productRepository
                .findProductByName(productName));

        return productOptional.orElse(null);
    }


    public List<Product> getProductsByCategoryId(String category_id) {


        Query query = new Query();
        query.addCriteria(Criteria.where("category_id").is(category_id));

        return mongoTemplate.find(query, Product.class);
    }

    public void addNewProduct(Product product) {
        Optional<Product> productOptional =
                Optional.ofNullable(productRepository.findProductByName(product.getProductName()));

        if (productOptional.isPresent()) {
            throw new IllegalStateException("name is taken");

        }
        productRepository.save(product);
    }


    public void deleteProduct(String productId) {
        Optional<Product> productOptional = productRepository
                .findById(productId);
        if (productOptional.isEmpty()) {
            throw new IllegalStateException(
                    "product with id" + " " + productId + " " + "does not exists"
            );
        }
        productRepository.deleteById(productId);
    }


    public void updateProduct(String productId,
                              String productName,
                              String description,
                              String version) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id" + " " + productId + " " + "does not exists"
                ));
        ;
        if (productName != null &&
                productName.length() > 0 &&
                !Objects.equals(product.getProductName(), productName)) {
            product.setProductName(productName);
        }

        if (description != null &&
                description.length() > 0 &&
                !Objects.equals(product.getDescription(), description)) {
            product.setDescription(description);
        }

        if (version != null &&
                version.length() > 0 &&
                !Objects.equals(product.getVersion(), version)) {
            product.setVersion(version);
        }
        productRepository.save(product);

    }




    public void updateAverageRating(String productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id" + " " + productId + " " + "does not exists"
                ));
                List<Integer> TheRating= product.getRating();
                int zeroStaramount = TheRating.get(0);
                int oneStaramount = TheRating.get(1);
                int twoStaramount = TheRating.get(2);
                int threeStaramount = TheRating.get(3);
                int fourStaramount = TheRating.get(4);

                int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;

                double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
               
            product.setaveragerating(newaveragerating);


        productRepository.save(product);

    }






    public void increase0starrating(String productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id" + " " + productId + " " + "does not exists"
                ));
                List<Integer> TheRating= product.getRating();
                int zeroStar;
                  zeroStar = TheRating.get(0);
                  zeroStar=zeroStar+1;
               

        TheRating.set(0, zeroStar);



                 TheRating= product.getRating();
                int zeroStaramount = TheRating.get(0);
                int oneStaramount = TheRating.get(1);
                int twoStaramount = TheRating.get(2);
                int threeStaramount = TheRating.get(3);
                int fourStaramount = TheRating.get(4);

                int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;

                double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
               
            product.setaveragerating(newaveragerating);


        productRepository.save(product);

    }
















    public void increase1starrating(String productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id" + " " + productId + " " + "does not exists"
                ));
                List<Integer> TheRating= product.getRating();
                int oneStar;
                  oneStar = TheRating.get(1);
                  oneStar=oneStar+1;
                    // String starvalue=oneStar.substring(0, 6);
                    //     String amount=oneStar.substring(7);
                    //     int amountinint=Integer.parseInt(amount);
        //             amountinint=amountinint+1;
        // String finalvalue="";
        // finalvalue=finalvalue+starvalue+""+amountinint;

        TheRating.set(1, oneStar);


        TheRating= product.getRating();
        int zeroStaramount = TheRating.get(0);
        int oneStaramount = TheRating.get(1);
        int twoStaramount = TheRating.get(2);
        int threeStaramount = TheRating.get(3);
        int fourStaramount = TheRating.get(4);

        int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;

        double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
       
    product.setaveragerating(newaveragerating);

        productRepository.save(product);

    }




    public void increase2starrating(String productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id" + " " + productId + " " + "does not exists"
                ));
                List<Integer> TheRating= product.getRating();
                int twoStar;
                  twoStar = TheRating.get(2);
                  twoStar=twoStar+1;

        TheRating.set(2, twoStar);


        TheRating= product.getRating();
        int zeroStaramount = TheRating.get(0);
        int oneStaramount = TheRating.get(1);
        int twoStaramount = TheRating.get(2);
        int threeStaramount = TheRating.get(3);
        int fourStaramount = TheRating.get(4);

        int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;

        double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
       
    product.setaveragerating(newaveragerating);

        productRepository.save(product);

    }

    public void increase3starrating(String productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id" + " " + productId + " " + "does not exists"
                ));
                List<Integer> TheRating= product.getRating();
                int threeStar;
                threeStar = TheRating.get(3);
                threeStar=threeStar+1;

        TheRating.set(3, threeStar);


        TheRating= product.getRating();
        int zeroStaramount = TheRating.get(0);
        int oneStaramount = TheRating.get(1);
        int twoStaramount = TheRating.get(2);
        int threeStaramount = TheRating.get(3);
        int fourStaramount = TheRating.get(4);

        int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;

        double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
       
    product.setaveragerating(newaveragerating);

        productRepository.save(product);

    }




    
    public void increase4starrating(String productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "product with id" + " " + productId + " " + "does not exists"
                ));
                List<Integer> TheRating= product.getRating();
                int fourStar;
                fourStar = TheRating.get(4);
                fourStar=fourStar+1;

        TheRating.set(4, fourStar);


        TheRating= product.getRating();
        int zeroStaramount = TheRating.get(0);
        int oneStaramount = TheRating.get(1);
        int twoStaramount = TheRating.get(2);
        int threeStaramount = TheRating.get(3);
        int fourStaramount = TheRating.get(4);

        int totalamount=zeroStaramount+oneStaramount+twoStaramount+threeStaramount+fourStaramount;

        double newaveragerating=((double)((zeroStaramount*0)+(oneStaramount)+(twoStaramount*2)+(threeStaramount*3)+(fourStaramount*4))/totalamount);
       
    product.setaveragerating(newaveragerating);

        productRepository.save(product);

    }






    
}
