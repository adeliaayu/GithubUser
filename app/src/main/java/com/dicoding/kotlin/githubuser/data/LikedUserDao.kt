package com.dicoding.kotlin.githubuser.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.kotlin.githubuser.model.LikedUser

@Dao
interface LikedUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(likedUser: LikedUser)

    @Delete
    suspend fun deleteUser(likedUser: LikedUser)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<LikedUser>>

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

}