package com.pbl.mobile.util

import androidx.core.text.HtmlCompat

object HtmlUtils {
    fun toHtmlText(text: String) = "<p>$text</p>"

    fun fromHtmlText(htmlText: String) =
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()

    fun removeHtmlHyphen(text: String):String{
        val result = text
            .replace("\n", "")
            .replace("\\t", "")
        return result.substring(1, result.length - 1)
    }
}
