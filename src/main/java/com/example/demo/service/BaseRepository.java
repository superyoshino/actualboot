package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import com.example.demo.dto.Page;
import com.example.demo.dto.PageFactory;
import com.example.demo.dto.Pageable;

/**
 * ベースリポジトリクラス
 *  -----------------------------------------------------------------------------
 * (変更履歴) Yoshino
 * 2020/7/14 Autowiredによる自動注入に失敗するため暫定処置でインスタンスを生成している。
 * -----------------------------------------------------------------------------
 */

public abstract class BaseRepository {
    	@Autowired
    	protected PageFactory pageFactory;
}
