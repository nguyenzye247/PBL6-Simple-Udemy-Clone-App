package com.pbl.mobile.model.dto

data class FilterData(
    var minPrice: Long,
    var maxPrice: Long,
    val categories: Set<CategoryTagItem>,
    val tags: Set<CategoryTagItem>
)
