package com.dicoding.kotlin.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.kotlin.githubuser.data.LikedUserDatabase
import com.dicoding.kotlin.githubuser.model.LikedUser
import com.dicoding.kotlin.githubuser.repository.LikedUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikedUserViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<LikedUser>>
    val repository: LikedUserRepository

    init {
        val likedUserDao = LikedUserDatabase.getDatabase(application).likedUserDao()
        repository = LikedUserRepository(likedUserDao)
        readAllData = repository.readAllData
    }

    fun addUser(likedUser: LikedUser){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(likedUser)
        }
    }

    fun deleteUser(likedUser: LikedUser){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(likedUser)
        }
    }

    fun deleteAllUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }

}