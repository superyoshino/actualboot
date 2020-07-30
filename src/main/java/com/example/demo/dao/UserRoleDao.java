package com.example.demo.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.example.demo.dto.Permission;
import com.example.demo.dto.PermissionCriteria;
import com.example.demo.users.UserCriteria;
import com.example.demo.users.UserRole;


/**
 *権限DAO
 * -----------------------------------------------------------------------------
 *Q:なぜ登録時に権限名や権限キーをexcludeしている…?
 */

@ConfigAutowireable
@Dao
public interface UserRoleDao {
	
    /**
     * 権限を取得します。
     *
     * @param userCriteria
     * @param permissionCriteria
     * @param options
     * @return
     */
	 @Select(strategy = SelectType.COLLECT)
	 <R> R selectAll(final UserCriteria userCriteria,final PermissionCriteria permissionCriteria,
			 final SelectOptions options,final Collector<UserRole, ? , R > collector);
	
    /**
     * 権限を取得します。
     * 
     * @param id
     * @param collector
     * @param <R>
     * @return
     */
	 @Select(strategy = SelectType.COLLECT)
	 <R> R selectByUserId(Long id, final Collector<UserRole, ? , R > collector);
	
    /**
     * 権限を1件取得します。
     *
     * @param id
     * @return
     */
	 @Select
	 Optional<Permission> selectById(Long id);
	
    /**
     * 権限を1件取得します。
     *
     * @param criteria
     * @return
     */
	 @Select
	 Optional<Permission> select(PermissionCriteria criteria);
	 
    /**
     * 権限を登録します。
     *
     * @param userRole
     * @return
     */
	 @Insert(exclude = {"roleName", "permissionKey", "permissionName" })
	 int insert(UserRole userRole);
	
    /**
     * 権限を更新します。
     *
     * @param userRole
     * @return
     */
	 @Update(exclude = {"roleName", "permissionKey", "permissionName" })
	 int update(UserRole userRole);
	
    /**
     * 権限を論理削除します。
     *
     * @param userRole
     * @return
     */
	 @Update(excludeNull = true) // NULLの項目は更新対象にしない
	 int delete(UserRole userRole);
	
    /**
     * 権限を一括登録します。
     *
     * @param userRoles
     * @return
     */
	 @BatchInsert(exclude = {"roleName", "permissionKey", "permissionName" })
	 int[] insert(List<UserRole> userRole);

}
