package com.example.demo.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.example.demo.users.UserForm;

/**
 * ユーザ登録 入力チェック
 */
@Service
public class UserFormValidator extends AbstractValidator<UserForm> {

	@Override
	protected void doValidate(UserForm form, Errors errors) {
		if(!isEquals(form.getPassword(),form.getPasswordConfirm())) {
			errors.rejectValue("password", "users.unmatchPassword");
			errors.rejectValue("passwordConfirm", "users.unmatchPassword");
		}
	}
	
	
	/**
	 * Yoshino added
	 * 等値判定 使いまわせそうならutilの方へ引っ越し
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isEquals(String str1,String str2){
		if(str1 == str2)return true;
		if(str1 == null || str2 == null)return false;
		return str1.equals(str2);
	}


}
