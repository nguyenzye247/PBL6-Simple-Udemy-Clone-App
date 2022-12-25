package com.pbl.mobile.ui.main.fragment.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pbl.mobile.R
import com.pbl.mobile.common.EMPTY_TEXT
import com.pbl.mobile.databinding.ItemCourseBinding
import com.pbl.mobile.model.local.Category
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import com.pbl.mobile.util.DateFormatUtils

class MyCoursesAdapter(
    private val instructor: GetSimpleUserResponse.User,
    private val onCourseItemClickCallback: (course: Course) -> Unit
) : PagingDataAdapter<Course, MyCoursesAdapter.MyCourseItemViewHolder>(COURSE_COMPARATOR) {
    private var categories: ArrayList<Category>? = null

    companion object {
        private val COURSE_COMPARATOR = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(
                oldItem: Course,
                newItem: Course
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Course,
                newItem: Course
            ) = oldItem == newItem
        }
    }

    inner class MyCourseItemViewHolder(val binding: ItemCourseBinding) : ViewHolder(binding.root) {

        fun bind(item: Course?) {
            binding.apply {
                item?.let {
                    tvInstructorName.text = instructor.fullName
                    Glide.with(root.context)
                        .load(instructor.avatarUrl)
                        .placeholder(R.drawable.avatar_holder_person)
                        .into(ivInstructorAvatar)
                    categories?.let {
                        it.forEach { category ->
                            if (category.id == item.categoryTopicId) {
                                tvCourseCategory.text = category.name
                            }
                        }
                    }
                    tvCourseName.text = item.name
                    tvCourseCreatedDate.text =
                        DateFormatUtils.parseDate(item.createdAt) ?: EMPTY_TEXT
                    val price = "$" + item.price.toFloat().toInt().toString()
                    tvCoursePrice.text = price
                    Glide.with(root.context)
                        .load(item.thumbnailUrl)
                        .transform(CenterInside(), RoundedCorners(24))
                        .into(ivCourseThumbnail)
                    root.setOnClickListener {
                        onCourseItemClickCallback.invoke(item)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCourseItemViewHolder {
        return MyCourseItemViewHolder(
            ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyCourseItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setCategories(categories: ArrayList<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}
