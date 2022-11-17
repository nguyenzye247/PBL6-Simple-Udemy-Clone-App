package com.pbl.mobile.ui.main.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pbl.mobile.databinding.ItemCourseBinding
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.util.HtmlUtils.fromHtmlText
import com.pbl.mobile.util.HtmlUtils.removeHtmlHyphen

class HomeCourseAdapter :
    PagingDataAdapter<Course, HomeCourseAdapter.CourseViewHolder>(COURSE_COMPARATOR) {
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

    inner class CourseViewHolder(private val binding: ItemCourseBinding) :
        ViewHolder(binding.root) {

        fun bind(item: Course?) {
            item?.let {
                binding.apply {
                    tvCourseName.text = it.name
                    val des = removeHtmlHyphen(fromHtmlText(it.description))
                    tvCourseDescription.text = des
                    tvCoursePrice.text = it.price
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(
            ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
