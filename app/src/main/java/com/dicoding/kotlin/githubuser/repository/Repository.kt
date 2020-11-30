package com.dicoding.kotlin.githubuser.repository

import com.dicoding.kotlin.githubuser.api.RetrofitInstace
import com.dicoding.kotlin.githubuser.data.SearchedUsers
import com.dicoding.kotlin.githubuser.model.ListUsersData
import com.dicoding.kotlin.githubuser.model.UserDetailsData
import retrofit2.Response

class Repository {

    suspend fun getListUsersData(): Response<List<ListUsersData>> {
        return RetrofitInstace.api.getListUsersData()
    }

    suspend fun getSearchResult(username: String) : Response<SearchedUsers> {
        return RetrofitInstace.api.getSearchResult(username)
    }

    suspend fun getUserDetailsData(name: String): Response<UserDetailsData> {
        return RetrofitInstace.api.getUserDetailsData(name)
    }
}