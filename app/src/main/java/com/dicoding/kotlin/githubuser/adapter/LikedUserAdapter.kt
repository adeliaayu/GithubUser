package com.dicoding.kotlin.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.kotlin.githubuser.R
import com.dicoding.kotlin.githubuser.data.ListUsers
import com.dicoding.kotlin.githubuser.model.LikedUser
import kotlinx.android.synthetic.main.item_user.view.*

class LikedUserAdapter(private var likedUser: List<LikedUser>) :
    RecyclerView.Adapter<LikedUserAdapter.FavoriteViewHolder>() {

    private var onItemClickCallback: LikedUserAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: LikedUserAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(likedUser[position])
    }

    override fun getItemCount(): Int = likedUser.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(likedUser: LikedUser) {
            with(itemView) {
                Glide.with(context).load(likedUser.avatar_url).into(itemUser_img_avatar)
                itemUser_txt_username.text = likedUser.username

                val user = ListUsers(
                    username = likedUser.username,
                    avatar = likedUser.avatar_url
                )
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListUsers)
    }
}