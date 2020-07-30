package com.example.demo.users;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import com.example.demo.dto.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "user_roles")
@Entity
@Getter
@Setter
public class UserRole extends DomaDtoImpl {
    private static final long serialVersionUID = -6750983302974218054L;

    // 担当者役割ID
    @Id
    @Column(name = "user_role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    // ユーザーID
    Long userId;

    // 役割キー
    String roleKey;

    // 役割名
    String roleName;

    // 権限キー
    String permissionKey;

    // 権限名
    String permissionName;

}
