package com.dicoding.kotlin.githubuser.repository

import androidx.lifecycle.LiveData
import com.dicoding.kotlin.githubuser.data.LikedUserDao
import com.dicoding.kotlin.githubuser.model.LikedUser

class LikedUserRepository(private val likedUserDao: LikedUserDao) {

    val readAllData: LiveData<List<LikedUser>> = likedUserDao.readAllData()

    suspend fun addUser(likedUser: LikedUser){
        likedUserDao.addUser(likedUser)
    }

    suspend fun deleteUser(likedUser: LikedUser){
        likedUserDao.deleteUser(likedUser)
    }

    suspend fun deleteAllUsers(){
        likedUserDao.deleteAllUsers()
    }

}