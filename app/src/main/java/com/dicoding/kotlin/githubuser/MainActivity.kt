package com.dicoding.kotlin.githubuser

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kotlin.githubuser.adapter.ListUserAdapter
import com.dicoding.kotlin.githubuser.data.ListUsers
import com.dicoding.kotlin.githubuser.data.SearchedUsers
import com.dicoding.kotlin.githubuser.model.ListUsersData
import com.dicoding.kotlin.githubuser.repository.Repository
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var list = ArrayList<ListUsers>()

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_users.setHasFixedSize(true)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (isInternetAvailable(this)){
            viewModel.getListUsersData()
            viewModel.myResponseListUsersData.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        setData(it)
                        showRecyclerList()
                        main_progressBar.visibility = View.INVISIBLE
                        rv_users.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(this, "Failed Connection", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            main_progressBar.visibility = View.INVISIBLE
            Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show()
        }

        main_searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                main_progressBar.visibility = View.VISIBLE
                rv_users.visibility = View.INVISIBLE
                if (isInternetAvailable(this@MainActivity)){
                    viewModel.getSearchResult(query)
                    viewModel.myResponseSearchResult.observe(this@MainActivity, Observer { response ->
                        if (response.isSuccessful) {
                            response.body()?.let {
                                list.clear()
                                setDataSearchUsers(it)
                                showRecyclerList()
                                main_progressBar.visibility = View.INVISIBLE
                                rv_users.visibility = View.VISIBLE
                                if (list.isEmpty()) Toast.makeText(this@MainActivity, "No Match Found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Connection Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
                }  else {
                    main_progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@MainActivity, "No Connection", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setDataSearchUsers(newList: SearchedUsers) {
        val listUser = ArrayList<ListUsers>()
        for (position in newList.items.indices) {
            val user = ListUsers(
                avatar = newList.items[position].avatar_url,
                username = newList.items[position].username
            )
            listUser.add(user)
        }
        list.addAll(listUser)
    }

    private fun setData(newList: List<ListUsersData>) {
        val listUser = ArrayList<ListUsers>()
        for (position in newList.indices) {
            val user = ListUsers(
                avatar = newList[position].avatar,
                username = newList[position].username
            )
            listUser.add(user)
        }
        list.addAll(listUser)
    }

    private fun showRecyclerList() {
        rv_users.layoutManager = LinearLayoutManager(this)
        val userAdapter = ListUserAdapter(list)
        rv_users.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListUsers) {
                val moveToDetailUser = Intent(this@MainActivity, DetailUser::class.java)
                moveToDetailUser.putExtra(DetailUser.EXTRA_USER, data)
                startActivity(moveToDetailUser)
            }
        })
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

}