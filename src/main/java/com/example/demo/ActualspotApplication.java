package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.example.demo.aop.SetAuditInfoInterceptor;
import com.example.demo.dto.DefaultPageFactoryImpl;
import com.example.demo.dto.PageFactory;

import lombok.val;

@SpringBootApplication(scanBasePackageClasses = { ComponentScanBasePackage.class })
public class ActualspotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActualspotApplication.class, args);
	}

}
