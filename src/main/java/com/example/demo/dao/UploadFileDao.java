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

import com.example.demo.dto.UploadFile;
import com.example.demo.dto.UploadFileCriteria;


@ConfigAutowireable
@Dao
public interface UploadFileDao {
	
    /**
     * アップロードファイルを取得します。
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select
    List<UploadFile> selectAll(UploadFileCriteria criteria, SelectOptions options);

    /**
     * アップロードファイルを1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    UploadFile selectById(Long id);

    /**
     * アップロードファイルを1件取得します。
     *
     * @param criteria
     * @return
     */
    @Select
    UploadFile select(UploadFileCriteria criteria);

    /**
     * アップロードファイルを登録します。
     *
     * @param uploadFile
     * @return
     */
    @Insert
    int insert(UploadFile uploadFile);

    /**
     * アップロードファイルを更新します。
     *
     * @param uploadFile
     * @return
     */
    @Update
    int update(UploadFile uploadFile);

    /**
     * アップロードファイルを物理削除します。
     *
     * @param uploadFile
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(UploadFile uploadFile);

    /**
     * アップロードファイルを一括登録します。
     *
     * @param uploadFiles
     * @return
     */
    @BatchInsert
    int[] insert(List<UploadFile> uploadFiles);

}
