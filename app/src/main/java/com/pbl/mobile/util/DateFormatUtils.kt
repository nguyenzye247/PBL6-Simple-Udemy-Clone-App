package com.pbl.mobile.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

object DateFormatUtils {

    fun parseDate(
        inputDateString: String?,
        inputDateFormat: SimpleDateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        )
    ): String? {
        val prettyTime = PrettyTime(Locale.getDefault())
        var date: Date?
        var outputDateString: String? = null
        try {
            date = inputDateString?.let { inputDateFormat.parse(it) }
            outputDateString = date?.let { prettyTime.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateString
    }

    @SuppressLint("NewApi")
    fun parseTimeZoneDate(
        inputDateString: String?
    ): String? {
        val prettyTime = PrettyTime(Locale.getDefault())
        var date: Date?
        var outputDateString: String? = null
        try {
            date = inputDateString?.let { Date(ZonedDateTime.parse(it).toInstant().toEpochMilli()) }
            outputDateString = date?.let { prettyTime.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateString
    }

    @SuppressLint("NewApi")
    fun getDateFromTimeZoneDate(
        inputDateString: String?
    ): String? {
        var date: Date?
        var outputDateFormat = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        )
        var outputDateString: String? = null
        try {
            date = inputDateString?.let { Date(ZonedDateTime.parse(it).toInstant().toEpochMilli()) }
            outputDateString = date?.let { outputDateFormat.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDateString
    }

    fun getDayAndMonthFrom(
        dateString: String,
        inputDateFormat: SimpleDateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        ),
        outputDateFormat: SimpleDateFormat = SimpleDateFormat("MMM-dd", Locale.US)
    ): String? {
        var date: Date?
        var outputDayAndMonthString: String? = null
        try {
            date = inputDateFormat.parse(dateString)
            outputDayAndMonthString = date?.let { outputDateFormat.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputDayAndMonthString
    }

    fun getYearFrom(
        dateString: String,
        inputDateFormat: SimpleDateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        ),
        outputDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy", Locale.US)
    ): String? {
        var date: Date?
        var outputYearString: String? = null
        try {
            date = inputDateFormat.parse(dateString)
            outputYearString = date?.let { outputDateFormat.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return outputYearString
    }
}
