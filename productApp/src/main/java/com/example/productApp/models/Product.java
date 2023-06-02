package com.example.productApp.models;

import jdk.jfr.Category;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document("Product")
public class Product {

    @Id
    private String productId;

    private String productName;

    private String description;

    private int download_count;

    private String version;

    private String category_id;

    public double AverageRating;

    public List<Integer> TheRating;

//    @DBRef
//    private Category category;

    public Product() {
    }

    public Product(String productId,
                   String productName,
                   String description,
                   int download_count,
                   String version,
                   String category_id,
                   double AverageRating,
                   List<Integer> TheRating) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.download_count = download_count;
        this.version = version;
        this.category_id = category_id;
        this.AverageRating=AverageRating;
        this.TheRating=TheRating;
    }

    public Product(String productName,
                   String description,
                   int download_count,
                   String version,
                   String category_id,
                   double AverageRating,
                   List<Integer> TheRating) {
        this.productName = productName;
        this.description = description;
        this.download_count = download_count;
        this.version = version;
        this.category_id = category_id;
        this.AverageRating=AverageRating;
        this.TheRating=TheRating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setaveragerating(double AverageRating) {
        this.AverageRating= AverageRating;
    }
    public double getaveragerating() {
        return AverageRating;
    }
    public List<Integer> getRating() {
        return TheRating;
    }

    public void setrating( List<Integer> TheRating) {
        this.TheRating= TheRating;
    }

}
