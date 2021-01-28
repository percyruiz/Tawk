package com.percivalruiz.tawk.ui.user_list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.percivalruiz.tawk.data.User

class UserListAdapter(
    private val glide: RequestManager,
    private val onClick: (id: Long, login: String) -> Unit
) : PagingDataAdapter<User, UserViewHolder>(USER_COMP) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent, glide, onClick)
    }

    companion object {
        val USER_COMP = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id
        }
    }
}