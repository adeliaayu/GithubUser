package com.dicoding.kotlin.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val userDetails = intent.getParcelableExtra<User>(EXTRA_USER) as User

        detailUser_img_avatar.setImageResource(userDetails.avatar)
        detailUser_txt_name.text = userDetails.name
        detailUser_txt_username.text = userDetails.username
        detailUser_txt_company.text = userDetails.company
        detailUser_txt_location.text = userDetails.location
        detailUser_txt_repository.text = userDetails.repository.toString()
        detailUser_txt_follower.text = userDetails.follower.toString()
        detailUser_txt_following.text = userDetails.following.toString()

        supportActionBar?.setTitle("Detail User")
    }
}