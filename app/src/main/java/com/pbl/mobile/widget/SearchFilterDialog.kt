package com.pbl.mobile.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pbl.mobile.R
import com.pbl.mobile.common.EMPTY_SPACE
import com.pbl.mobile.databinding.DialogSearchFilterBinding
import com.pbl.mobile.model.dto.CategoryTagItem
import com.pbl.mobile.model.dto.FilterData
import com.pbl.mobile.ui.search.filter.CategoryItemAdapter
import com.pbl.mobile.util.enforceSingleScrollDirection
import java.text.NumberFormat
import java.util.*

class SearchFilterDialog(
    private val onFilterApplyListener: (filterData: FilterData) -> Unit,
    private val categories: ArrayList<CategoryTagItem>,
    private val tags: ArrayList<CategoryTagItem>,
) : DialogFragment() {
    private lateinit var categoryAdapter: CategoryItemAdapter
    private lateinit var tagAdapter: CategoryItemAdapter
    private val selectedCategories: MutableSet<CategoryTagItem> = mutableSetOf()
    private val selectedTags: MutableSet<CategoryTagItem> = mutableSetOf()
    private val filterData: FilterData = FilterData(
        0,
        500000L,
        selectedCategories,
        selectedTags
    )

    companion object {
        const val TAG = "FILTER_SEARCH_DIALOG"
        fun newInstance(
            onFilterApplyListener: (filterData: FilterData) -> Unit,
            categories: ArrayList<CategoryTagItem>,
            tags: ArrayList<CategoryTagItem>
        ) =
            SearchFilterDialog(onFilterApplyListener, categories, tags)
    }

    private val binding by lazy { DialogSearchFilterBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
        }
        view?.let { view ->
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(16, 50, 16, 50)
            view.layoutParams = params
        }
        binding.apply {
            val minPriceText = rangePrice.values[0].toLong()
                .toString() + EMPTY_SPACE + getString(R.string.vnd)
            val maxPriceText = rangePrice.values[1].toLong()
                .toString() + EMPTY_SPACE + getString(R.string.vnd)
            tvMinPrice.text = minPriceText
            tvMaxPrice.text = maxPriceText
            rvCategories.apply {
                categoryAdapter = CategoryItemAdapter(
                    categories,
                    onItemSelected = { categoryTagItem ->
                        if (categoryTagItem.isSelected)
                            selectedCategories.add(categoryTagItem)
                        else {
                            selectedCategories.remove(categoryTagItem)
                        }
                    }
                )
                enforceSingleScrollDirection()
                adapter = categoryAdapter
                layoutManager = StaggeredGridLayoutManager(
                    3,
                    StaggeredGridLayoutManager.VERTICAL
                )
            }
            rvTags.apply {
                tagAdapter = CategoryItemAdapter(
                    tags,
                    onItemSelected = { categoryTagItem ->
                        if (categoryTagItem.isSelected)
                            selectedTags.add(categoryTagItem)
                        else {
                            selectedTags.remove(categoryTagItem)
                        }
                    }
                )
                enforceSingleScrollDirection()
                adapter = tagAdapter
                layoutManager = StaggeredGridLayoutManager(
                    3,
                    StaggeredGridLayoutManager.VERTICAL
                )
            }
        }
    }

    fun closeDialog() {
        this.dismiss()
    }

    private fun initListeners() {
        binding.apply {
            btnApplyFilter.setOnClickListener {
                onFilterApplyListener.invoke(filterData)
                closeDialog()
            }
            tvPriceConfirm.setOnClickListener {
                val minPrice = rangePrice.values[0].toLong()
                val maxPrice = rangePrice.values[1].toLong()
                filterData.minPrice = minPrice
                filterData.maxPrice = maxPrice
            }
            rangePrice.addOnChangeListener { _, _, _ ->
                // Responds to when slider's value is changed
                binding.apply {
                    val minPrice = rangePrice.values[0].toLong()
                    val maxPrice = rangePrice.values[1].toLong()
                    filterData.minPrice = minPrice
                    filterData.maxPrice = maxPrice

                    val minPriceText = minPrice.toString() + EMPTY_SPACE + getString(R.string.vnd)
                    val maxPriceText = maxPrice.toString() + EMPTY_SPACE + getString(R.string.vnd)
                    tvMinPrice.text = minPriceText
                    tvMaxPrice.text = maxPriceText
                }
            }
            rangePrice.setLabelFormatter { value: Float ->
                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("VND")
                format.format(value.toDouble())
            }
        }
    }

}
