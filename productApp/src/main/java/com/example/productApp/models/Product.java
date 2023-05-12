package com.example.productApp.models;


import jdk.jfr.Category;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("Product")
public class Product {

    @Id
    private String productId;

    private String productName;

    private String description;

    private int download_count;

    private String version;

    private String category_id;

//    @DBRef
//    private Category category;

    public Product() {
    }

    public Product(String productId,
                   String productName,
                   String description,
                   int download_count,
                   String version,
                   String category_id) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.download_count = download_count;
        this.version = version;
        this.category_id = category_id;
    }

    public Product(String productName,
                   String description,
                   int download_count,
                   String version,
                   String category_id) {
        this.productName = productName;
        this.description = description;
        this.download_count = download_count;
        this.version = version;
        this.category_id = category_id;
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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + productId +
                ", name='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", download_count=" + download_count +
                ", version='" + version + '\'' +
                '}';
    }
}
