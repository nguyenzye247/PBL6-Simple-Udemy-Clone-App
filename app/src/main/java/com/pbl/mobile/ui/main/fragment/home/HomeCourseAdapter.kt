package com.pbl.mobile.ui.main.fragment.home

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pbl.mobile.common.EMPTY_TEXT
import com.pbl.mobile.databinding.ItemCourseBinding
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.model.remote.user.GetSimpleUserResponse
import com.pbl.mobile.util.DateFormatUtils
import com.pbl.mobile.util.HtmlUtils.fromHtmlText
import com.pbl.mobile.util.HtmlUtils.removeHtmlHyphen

class HomeCourseAdapter(
    private val onCourseItemClickCallback: (course: Course?) -> Unit
) : PagingDataAdapter<Course, HomeCourseAdapter.CourseViewHolder>(COURSE_COMPARATOR) {
    private var instructors: ArrayList<GetSimpleUserResponse.User>? = null

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
            item?.let { course ->
                binding.apply {
                    instructors?.let {
                        it.forEach { instructor ->
                            if (instructor.userId == course.userId) {
                                tvInstructorName.text = instructor.fullName
                                Glide.with(root.context)
                                    .load(instructor.avatarUrl)
                                    .into(ivInstructorAvatar)
                            }
                        }
                    }
                    tvCourseName.text = course.name
                    tvCourseCreatedDate.text =
                        DateFormatUtils.parseDate(course.createdAt) ?: EMPTY_TEXT
                    val des = removeHtmlHyphen(fromHtmlText(course.description))
                    Log.d("OKEOKE", des)
                    tvCourseDescription.text = des
                    val price = "$" + course.price.toFloat().toInt().toString()
                    tvCoursePrice.text = price
                    Thread {
                        Handler(Looper.getMainLooper()).post {
                            Glide.with(root.context)
                                .load(course.thumbnailUrl)
                                .transform(CenterInside(), RoundedCorners(24))
                                .into(ivCourseThumbnail)
                        }
                    }.start()
                    root.setOnClickListener {
                        onCourseItemClickCallback.invoke(item)
                    }
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

    fun setInstructor(instructors: ArrayList<GetSimpleUserResponse.User>) {
        this.instructors = instructors
        notifyDataSetChanged()
    }
}
