package com.example.demo.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.example.demo.dto.Page;
import com.example.demo.dto.Pageable;
import com.example.demo.service.BaseTransactionalService;
import com.example.demo.users.User;
import com.example.demo.users.UserCriteria;

/**
 * ユーザーサービス
 */
@Service
public class UserService extends BaseTransactionalService {
	
    @Autowired
    UserRepository userRepository;
	
    /**
     * ユーザーを複数取得します。
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<User> findAll(UserCriteria criteria, Pageable pageable){
    	Assert.notNull(criteria, "criteria must not be null");
		return userRepository.findAll(criteria, pageable);
	}
    
    /**
     * ユーザーを1件取得します。
     *
     * @return
     */
//    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
//    public Optional<User> findOne(UserCriteria criteria){
//    	Assert.notNull(criteria, "criteria must not be null");
//		return userRepository.findOne(criteria);
//	}
    
    /**
     * ユーザーを1件IDから取得します。
     *
     * @return
     */
//    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
//    public Optional<User> findById(UserCriteria criteria){
//    	System.out.println("ユーザーサービスのfindByIdを呼び出し");
//    	Assert.notNull(criteria, "criteria must not be null");
//		return userRepository.findOne(criteria);
//	}
    
    /**
     * ユーザーを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public User findById(final Long id) {
    	System.out.println("ユーザーサービスのfindById"+ id +"を呼び出し");
        Assert.notNull(id, "id must not be null");
        return userRepository.findById(id);
    }
    
    /**
     * ユーザーを追加します。
     *
     * @param inputUser
     * @return
     */
    public User create(final User inputUser) {
    	Assert.notNull(inputUser, "inputUser must not be null");
    	return userRepository.create(inputUser);
    }
    
    
    /**
     * ユーザーを更新します。
     *
     * @param inputUser
     * @return
     */
    public User update(final User inputUser) {
    	Assert.notNull(inputUser, "inputUser must not be null");
    	return userRepository.update(inputUser);
    }
    
    /**
     * ユーザーを論理削除します。
     *
     * @return
     */
    public User delete(final Long id) {
    	Assert.notNull(id, "id must not be null");
    	return userRepository.delete(id);
    }

}
