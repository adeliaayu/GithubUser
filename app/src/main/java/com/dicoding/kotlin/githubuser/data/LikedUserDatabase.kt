package com.dicoding.kotlin.githubuser.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.kotlin.githubuser.model.LikedUser

@Database(entities = [LikedUser::class], version = 1, exportSchema = false)
abstract class LikedUserDatabase : RoomDatabase() {

    abstract fun likedUserDao(): LikedUserDao

    companion object {
        @Volatile
        private var INSTANCE: LikedUserDatabase? = null

        fun getDatabase(context: Context): LikedUserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LikedUserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
