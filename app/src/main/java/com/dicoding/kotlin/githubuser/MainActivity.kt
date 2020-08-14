package com.dicoding.kotlin.githubuser

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

    private lateinit var dataAvatar: TypedArray
    private lateinit var dataName: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataFollowing: Array<String>
    private lateinit var dataFollower: Array<String>
    private lateinit var dataUsername: Array<String>
    private lateinit var dataCompany: Array<String>
    private lateinit var dataLocation: Array<String>

    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserAdapter(this)

        main_lv_listUser.adapter = adapter

        prepare()

        addItem()

        main_lv_listUser.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
            Toast.makeText(this@MainActivity, users[position].name, Toast.LENGTH_SHORT).show()

            val user_detail = users[position]
            val moveToDetailUser = Intent(this@MainActivity, DetailUser::class.java)
            moveToDetailUser.putExtra(DetailUser.EXTRA_USER, user_detail)
            startActivity(moveToDetailUser)
        }
    }

    private fun prepare() {
        dataAvatar = resources.obtainTypedArray(R.array.avatar)
        dataName = resources.getStringArray(R.array.name)
        dataRepository = resources.getStringArray(R.array.repository)
        dataFollowing = resources.getStringArray(R.array.following)
        dataFollower = resources.getStringArray(R.array.followers)
        dataUsername = resources.getStringArray(R.array.username)
        dataCompany = resources.getStringArray(R.array.company)
        dataLocation = resources.getStringArray(R.array.location)
    }

    private fun addItem() {
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
            users.add(user)
        }
        adapter.users = users
    }
}