package com.dicoding.kotlin.githubuser.api

import com.dicoding.kotlin.githubuser.model.ListUsers
import com.dicoding.kotlin.githubuser.model.UserDetailsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("/users")
    suspend fun getListUsers(): Response<List<ListUsers>>

    @GET("/users/{username}")
    suspend fun getUserDetailsData(
        @Path("username") name: String
    ): Response<UserDetailsData>
}