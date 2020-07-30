package com.example.demo.model;

import lombok.Data;

@Data
public class SampleProductModel {

    private String firstName;
    private String lastName;
    private String email;

    public SampleProductModel(String firstname,String lastname, String email){
        this.firstName = firstname;
        this.lastName = lastname;
        this.email = email;
    }
}