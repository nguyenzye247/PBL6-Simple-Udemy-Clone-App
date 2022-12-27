package com.pbl.mobile.model.dto

data class CategoryTagItem(
    val name: String,
    val id: String,
    val isCategory: Boolean,
    var isSelected: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is CategoryTagItem) return false
        return name == other.name && id == other.id
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}
