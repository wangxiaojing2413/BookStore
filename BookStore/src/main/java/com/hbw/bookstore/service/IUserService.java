package com.hbw.bookstore.service;

import com.hbw.bookstore.entities.UserEntity;

/**
 * 
 * @author ietm
 *
 */
public interface IUserService {

	boolean addUserEntity(UserEntity entity);

	boolean deleteUserEntity(UserEntity entity);

	UserEntity queryUserEntityByUserId(UserEntity userEntity);

}
