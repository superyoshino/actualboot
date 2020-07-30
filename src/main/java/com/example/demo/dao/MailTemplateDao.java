package com.example.demo.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.example.demo.mail.MailTemplate;
import com.example.demo.mail.MailTemplateCriteria;


@ConfigAutowireable
@Dao
public interface MailTemplateDao {

    /**
     * メールテンプレートを取得します。
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final MailTemplateCriteria criteria, final SelectOptions options,
            final Collector<MailTemplate, ?, R> collector);

    /**
     * メールテンプレートを1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<MailTemplate> selectById(Long id);

    /**
     * メールテンプレートを1件取得します。
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<MailTemplate> select(MailTemplateCriteria criteria);

    /**
     * メールテンプレートを登録します。
     *
     * @param mailtemplate
     * @return
     */
    @Insert
    int insert(MailTemplate mailtemplate);

    /**
     * メールテンプレートを更新します。
     *
     * @param mailTemplate
     * @return
     */
    @Update
    int update(MailTemplate mailTemplate);

    /**
     * メールテンプレートを論理削除します。
     *
     * @param mailTemplate
     * @return
     */
    @Update(excludeNull = true)
    // NULLの項目は更新対象にしない
    int delete(MailTemplate mailTemplate);

    /**
     * メールテンプレートを一括登録します。
     *
     * @param mailTemplates
     * @return
     */
    @BatchInsert
    int[] insert(List<MailTemplate> mailTemplates);

    /**
     * メールテンプレートを一括更新します。
     *
     * @param mailtemplates
     * @return
     */
    @BatchUpdate
    int[] update(List<MailTemplate> mailtemplates);
}
