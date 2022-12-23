package com.pbl.mobile.ui.course.lecture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pbl.mobile.databinding.ItemLectureVideoBinding
import com.pbl.mobile.model.local.Lecture

class LectureAdapter(
    private val isCoursePurchased: Boolean,
    private val lectures: ArrayList<Lecture>,
    private val onLectureItemClickCallback: (lecture: Lecture) -> Unit
) : RecyclerView.Adapter<LectureAdapter.LectureItemViewHolder>() {


    inner class LectureItemViewHolder(val binding: ItemLectureVideoBinding) :
        ViewHolder(binding.root) {
        fun bind(lecture: Lecture) {
            binding.apply {
                val numb = (absoluteAdapterPosition + 1).toString()
                tvLectureNumb.text = numb
                tvLectureTitle.text = lecture.title
                if (!lecture.isLock || isCoursePurchased)
                    root.setOnClickListener {
                        onLectureItemClickCallback.invoke(lecture)
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureItemViewHolder {
        return LectureItemViewHolder(
            ItemLectureVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LectureItemViewHolder, position: Int) {
        holder.bind(lectures[position])
    }

    override fun getItemCount() = lectures.size
}
