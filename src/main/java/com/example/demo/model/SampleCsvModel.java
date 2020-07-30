package com.example.demo.model;

import lombok.Data;

@Data
public class SampleCsvModel {

    private int id;
    private String name;


    public SampleCsvModel( int id,String name){
        this.id = id;
        this.name = name;
    }
}