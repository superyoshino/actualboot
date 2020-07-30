package com.example.demo.users;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.DARK_GREEN;
import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.WHITE;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.example.demo.model.SampleExcelModel;
import com.example.demo.view.ExcelView;

import lombok.val;


public class UserExcel implements ExcelView.Callback{
	@Override
	public void buildExcelWorkbook(Map<String, Object> model, Collection<?> data, Workbook workbook) {
		
		System.out.println(data);
		Sheet sheet = workbook.createSheet("ユーザ");
		sheet.setDefaultColumnWidth(30);
		
		Font font = workbook.createFont();
		font.setFontName("メイリオ");
		font.setBold(true);
		font.setColor(WHITE.getIndex());
		
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(DARK_GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(font);
		
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("苗字");
		header.getCell(0).setCellStyle(style);
		header.createCell(1).setCellValue("名前");
		header.getCell(1).setCellStyle(style);
		header.createCell(2).setCellValue("メールアドレス");
		header.getCell(2).setCellStyle(style);
		
		@SuppressWarnings("unchecked")
		val users = (List<User>) data;
		
		int count = 1;
        for (User user : users) {
            Row userRow = sheet.createRow(count++);
            userRow.createCell(0).setCellValue(user.getLastName());
            userRow.createCell(1).setCellValue(user.getFirstName());
            userRow.createCell(2).setCellValue(user.getEmail());
        }
		
	}

}
