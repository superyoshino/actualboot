package com.example.demo.model;

import lombok.Data;

@Data
public class SampleExcelModel {

    private String lastname;
    private String firstname;
    private String email;

    public SampleExcelModel(String lastname, String firstname, String email){
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
    }
}