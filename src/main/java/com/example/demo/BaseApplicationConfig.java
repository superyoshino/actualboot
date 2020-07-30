package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.aop.SetAuditInfoInterceptor;
import com.example.demo.dto.DefaultPageFactoryImpl;
import com.example.demo.dto.PageFactory;

import lombok.val;

@Configuration
@ComponentScan({"com","com.example","com.example.demo","com.example.demo.service","com.example.demo.dto"})
public class BaseApplicationConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>, WebMvcConfigurer {
	
	@Bean
	public ModelMapper modelMapper() {
	 return DefaultModelMapperFactory.create();
	}
	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
	@Bean
	public LocalValidatorFactoryBean beanValidator(MessageSource messageSource) {
		val bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}
	
    @Bean
    public SetAuditInfoInterceptor setAuditInfoInterceptor() {
        // システム制御項目を保存してDB保存時に利用する
        return new SetAuditInfoInterceptor();
    }
    
    @Bean
    PageFactory pageFactory() {
        return new DefaultPageFactoryImpl();
    }

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	

}
