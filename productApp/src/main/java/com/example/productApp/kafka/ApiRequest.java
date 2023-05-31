package com.example.productApp.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequest implements Serializable {
    private String action;
    private String appnameid;
    private int threads;
    private int dbconnections;
    private int reportinglevel;
}
