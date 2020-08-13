package com.dicoding.kotlin.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {

    internal var users = arrayListOf<User>()

    override fun getItem(i: Int): Any = users[i]

    override fun getItemId(i: Int): Long = i.toLong()

    override fun getCount(): Int = users.size

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        var itemView = view
        if(itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)
        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }

    inner class ViewHolder constructor(private val view: View) {
        fun bind(user: User) {
            with(view) {
                img_avatar.setImageResource(user.avatar)
                txt_name.text = user.name
                txt_username.text = user.username
                txt_company.text = user.company
                txt_location.text = user.location
            }
        }
    }

}