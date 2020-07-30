package com.example.demo.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.AbstractView;

import lombok.val;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class PdfView extends AbstractView {

	protected String report;

	protected Collection<?> data;

	protected String filename;

	/*
	 * コンストラクタ
	 * 
	 * @param report
	 * 
	 * @param data
	 * 
	 * @param filename
	 */
	public PdfView(String report, Collection<?> data, String filename) {
		super();
		this.setContentType("application/pdf");
		this.report = report;
		this.data = data;
		this.filename = filename;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// IEの場合はContent-Lengthヘッダが指定されてないうとダウンロードが失敗するので
		// サイズを取得するための一時的なバイト配列ストリームにコンテンツを書き出すようにする
		val baos = createTemporaryOutputStream();

		// 帳票レイアウト
		val report = loadReport();

		// データの設定
		val dataSource = new JRBeanCollectionDataSource(this.data);
		val print = JasperFillManager.fillReport(report, model, dataSource);

		val exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
		exporter.exportReport();

		/*
		 * Yoshino fixed 改修
		 * 文字化け対策 URLEncoderを使用したモジュールは使わずStandardCharsetsで対応
		 */		  
		byte[] encodedFilename = filename.getBytes(StandardCharsets.UTF_8);
		val contentDisposition = String.format("attachment; filename*=UTF-8''%s",encodedFilename);
		response.setHeader("CONTENT_DISPOTION","contentDisposition");

		// Content-TypeとContent-Lengthヘッダを設定した後にレスポンスを書き出す
		writeToResponse(response, baos);
	}

	/*
	 * 帳票レイアウトを読み込む
	 * 
	 * @return
	 */
	protected final JasperReport loadReport() {
		val resource = new ClassPathResource(this.report);

		try {
			val filename = resource.getFilename();
			if (filename.endsWith(".jasper")) {
				try (val is = resource.getInputStream()) {
					return (JasperReport) JRLoader.loadObject(is);
				}
			} else if (filename.endsWith(".jrxml")) {
				try (val is = resource.getInputStream()) {
					JasperDesign design = JRXmlLoader.load(is);
					return JasperCompileManager.compileReport(design);
				}
			} else {
				throw new IllegalArgumentException(".jasperか.jrxml形式の帳票を指定してください");
			}

		} catch (IOException e) {
			throw new IllegalArgumentException("failed to load report." + resource, e);
		} catch (JRException e) {
			throw new IllegalArgumentException("failed to parse report." + resource, e);
		}

	}

}
