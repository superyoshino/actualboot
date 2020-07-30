package com.example.demo.dto;


/**
 * MultipartFileインターフェースがwebモジュールに依存しているので、 <br/>
 * 本インターフェースを介させることで循環参照にならないようにする。
 * 
 * ↑よくわからないので保留/Yoshino
 * BZip2Dataも何をやっているかわからない。
 */
public interface MultipartFileConvertible {

    void setFilename(String filename);

    void setOriginalFilename(String originalFilename);

    void setContentType(String contentType);

    void setContent(BZip2Data data);
}