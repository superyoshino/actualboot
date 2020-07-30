package com.example.demo.mail;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;

import com.example.demo.dto.DomaDtoImpl;
import com.example.demo.mail.MailTemplate;

import lombok.Getter;
import lombok.Setter;

@Table(name = "mail_templates")
@Entity
@Getter
@Setter
public class MailTemplate extends DomaDtoImpl {

    private static final long serialVersionUID = -2997823123579780864L;

    @OriginalStates // 差分UPDATEのために定義する
    MailTemplate originalStates;

    @Id
    @Column(name = "mail_template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // カテゴリキー
    String categoryKey;

    // メールテンプレートキー
    String templateKey;

    // メールタイトル
    String subject;

    // メール本文
    String templateBody;
}
