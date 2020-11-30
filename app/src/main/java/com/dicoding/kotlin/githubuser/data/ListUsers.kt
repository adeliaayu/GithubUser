package com.dicoding.kotlin.githubuser.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListUsers(
    var username: String,
    var avatar: String
) : Parcelable
