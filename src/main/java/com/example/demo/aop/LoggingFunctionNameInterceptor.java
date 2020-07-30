package com.example.demo.aop;


/**
 * 機能名をログに出力する
 *
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;

import com.example.demo.WebConst;
import com.example.demo.log.FunctionNameAware;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFunctionNameInterceptor extends BaseHandlerInterceptor {

	private static final String MDC_FUNCTION_NAME = WebConst.MDC_FUNCTION_NAME;
	
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) 
	throws Exception{
		//コントローラの動作前
		val fna = getBean(handler,FunctionNameAware.class);
		if(fna != null) {
			val functionName = fna.getFunctionName();
			MDC.put(MDC_FUNCTION_NAME, functionName);
		}
		return true;
		
	}

}
