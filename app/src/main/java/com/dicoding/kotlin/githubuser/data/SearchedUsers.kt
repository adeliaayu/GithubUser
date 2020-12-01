package com.dicoding.kotlin.githubuser.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchedUsers(
    val total_count : Int,
    val incomplete_results : Boolean,
    val items : List<Items>
): Parcelable
