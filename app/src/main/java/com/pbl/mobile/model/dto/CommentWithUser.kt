package com.pbl.mobile.model.dto

import com.pbl.mobile.model.local.Comment
import com.pbl.mobile.model.local.User

data class CommentWithUser(
    val comment: Comment,
    val user: User
)
