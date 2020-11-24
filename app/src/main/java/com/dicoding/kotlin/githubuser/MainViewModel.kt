package com.dicoding.kotlin.githubuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kotlin.githubuser.model.ListUsers
import com.dicoding.kotlin.githubuser.model.UserDetailsData
import com.dicoding.kotlin.githubuser.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val myResponseUserDetailsData: MutableLiveData<Response<UserDetailsData>>? = MutableLiveData()
    val myResponseListUsers: MutableLiveData<Response<List<ListUsers>>> = MutableLiveData()

    fun getListUsers() {
        viewModelScope.launch{
            val responseListUsers = repository.getListUsers()
            myResponseListUsers.value = responseListUsers
        }
    }

    fun getUserDetailsData(name: String) {
        viewModelScope.launch{
            val responseUserDetailsData = repository.getUserDetailsData(name)
            myResponseUserDetailsData?.value = responseUserDetailsData
        }
    }

}