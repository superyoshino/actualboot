package com.example.demo.view;

import static com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS;
import static com.example.demo.util.ValidateUtils.isNotEmpty;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.val;

public class CsvView extends AbstractView {

	protected static final CsvMapper csvMapper = createCsvMapper();

	protected Collection<?> data;

	protected Class<?> clazz;

	protected String filename;

	protected List<String> columns;

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	/*
	 * CSVマッパーを生成する
	 * 
	 * @return
	 */
	static CsvMapper createCsvMapper() {
		CsvMapper mapper = new CsvMapper();
		/*
		 * Yoshino fixed エラー対応
		 * CsvGenerator.Featureを追記。com.fasterxml.jackson.dataformat.csv.CsvGenerator;
		 * をインポート
		 */
		mapper.configure(ALWAYS_QUOTE_STRINGS, true);
		mapper.findAndRegisterModules();
		return mapper;
	}

	public CsvView(Class<?> clazz, Collection<?> data, String filename) {
		this.setContentType("application/octet-stream; charset=Windows-31J;");
		this.clazz = clazz;
		this.data = data;
		/*
		 * Yoshino added 教材誤り
		 * 
		 */
		this.filename = filename;
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		/*
		 * Yoshino fixed 改修
		 * 文字化け対策 URLEncoderを使用したモジュールは使わずStandardCharsetsで対応
		 */		  
		  byte[] encodedFilename = filename.getBytes(StandardCharsets.UTF_8);
		  val contentDisposition = String.format("attachment; filename*=UTF-8''%s",encodedFilename);
		  response.setHeader("CONTENT_TYPE",getContentType());
		  response.setHeader("Content-Disposition","contentDisposition");
		 

		// CSVヘッダをオブジェクトから作成する
		CsvSchema schema = csvMapper.schemaFor(clazz).withHeader();

		if (isNotEmpty(columns)) {
			System.out.println(columns);
			// カラムが指定された場合はスキーマを再構築する
			val builder = schema.rebuild().clearColumns();
			for (String column : columns) {
				builder.addColumn(column);
			}
			schema = builder.build();
		}
		
		System.out.println("CsvView.columns" +columns);
		System.out.println("CsvView.schema" + schema);

		// 書き出し
		/*
		 * Yoshino fixed 教材誤り?
		 * createTemporaryOutputStreamを削除（csvファイルにデータ出力されない）
		 * ＩＥ対策らしいが、昔ながらのFileOutputStreamでＩＥ11でも普通に動く
		 */
		val outputStream = new FileOutputStream(filename);
		try (Writer writer = new OutputStreamWriter(outputStream, "Windows-31J")) {
			csvMapper.writer(schema).writeValue(writer, data);
			System.out.println(csvMapper.writer(schema).writeValueAsString(data));
		}

	}

}
