package com.pbl.mobile.ui.upload.lecture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pbl.mobile.databinding.ItemLectureBinding

class LectureAdapter(
    private val lectures: ArrayList<Any>
) : RecyclerView.Adapter<LectureAdapter.LectureViewHolder>() {

    inner class LectureViewHolder(val binding: ItemLectureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        return LectureViewHolder(
            ItemLectureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {

    }

    override fun getItemCount() = lectures.size
}
