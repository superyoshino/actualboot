package com.example.demo.mail;

import static com.example.demo.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.MailTemplateDao;
import com.example.demo.dto.Page;
import com.example.demo.dto.Pageable;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.service.BaseRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * メールテンプレートリポジトリ
 */
@Repository
public class MailTemplateRepository extends BaseRepository {

    @Autowired
    MailTemplateDao mailTemplateDao;

    /**
     * メールテンプレートを複数取得します。
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<MailTemplate> findAll(MailTemplateCriteria criteria, Pageable pageable) {
        // ページングを指定する
        val options = createSelectOptions(pageable).count();
        val data = mailTemplateDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * メールテンプレートを取得します。
     *
     * @param criteria
     * @return
     */
    public Optional<MailTemplate> findOne(MailTemplateCriteria criteria) {
        // 1件取得
        return mailTemplateDao.select(criteria);
    }

    /**
     * メールテンプレートを取得します。
     *
     * @return
     */
    public MailTemplate findById(final Long id) {
        // 1件取得
        return mailTemplateDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("mailTemplate_id=" + id + " のデータが見つかりません。"));
    }

    /**
     * メールテンプレートを追加します。
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate create(final MailTemplate inputMailTemplate) {
        // 1件登録
    	System.out.println("メールリポジトリのcreate.inputMailTemplate.getTemplateBody()"+ inputMailTemplate.getTemplateBody() +"を呼び出し");
        mailTemplateDao.insert(inputMailTemplate);

        return inputMailTemplate;
    }

    /**
     * メールテンプレートを更新します。
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate update(final MailTemplate inputMailTemplate) {
        // 1件更新
        int updated = mailTemplateDao.update(inputMailTemplate);

        if (updated < 1) {
            throw new NoDataFoundException("mailTemplate_id=" + inputMailTemplate.getId() + " のデータが見つかりません。");
        }

        return inputMailTemplate;
    }

    /**
     * メールテンプレートを論理削除します。
     *
     * @return
     */
    public MailTemplate delete(final Long id) {
        val mailTemplate = mailTemplateDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("mailTemplate_id=" + id + " のデータが見つかりません。"));

        int updated = mailTemplateDao.delete(mailTemplate);

        if (updated < 1) {
            throw new NoDataFoundException("mailTemplate_id=" + id + " は更新できませんでした。");
        }

        return mailTemplate;
    }
}
