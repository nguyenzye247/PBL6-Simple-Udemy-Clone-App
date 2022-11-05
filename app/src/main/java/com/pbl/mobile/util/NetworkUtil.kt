package com.pbl.mobile.util

import retrofit2.HttpException


object NetworkUtil {
    fun isHttpStatusCode(throwable: Throwable, statusCode: Int): Boolean {
        return (throwable is HttpException
                && throwable.code() == statusCode)
    }
}