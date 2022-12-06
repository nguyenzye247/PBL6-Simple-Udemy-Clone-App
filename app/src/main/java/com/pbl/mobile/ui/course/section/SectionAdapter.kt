package com.pbl.mobile.ui.course.section

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pbl.mobile.databinding.ItemSectionBinding
import com.pbl.mobile.model.local.Section

class SectionAdapter(
    private val sections: ArrayList<Section>,
    private val onSectionClickCallback: (section: Section) -> Unit
): RecyclerView.Adapter<SectionAdapter.CourseSectionsViewHolder>(){

    inner class CourseSectionsViewHolder(val binding: ItemSectionBinding): ViewHolder(binding.root) {
        fun bind(section: Section) {
            binding.apply {
                val sectionNumb = (sections.indexOf(section) + 1).toString() + ": "
                tvSectionNumb.text = sectionNumb
                tvSectionTitle.text = section.name
                btnExpand.setOnClickListener {
                    onSectionClickCallback.invoke(section)
                }
                root.setOnClickListener {
                    btnExpand.performClick()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseSectionsViewHolder {
        return CourseSectionsViewHolder(
            ItemSectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CourseSectionsViewHolder, position: Int) {
        holder.bind(sections[position])
    }

    override fun getItemCount() = sections.size
}
