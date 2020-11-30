package com.dicoding.kotlin.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.kotlin.githubuser.data.ListUsers
import com.dicoding.kotlin.githubuser.data.User
import com.dicoding.kotlin.githubuser.repository.Repository
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUser : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val userDetails = intent.getParcelableExtra<User>(EXTRA_USER) as ListUsers

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getUserDetailsData(userDetails.username)
        viewModel.myResponseUserDetailsData?.observe(this, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    Glide.with(this).load(response.body()?.avatar).into(detailUser_img_avatar)
                    detailUser_txt_name.text = response.body()?.name
                    detailUser_txt_username.text = response.body()?.username
                    detailUser_txt_company.text = response.body()?.company
                    detailUser_txt_location.text = response.body()?.location
                    detailUser_txt_repository.text = response.body()?.repository.toString()
                    detailUser_txt_follower.text = response.body()?.followers.toString()
                    detailUser_txt_following.text = response.body()?.following.toString()
                }
                setVisible()
            } else {
                detailUser_progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
            }
        })

        supportActionBar?.setTitle("User Detail")
    }

    private fun setVisible() {
        detailUser_progressBar.visibility = View.INVISIBLE
        detailUser_img_avatar.visibility = View.VISIBLE
        detailUser_txt_name.visibility = View.VISIBLE
        detailUser_txt_username.visibility = View.VISIBLE
        detailUser_txt_company.visibility = View.VISIBLE
        detailUser_txt_location.visibility = View.VISIBLE
        detailUser_ll_repository.visibility = View.VISIBLE
        detailUser_ll_follow.visibility = View.VISIBLE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}
