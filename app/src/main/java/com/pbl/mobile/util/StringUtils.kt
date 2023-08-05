package com.pbl.mobile.util

object StringUtils {
    fun separateIdentityUrl(url: String?): Pair<String, String> {
        url?.let {
            val separator = "\\s* - \\s*"
            val strings = url.split(separator.toRegex())
            val front = strings[0]
            val back = strings[1]
            return Pair(front, back)
        }
        return Pair("", "")
    }
}
