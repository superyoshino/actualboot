package com.example.demo.mail;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.example.demo.dto.Page;
import com.example.demo.dto.Pageable;
import com.example.demo.service.BaseTransactionalService;

/**
 * メールテンプレートサービス
 */
@Service
public class MailTemplateService extends BaseTransactionalService {

    @Autowired
    MailTemplateRepository mailTemplateRepository;

    /**
     * メールテンプレートを複数取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<MailTemplate> findAll(MailTemplateCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return mailTemplateRepository.findAll(criteria, pageable);
    }

    /**
     * メールテンプレートを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<MailTemplate> findOne(MailTemplateCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return mailTemplateRepository.findOne(criteria);
    }

    /**
     * メールテンプレートを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public MailTemplate findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return mailTemplateRepository.findById(id);
    }

    /**
     * メールテンプレートを追加します。
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate create(final MailTemplate inputMailTemplate) {
        Assert.notNull(inputMailTemplate, "inputMailTemplate must not be null");
        return mailTemplateRepository.create(inputMailTemplate);
    }

    /**
     * メールテンプレートを更新します。
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate update(final MailTemplate inputMailTemplate) {
        Assert.notNull(inputMailTemplate, "inputMailTemplate must not be null");
        return mailTemplateRepository.update(inputMailTemplate);
    }

    /**
     * メールテンプレートを論理削除します。
     *
     * @return
     */
    public MailTemplate delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return mailTemplateRepository.delete(id);
    }
}
