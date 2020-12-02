package com.dicoding.kotlin.githubuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.kotlin.githubuser.data.SearchedUsers
import com.dicoding.kotlin.githubuser.model.ListUsersData
import com.dicoding.kotlin.githubuser.model.UserDetailsData
import com.dicoding.kotlin.githubuser.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val myResponseListUsersData: MutableLiveData<Response<List<ListUsersData>>> = MutableLiveData()
    val myResponseSearchResult: MutableLiveData<Response<SearchedUsers>> = MutableLiveData()
    val myResponseUserDetailsData: MutableLiveData<Response<UserDetailsData>>? = MutableLiveData()
    val myResponseFollowersList: MutableLiveData<Response<List<ListUsersData>>> = MutableLiveData()
    val myResponseFollowingList: MutableLiveData<Response<List<ListUsersData>>> = MutableLiveData()

    fun getListUsersData() {
        viewModelScope.launch{
            val responseListUsers = repository.getListUsersData()
            myResponseListUsersData.value = responseListUsers
        }
    }

    fun getSearchResult(username: String) {
        viewModelScope.launch {
            val responseSearchResult = repository.getSearchResult(username)
            myResponseSearchResult.value = responseSearchResult
        }
    }

    fun getUserDetailsData(username: String) {
        viewModelScope.launch{
            val responseUserDetailsData = repository.getUserDetailsData(username)
            myResponseUserDetailsData?.value = responseUserDetailsData
        }
    }

    fun getFollowersList(username: String) {
        viewModelScope.launch{
            val responseFollowersList = repository.getFollowersList(username)
            myResponseFollowersList.value = responseFollowersList
        }
    }

    fun getFollowingList(username: String) {
        viewModelScope.launch{
            val responseFollowingList = repository.getFollowingList(username)
            myResponseFollowingList.value = responseFollowingList
        }
    }

}