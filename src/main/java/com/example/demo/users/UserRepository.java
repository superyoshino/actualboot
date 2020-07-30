package com.example.demo.users;

import org.jfree.util.Log;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import static com.example.demo.util.DomaUtils.createSelectOptions;

import com.example.demo.dao.UploadFileDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dao.UserRoleDao;
import com.example.demo.dto.Page;
import com.example.demo.dto.PageFactory;
import com.example.demo.dto.Pageable;
import com.example.demo.dto.Permission;
import com.example.demo.dto.PermissionCriteria;
import com.example.demo.dto.UploadFileCriteria;
import com.example.demo.dto.UploadFile;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.service.BaseRepository;
import com.example.demo.util.DomaUtils;


import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * ユーザーリポジトリクラス
 *  -----------------------------------------------------------------------------
 * (変更履歴) Yoshino
 * 2020/7/14 Autowiredによる自動注入に失敗するため暫定処置でインスタンスを生成している。
 * 2020/7/20 自動注入成功。
 * -----------------------------------------------------------------------------
 */

@Repository
public class UserRepository extends BaseRepository {
	
       @Autowired
	   UserDao userDao;

       @Autowired 
       UserRoleDao userRoleDao; 

       @Autowired 
       UploadFileDao uploadFileDao; 
	
    /**
     * ユーザーを複数取得します。
     * 
     * @param criteria
     * @param pageable
     * @return
     */	
	public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
		// ページングを指定する
		System.out.println("ユーザーリポジトリのfindAll"+ criteria + "と" +  pageable + "を呼び出し");
		val options = DomaUtils.createSelectOptions(pageable).count();
		val data = userDao.selectAll(criteria, options, toList());
		return pageFactory.create(data, pageable, options.getCount());
		
	}
	
    /**
     * ユーザーを取得します。
     * 
     * @param criteria
     * @return
     */
//    public Optional<User> findOne(UserCriteria criteria) {
//        // 1件取得
//        val user = userDao.select(criteria);
//        System.out.println("ユーザーリポジトリのfindOne.user"+ user +"を呼び出し");
//
//        // 添付ファイルを取得する
//        user.ifPresent(u -> {
//            val uploadFileId = u.getUploadFileId();
//            System.out.println("ユーザーリポジトリのfindOne.uploadFileId"+ uploadFileId +"を呼び出し");
//            val uploadFile = ofNullable(uploadFileId).map(uploadFileDao::selectById);
//            uploadFile.ifPresent(u::setUploadFile);
//        });
//        
//        
//    	return user;
//    }
    
    /**
     * ユーザー取得します。
     *
     * @return
     */
    public User findById(final Long id) {
    	System.out.println("ユーザーリポジトリのfindById"+ id +"を呼び出し");
    	return userDao.selectById(id).orElseThrow(() -> new NoDataFoundException("user_id=" + id + " のデータが見つかりません。"));
    }
    
    /**
     * ユーザーを追加します。
     *
     * @param inputUser
     * @return
     */
    public User create(final User inputUser) {
        
        val uploadFile = inputUser.getUploadFile();
        System.out.println("ユーザーリポジトリのcreate.uploadFile"+ uploadFile +"を呼び出し");
        if (uploadFile != null) {
            // 添付ファイルがある場合は、登録・更新する
            uploadFileDao.insert(uploadFile);
            inputUser.setUploadFileId(uploadFile.getId());
            System.out.println("ユーザーリポジトリのcreate.uploadFile.getId()"+ uploadFile.getId() +"有り");
        }
        
    	userDao.insert(inputUser);
    	System.out.println("ユーザーリポジトリのcreate"+ inputUser.uploadFile +"を呼び出し");
    	
    	//役割権限紐付けを登録する
    	val userRole = new UserRole();
    	userRole.setUserId(inputUser.getId());
        userRole.setRoleKey("user");
        userRoleDao.insert(userRole);
    	
    	
    	return inputUser;
    	
    }
    
    /**
     * ユーザーを更新します。
     *
     * @param inputUser
     * @return
     */
    public User update(final User inputUser) {
    	if(inputUser.getUploadFile() != null) {
        val uploadFile = inputUser.getUploadFile();
        if (uploadFile.getContent() != null) {
            // 添付ファイルがある場合は、登録・更新する
            val uploadFileId = inputUser.getUploadFileId();
            System.out.println("ユーザーリポジトリのupdate.uploadFile.getContent()"+ uploadFile.getContent() );
            System.out.println("ユーザーリポジトリのupdate.uploadFileId"+ uploadFileId );
            if (uploadFileId == null) {
                uploadFileDao.insert(uploadFile);
            } else {
            	uploadFile.setId(uploadFileId);
                uploadFileDao.update(uploadFile);
            }
            
            inputUser.setUploadFileId(uploadFile.getId());            
        	}
    	}
        	//1件更新
        	int updated = userDao.update(inputUser);
        	
        	if(updated < 1) {
        		throw new NoDataFoundException("user_id=" + inputUser.getId() + " のデータが見つかりません。");
        	}
        	return inputUser;
    }
    
    /**
     * ユーザーを論理削除します。
     *
     * @return
     */
     public User delete(final Long id) {
    	 val user = userDao.selectById(id)
    	 		.orElseThrow(() ->  new NoDataFoundException("user_id=" + id + " のデータが見つかりません。"));
         // 1件削除
         int updated = userDao.delete(user);
         System.out.println("ユーザーリポジトリのdelete.user"+ user.id +"を呼び出し");
         System.out.println("ユーザーリポジトリのdelete.updated"+ updated +"を呼び出し");
         System.out.println("ユーザーリポジトリのuserDao.update(user)"+ userDao.update(user) +"を呼び出し");

         if (updated < 1) {
             throw new NoDataFoundException("user_id=" + id + " は更新できませんでした。");
         }

         return user;
    	 
     }

}