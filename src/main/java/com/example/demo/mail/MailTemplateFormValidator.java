package com.example.demo.mail;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.example.demo.validator.AbstractValidator;


/**
 * メールテンプレート登録 入力チェック
 */
@Component
public class MailTemplateFormValidator extends AbstractValidator<MailTemplateForm> {

    @Override
    protected void doValidate(MailTemplateForm form, Errors errors) {

    }
}
