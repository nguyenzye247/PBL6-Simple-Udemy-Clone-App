package com.pbl.mobile.ui.watchlecture.comment

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.pbl.mobile.R
import com.pbl.mobile.databinding.ItemCommentBinding
import com.pbl.mobile.model.local.Comment
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import com.pbl.mobile.util.DateFormatUtils

class LectureCommentAdapter :
    PagingDataAdapter<Comment, LectureCommentAdapter.CommentItemViewHolder>(COMMENT_COMPARATOR) {
    private var users: ArrayList<GetSimpleUserResponse.User>? = null

    companion object {
        private val COMMENT_COMPARATOR = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(
                oldItem: Comment,
                newItem: Comment
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Comment,
                newItem: Comment
            ) = oldItem == newItem
        }
    }

    inner class CommentItemViewHolder(val binding: ItemCommentBinding) : ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(comment: Comment?) {
            comment?.let { comment ->
                binding.apply {
                    users?.let {
                        it.forEach { instructor ->
                            if (instructor.userId == comment.userId) {
                                tvUserFullName.text = instructor.fullName
                                Glide.with(root.context)
                                    .load(instructor.avatarUrl)
                                    .placeholder(R.drawable.avatar_holder_person)
                                    .into(ivUserAvatar)
                            }
                        }
                    }
                    tvCommentContent.text = comment.content
                    tvCommentCreatedAt.text = DateFormatUtils.parseCommentDate(comment.createdAt)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemViewHolder {
        return CommentItemViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setUser(users: ArrayList<GetSimpleUserResponse.User>) {
        this.users = users
        notifyDataSetChanged()
    }
}
