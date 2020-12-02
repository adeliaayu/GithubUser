package com.dicoding.kotlin.githubuser

import android.content.Intent
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
        viewModel.getListUsersData()
        viewModel.myResponseListUsersData.observe(this, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    setData(it)
                    showRecyclerList()
                    main_progressBar.visibility = View.INVISIBLE
                    rv_users.visibility = View.VISIBLE
                }
            }
        })

        main_searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                main_progressBar.visibility = View.VISIBLE
                rv_users.visibility = View.INVISIBLE
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

}