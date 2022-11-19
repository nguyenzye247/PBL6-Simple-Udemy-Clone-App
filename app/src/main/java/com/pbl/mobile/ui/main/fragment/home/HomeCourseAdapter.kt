package com.pbl.mobile.ui.main.fragment.home

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pbl.mobile.databinding.ItemCourseBinding
import com.pbl.mobile.model.local.Course
import com.pbl.mobile.util.HtmlUtils.fromHtmlText
import com.pbl.mobile.util.HtmlUtils.removeHtmlHyphen
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

class HomeCourseAdapter(
    private val subscription: CompositeDisposable
) :
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
                    val price = "$" + it.price.toFloat().toInt().toString()
                    tvCoursePrice.text = price
                    Thread(
                        Runnable {
                            Handler(Looper.getMainLooper()).post {
                                Glide.with(root.context)
                                    .load(it.thumbnailUrl)
                                    .transform(CenterInside(), RoundedCorners(24))
                                    .into(ivCourseThumbnail)
                            }
                        }
                    ).start()
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

//    private fun loadThumbnail(): Single<String> {
//        return Single.create{
//
//        }
//    }
}
