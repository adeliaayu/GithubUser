package com.dicoding.kotlin.githubuser.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.kotlin.githubuser.R
import com.dicoding.kotlin.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.kotlin.githubuser.data.ListUsers
import com.dicoding.kotlin.githubuser.data.User
import com.dicoding.kotlin.githubuser.model.LikedUser
import com.dicoding.kotlin.githubuser.model.UserDetailsData
import com.dicoding.kotlin.githubuser.repository.Repository
import com.dicoding.kotlin.githubuser.viewmodel.LikedUserViewModel
import com.dicoding.kotlin.githubuser.viewmodel.MainViewModel
import com.dicoding.kotlin.githubuser.viewmodel.MainViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUser : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var likedViewModel: LikedUserViewModel
    private var likedUser: UserDetailsData? = null

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val userDetails = intent.getParcelableExtra<User>(EXTRA_USER) as ListUsers

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (isInternetAvailable(this)) {
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
                    likedUser = response.body()
                    setVisible()
                } else {
                    detailUser_progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            detailUser_progressBar.visibility = View.INVISIBLE
            Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show()
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = userDetails.username
        detailUser_viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(detailUser_tabs, detailUser_viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.setTitle("User Detail")

        likedViewModel = ViewModelProvider(this ).get(LikedUserViewModel::class.java)

        var statusFav = false
        setStatusFavorite(statusFav)
        detailUser_fab.setOnClickListener {
            statusFav = !statusFav
            if (statusFav) {
                insertLikedUserToDatabase()
            } else {
                deleteLikedUserInDatabase()
            }
            setStatusFavorite(statusFav)
        }
    }

    private fun deleteLikedUserInDatabase() {
        likedUser?.let { likedViewModel.deleteUser(it.username) }

        Toast.makeText(this, "Removed From Favorite", Toast.LENGTH_SHORT).show()
    }

    private fun insertLikedUserToDatabase() {
        val insertLiked = likedUser?.let {
            LikedUser(
                0,
                it.username,
                it.avatar
            )
        }

        insertLiked?.let { likedViewModel.addUser(it) }
        Toast.makeText(this, "Favorite!", Toast.LENGTH_SHORT).show()
    }

    private fun setStatusFavorite(statusFav: Boolean) {
        if (statusFav) {
            detailUser_fab.setImageDrawable(
                ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.ic_favorite_24
                )
            )
        } else {
            detailUser_fab.setImageDrawable(
                ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.ic_favorite_border_24
                )
            );
        }
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
        detailUser_tabs.visibility = View.VISIBLE
        detailUser_viewPager.visibility = View.VISIBLE
        detailUser_fab.visibility = View.VISIBLE
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
        return result
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}
