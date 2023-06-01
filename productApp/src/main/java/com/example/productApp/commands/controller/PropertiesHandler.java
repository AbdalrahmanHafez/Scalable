package com.example.productApp.commands.controller;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesHandler {
    static Properties properties = new Properties();
    static OutputStream output;
    static InputStream input;

    public static void loadPropertiesHandler(){
        try {

            output = new FileOutputStream("application.properties");
            input = new FileInputStream("application.properties");

            // set the properties value
            properties.setProperty("max_db_threads", "20");
            properties.setProperty("max_app_threads", "20");
            properties.setProperty("freeze", "false");

            properties.store(output, null);
        } catch (Exception e) {
            System.out.println("error in loadPropertiesHandler");
        }
    }

    public static void addProperty(String key, String val) {
        try {
            input = new FileInputStream("application.properties");
            output = new FileOutputStream("application.properties");
            properties.load(input);
            properties.setProperty(key, val);
            properties.store(output, null);
        } catch(Exception e) {
            System.out.println("error in addProperty");
        }
    }

    public static String getProperty(String key) {
        try{
            input = new FileInputStream("application.properties");
            properties.load(input);
            return properties.getProperty(key);
        } catch(Exception e) {
            return "error";
        }
    }
}