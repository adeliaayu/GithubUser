package com.dicoding.kotlin.githubuser

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.kotlin.githubuser.adapter.SectionsPagerAdapter
import com.dicoding.kotlin.githubuser.data.ListUsers
import com.dicoding.kotlin.githubuser.data.User
import com.dicoding.kotlin.githubuser.repository.Repository
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUser : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val userDetails = intent.getParcelableExtra<User>(EXTRA_USER) as ListUsers

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (isInternetAvailable(this)){
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
