package com.example.demo.dto;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "upload_files")
@Entity
@Getter
@Setter
public class UploadFile extends DomaDtoImpl implements MultipartFileConvertible {

    private static final long serialVersionUID = 1738092593334285554L;

    @OriginalStates // 差分UPDATEのために定義する
    UploadFile originalStates;

    @Id
    @Column(name = "upload_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // ファイル名
    @Column(name = "file_name")
    String filename;

    // オリジナルファイル名
    @Column(name = "original_file_name")
    String originalFilename;

    // コンテンツタイプ
    String contentType;

    // コンテンツ
    BZip2Data content;
}
