package com.dicoding.kotlin.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_users.setHasFixedSize(true)

        list.addAll(getListUser())
        showRecyclerList()
    }

    private fun getListUser(): ArrayList<User> {
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)
        val dataName = resources.getStringArray(R.array.name)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataFollowing = resources.getStringArray(R.array.following)
        val dataFollower = resources.getStringArray(R.array.followers)
        val dataUsername = resources.getStringArray(R.array.username)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataLocation = resources.getStringArray(R.array.location)

        val listUser = ArrayList<User>()
        for (position in dataName.indices) {
            val user = User(
                avatar = dataAvatar.getResourceId(position, -1),
                name = dataName[position],
                repository = dataRepository[position].toInt(),
                following = dataFollowing[position].toInt(),
                follower = dataFollower[position].toInt(),
                username = dataUsername[position],
                company = dataCompany[position],
                location = dataLocation[position]
            )
            listUser.add(user)
        }
        return listUser
    }

    private fun showRecyclerList() {
        rv_users.layoutManager = LinearLayoutManager(this)
        val userAdapter = UserAdapter(list)
        rv_users.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                val moveToDetailUser = Intent(this@MainActivity, DetailUser::class.java)
                moveToDetailUser.putExtra(DetailUser.EXTRA_USER, data)
                startActivity(moveToDetailUser)
            }
        })
    }

}