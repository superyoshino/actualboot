package com.example.demo.view;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import lombok.val;

public class ExcelView extends AbstractXlsxView {

	public interface Callback {
		/*
		 * Excelブックを構築する
		 * 
		 * @param model
		 * @param data
		 * @param workbook
		 */
		void buildExcelWorkbook(Map<String,Object> model ,Collection<?> data, Workbook workbook);
	}

	protected String filename;
	
	protected Collection<?> data;

	protected Callback callback;

	protected List<String> columns;

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	/*
	 * コンストラクタ
	 * 
	 * Yoshino fixed
	 * わざわざコンストラクタを2つに分けている意味がわからなかったので統一しました。
	 * 
	 * @return
	 */
	public ExcelView(Callback callback, Collection<?> data, String filename) {
		this.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=Windows-31J;");
		this.filename = filename;
		this.data = data;
		this.callback = callback;
	}

	@Override
	protected final void buildExcelDocument(Map<String, Object> model, Workbook workbook,HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		/*
		 * Yoshino fixed 改修
		 * 文字化け対策 URLEncoderを使用したモジュールは使わずStandardCharsetsで対応
		 */		  
		  byte[] encodedFilename = filename.getBytes(StandardCharsets.UTF_8);
		  val contentDisposition = String.format("attachment; filename*=UTF-8''%s",encodedFilename);
		  response.setHeader("CONTENT_TYPE",getContentType());
		  response.setHeader("Content-Disposition","contentDisposition");
		 

		// Excelブックを構築する
		callback.buildExcelWorkbook(model ,this.data,workbook);
		
		
	}

}
