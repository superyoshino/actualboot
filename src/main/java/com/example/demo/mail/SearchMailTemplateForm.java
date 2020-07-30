package com.example.demo.mail;


import com.example.demo.BaseSearchForm;
import com.example.demo.dto.Pageable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchMailTemplateForm extends BaseSearchForm implements Pageable {

    private static final long serialVersionUID = -6365336122351427141L;

    Long id;

    // メールテンプレートキー
    String templateKey;

    // メールタイトル
    String subject;

    // メール本文
    String templateBody;
}
