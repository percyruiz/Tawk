package com.percivalruiz.tawk.ui.user_list

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.percivalruiz.tawk.R
import com.percivalruiz.tawk.data.Note
import com.percivalruiz.tawk.data.User

class UserViewHolder(
    view: View, private
    val glide: RequestManager,
    val onClick: (id: Long, login: String) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val login: TextView = view.findViewById(R.id.login)
    private val typeInfo: TextView = view.findViewById(R.id.type_info)
    private val avatar: ImageView = view.findViewById(R.id.avatar)
    private val note: ImageView = view.findViewById(R.id.note)
    private var user: User? = null
    //private val isInverted = false

    init {
        view.setOnClickListener {
            onClick(user?.id ?: 0, user?.login.orEmpty())
        }
    }

    fun bind(user: User?) {
        user?.apply {
            this@UserViewHolder.user = this
            this@UserViewHolder.login.text = login
            this@UserViewHolder.typeInfo.text = type
            glide.load(avatarURL)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(avatar)

            // Invert color of avatars
            if ((bindingAdapterPosition + 1) % 4 == 0) {
                val matrix = floatArrayOf(
                    -1f, 0f, 0f, 0f, 255f,
                    0f, -1f, 0f, 0f, 255f,
                    0f, 0f, -1f, 0f, 255f,
                    0f, 0f, 0f, 1f, 0f
                )
                val colorMatrixColorFilter = ColorMatrixColorFilter(matrix)
                avatar.colorFilter = colorMatrixColorFilter
            } else {
                avatar.clearColorFilter()
                avatar.invalidate()
            }

            if (note != null) {
                this@UserViewHolder.note.visibility = View.VISIBLE
            } else {
                this@UserViewHolder.note.visibility = View.GONE
            }
        }
    }

    fun updateNote(item: User?) {
        user = item
        user?.note = item?.note
    }

    companion object {
        fun create(
            parent: ViewGroup,
            glide: RequestManager,
            onClick: (id: Long, login: String) -> Unit
        ): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)
            return UserViewHolder(view, glide, onClick)
        }
    }
}