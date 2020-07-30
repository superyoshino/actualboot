package com.example.demo.dto;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "permissions")
@Entity
@Getter
@Setter
public class Permission extends DomaDtoImpl {

    private static final long serialVersionUID = -258501373358638948L;

    // 権限ID
    @Id
    @Column(name = "permission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 権限カテゴリキー
    String categoryKey;

    // 権限キー
    String permissionKey;

    // 権限名
    String permissionName;
}
