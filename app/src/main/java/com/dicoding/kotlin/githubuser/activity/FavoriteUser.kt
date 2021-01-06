package com.dicoding.kotlin.githubuser.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kotlin.githubuser.R
import com.dicoding.kotlin.githubuser.activity.DetailUser
import com.dicoding.kotlin.githubuser.adapter.LikedUserAdapter
import com.dicoding.kotlin.githubuser.data.ListUsers
import com.dicoding.kotlin.githubuser.model.LikedUser
import com.dicoding.kotlin.githubuser.viewmodel.LikedUserViewModel
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.android.synthetic.main.activity_main.*

class FavoriteUser : AppCompatActivity() {

    private lateinit var likedViewModel: LikedUserViewModel
    private var likedUser = ArrayList<LikedUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        likedViewModel = ViewModelProvider(this ).get(LikedUserViewModel::class.java)

        likedViewModel.readAllData.observe(this, Observer { likedUserList ->
            setRecyclerview(likedUserList)
        })

        supportActionBar?.setTitle("Favorite Users")
    }

    private fun setRecyclerview(likedUserList: List<LikedUser>?) {
        favUser_rv_users.layoutManager = LinearLayoutManager(this)
        val likedAdapter = likedUserList?.let { LikedUserAdapter(it) }
        favUser_rv_users.adapter = likedAdapter

        likedAdapter?.setOnItemClickCallback(object : LikedUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListUsers) {
                val moveToDetailUser = Intent(this@FavoriteUser, DetailUser::class.java)
                moveToDetailUser.putExtra(DetailUser.EXTRA_USER, data)
                startActivity(moveToDetailUser)
            }
        })
    }
}