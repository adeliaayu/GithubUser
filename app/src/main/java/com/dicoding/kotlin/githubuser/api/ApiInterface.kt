package com.dicoding.kotlin.githubuser.api

import com.dicoding.kotlin.githubuser.data.SearchedUsers
import com.dicoding.kotlin.githubuser.model.ListUsersData
import com.dicoding.kotlin.githubuser.model.UserDetailsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/users")
    suspend fun getListUsersData(): Response<List<ListUsersData>>

    @GET("/search/users")
    suspend fun getSearchResult(
        @Query("q") q: String
    ): Response<SearchedUsers>

    @GET("/users/{username}")
    suspend fun getUserDetailsData(
        @Path("username") name: String
    ): Response<UserDetailsData>

    @GET("/users/{username}/followers")
    suspend fun getFollowersList(
        @Path("username") name: String
    ): Response<List<ListUsersData>>

    @GET("/users/{username}/following")
    suspend fun getFollowingList(
        @Path("username") name: String
    ): Response<List<ListUsersData>>

}
