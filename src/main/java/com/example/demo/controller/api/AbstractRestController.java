package com.example.demo.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.controller.BaseController;
import com.example.demo.controller.api.resource.ResourceFactory;
import com.example.demo.log.FunctionNameAware;

import lombok.extern.slf4j.Slf4j;

/**
 * 基底APIコントローラー
 */
@ResponseStatus(HttpStatus.OK)
@Slf4j
public abstract class AbstractRestController extends BaseController implements FunctionNameAware {
    @Autowired
    protected ResourceFactory resourceFactory;
}
