package com.pbl.mobile.ui.main.fragment.mycourse

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

class MyPurchasedCourseAdapter(
    private val courses: ArrayList<Course>,
    private val onCourseItemClickCallback: (course: Course) -> Unit
) : RecyclerView.Adapter<MyPurchasedCourseAdapter.CourseItemViewHolder>() {
    private var instructors: ArrayList<GetSimpleUserResponse.User>? = null
    private var categories: ArrayList<Category>? = null

    inner class CourseItemViewHolder(val binding: ItemCourseBinding) : ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.apply {
                instructors?.let {
                    it.forEach { instructor ->
                        if (instructor.userId == course.userId) {
                            tvInstructorName.text = instructor.fullName
                            Glide.with(root.context)
                                .load(instructor.avatarUrl)
                                .placeholder(R.drawable.avatar_holder_person)
                                .into(ivInstructorAvatar)
                        }
                    }
                }
                categories?.let {
                    it.forEach { category ->
                        if (category.id == course.categoryTopicId) {
                            tvCourseCategory.text = category.name
                        }
                    }
                }
                tvCourseName.text = course.name
                tvCourseCreatedDate.text =
                    DateFormatUtils.parseDate(course.createdAt) ?: EMPTY_TEXT
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
                    onCourseItemClickCallback.invoke(course)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemViewHolder {
        return CourseItemViewHolder(
            ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CourseItemViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount() = courses.size


    fun setInstructor(instructors: ArrayList<GetSimpleUserResponse.User>) {
        this.instructors = instructors
        notifyDataSetChanged()
    }

    fun setCategories(categories: ArrayList<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}
