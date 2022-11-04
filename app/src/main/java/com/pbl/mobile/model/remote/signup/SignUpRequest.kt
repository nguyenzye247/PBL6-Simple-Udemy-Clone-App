package com.pbl.mobile.model.remote.signup

data class SignUpRequest(
    var email: String,

    var password: String,

    var fullName: String,
)