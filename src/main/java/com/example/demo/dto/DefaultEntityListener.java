package com.example.demo.dto;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.seasar.doma.Id;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

import com.example.demo.dao.AuditInfoHolder;
import com.example.demo.exception.DoubleSubmitErrorException;
import com.example.demo.util.ReflectionUtils;

import lombok.NoArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class DefaultEntityListener<ENTITY> implements EntityListener<ENTITY> {
	
	@Override
    public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
		log.info("DefaultEntityListener.preInsert");
		//二重送信防止チェック
		val expected = DoubleSubmitCheckTokenHolder.getExpectedToken();
		val actual = DoubleSubmitCheckTokenHolder.getActualToken();
		
		if(expected != null && actual  != null && Objects.equals(expected, actual)) {
			throw new DoubleSubmitErrorException();
		}
		
		if(entity instanceof DomaDto) {
			
	        // 監査情報取得設定仮置き
			
			//現在時刻取得
	        val now = LocalDateTime.now();

	        // 監査情報仮設定
	        AuditInfoHolder.set("GUEST", now);
	       
	        //監査情報取得設定仮置きここまで
	        
			val domaDto = (DomaDto) entity;
			val createdAt = AuditInfoHolder.getAuditDateTime();
			val createdBy = AuditInfoHolder.getAuditUser();
			domaDto.setCreatedAt(createdAt);
			domaDto.setCreatedBy(createdBy);
			log.info("createdAt"+createdAt);
			log.info("createdBy"+createdBy);
		}
		
		
		
    }
    
	@Override
    public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {
		if(entity instanceof DomaDto) {
			
	        // 監査情報取得設定仮置き
			
			//現在時刻取得
	        val now = LocalDateTime.now();

	        // 監査情報仮設定
	        AuditInfoHolder.set("GUEST", now);
	       
	        //監査情報取得設定仮置きここまで
						
			val domaDto = (DomaDto) entity;
			val updatedAt = AuditInfoHolder.getAuditDateTime();
			val updatedBy = AuditInfoHolder.getAuditUser();
			val methodName = context.getMethod().getName();
			if(StringUtils.startsWith("delete", methodName)) {
				domaDto.setDeletedAt(updatedAt);//削除日
				domaDto.setDeletedBy(updatedBy);
			} else {
				domaDto.setUpdatedAt(updatedAt);//更新日
				domaDto.setUpdatedBy(updatedBy);
			}
		}
    }
    
    public void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {
		if(entity instanceof DomaDto) {
			
	        // 監査情報取得設定仮置き
			
			//現在時刻取得
	        val now = LocalDateTime.now();

	        // 監査情報仮設定
	        AuditInfoHolder.set("GUEST", now);
	       
	        //監査情報取得設定仮置きここまで
			
			val domaDto = (DomaDto) entity;
			val deletedAt = AuditInfoHolder.getAuditDateTime();
			val deletedBy = AuditInfoHolder.getAuditUser();
			val name = domaDto.getClass().getName();
			val ids = getIds(domaDto);
			//物理削除した場合はログを出力する。
			log.info("データを物理削除しました。entity={}, id={}, deletedBy={}, deletedAt={}", name, ids, deletedBy, deletedAt);
		}
    }

    /**
     * Idアノテーションが付与されたフィールドの値のリストを返します。
     *
     * @param dto
     * @return
     */
	protected List<Object> getIds(Dto dto) {
		// TODO 自動生成されたメソッド・スタブ
		return ReflectionUtils.findWithAnnotation(dto.getClass(), Id.class)
				.map(f -> ReflectionUtils.getFieldValue(f, dto)).collect(toList());	
	}

	
	
}
