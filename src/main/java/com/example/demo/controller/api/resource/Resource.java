package com.example.demo.controller.api.resource;

import java.util.List;

import com.example.demo.dto.Dto;

public interface Resource {

    List<? extends Dto> getData();

    void setData(List<? extends Dto> data);

    String getMessage();

    void setMessage(String message);
}
