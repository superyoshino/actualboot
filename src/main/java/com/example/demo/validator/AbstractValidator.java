package com.example.demo.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * 入力チェック基底クラス
 */
@Slf4j
public abstract class AbstractValidator<T> implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void validate(final Object target, final Errors errors) {
		try {
			boolean hasErrors = errors.hasErrors();
			
			if(!hasErrors || passThruBeanValidaton(hasErrors)) {
				//各機能で実装しているバリデーションを実行する
				doValidate((T) target,errors);
			}
		} catch(Exception e) {
			log.error("validate error", e);
		}
	}

	/**
	 * 入力チェック実施
	 * 
	 * @param form
	 * @param errors
	 */
	protected abstract void doValidate(final T form, final Errors errors);

	/**
	 * 相関チェックバリデーションを実施するかどうかを示す値を返す。
	 * デフォルトは、JSR-303 バリデーションでエラーがあった場合に相関チェックを実施しない
	 * 
	 * @param return
	 */
	protected boolean passThruBeanValidaton(boolean hasErrors) {
		return false;
	}
	
	
}
