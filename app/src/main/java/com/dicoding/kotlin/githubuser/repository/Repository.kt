package com.dicoding.kotlin.githubuser.repository

import com.dicoding.kotlin.githubuser.api.RetrofitInstace
import com.dicoding.kotlin.githubuser.model.ListUsers
import com.dicoding.kotlin.githubuser.model.UserDetailsData
import retrofit2.Response

class Repository {

    suspend fun getListUsers(): Response<List<ListUsers>> {
        return RetrofitInstace.api.getListUsers()
    }

    suspend fun getUserDetailsData(name: String): Response<UserDetailsData> {
        return RetrofitInstace.api.getUserDetailsData(name)
    }

}