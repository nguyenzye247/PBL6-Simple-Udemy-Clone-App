package com.pbl.mobile.ui.upload.section

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pbl.mobile.databinding.ItemSectionBinding

class SectionAdapter(
    private val sections: ArrayList<Any>
) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    inner class SectionViewHolder(val binding: ItemSectionBinding) : ViewHolder(binding.root) {
        fun bind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        return SectionViewHolder(
            ItemSectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {

    }

    override fun getItemCount() = sections.size
}
