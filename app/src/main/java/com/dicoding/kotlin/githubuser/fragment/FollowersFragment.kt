package com.dicoding.kotlin.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kotlin.githubuser.DetailUser
import com.dicoding.kotlin.githubuser.MainViewModel
import com.dicoding.kotlin.githubuser.MainViewModelFactory
import com.dicoding.kotlin.githubuser.R
import com.dicoding.kotlin.githubuser.adapter.ListUserAdapter
import com.dicoding.kotlin.githubuser.data.ListUsers
import com.dicoding.kotlin.githubuser.model.ListUsersData
import com.dicoding.kotlin.githubuser.repository.Repository
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {

    private var list = ArrayList<ListUsers>()
    private lateinit var viewModel: MainViewModel

    var username : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        username = arguments?.getString(ARG_USERNAME)
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(activity!!.viewModelStore, viewModelFactory).get(MainViewModel::class.java)
        username?.let { viewModel.getFollowersList(it) }
        viewModel.myResponseFollowersList.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                response.body()?.let {
                    setData(it)
                    showRecyclerList()
                }
            }
        })
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
        rv_followers.layoutManager = LinearLayoutManager(activity)
        val listFollowersAdapter = ListUserAdapter(list)
        rv_followers.adapter = listFollowersAdapter

        listFollowersAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListUsers) {
                val moveToDetailUser = Intent(activity, DetailUser::class.java)
                moveToDetailUser.putExtra(DetailUser.EXTRA_USER, data)
                startActivity(moveToDetailUser)
            }
        })
    }


    companion object {

        private val ARG_USERNAME = "username"

        fun newInstance(username: String) : FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}