package com.percivalruiz.tawk.ui.user_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.percivalruiz.tawk.R
import com.percivalruiz.tawk.data.User

class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val login: TextView = view.findViewById(R.id.login)

    private var user : User? = null

    fun bind(user: User?) {
        user?.apply {
            this@UserViewHolder.user = this
            this@UserViewHolder.login.text = login
        }
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)
            return UserViewHolder(view)
        }
    }
}