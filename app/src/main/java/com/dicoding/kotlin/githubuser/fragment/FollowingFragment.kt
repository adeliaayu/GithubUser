package com.dicoding.kotlin.githubuser.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.dicoding.kotlin.githubuser.viewmodel.MainViewModel
import com.dicoding.kotlin.githubuser.viewmodel.MainViewModelFactory
import com.dicoding.kotlin.githubuser.R
import com.dicoding.kotlin.githubuser.adapter.ListUserAdapter
import com.dicoding.kotlin.githubuser.data.ListUsers
import com.dicoding.kotlin.githubuser.model.ListUsersData
import com.dicoding.kotlin.githubuser.repository.Repository
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private var list = ArrayList<ListUsers>()
    private lateinit var viewModel: MainViewModel

    var username : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        username = arguments?.getString(ARG_USERNAME)
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(activity!!.viewModelStore, viewModelFactory).get(MainViewModel::class.java)
        if (context?.let { isInternetAvailable(it) } == true) {
            username?.let { viewModel.getFollowingList(it) }
            viewModel.myResponseFollowingList.observe(viewLifecycleOwner, Observer { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        setData(it)
                        showRecyclerList()
                    }
                }
            })
        } else {
            Toast.makeText(context, "No Connection", Toast.LENGTH_LONG).show()
        }
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
        rv_following.layoutManager = LinearLayoutManager(activity)
        val listFollowingAdapter = ListUserAdapter(list)
        rv_following.adapter = listFollowingAdapter

        listFollowingAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListUsers) {
                val moveToDetailUser = Intent(activity, DetailUser::class.java)
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

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String) : FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}