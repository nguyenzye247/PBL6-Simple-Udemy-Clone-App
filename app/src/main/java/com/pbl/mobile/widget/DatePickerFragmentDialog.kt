package com.pbl.mobile.widget

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragmentDialog(
    private val onDateSetCallback: (date: String) -> Unit,
    private val onDateSetListener: DatePickerDialog.OnDateSetListener
) : DialogFragment()
//    , DatePickerDialog.OnDateSetListener
{

    companion object {
        const val TAG = "DatePickerFragmentDialog_TAG"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), onDateSetListener, year, month, day)

    }
//
//    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
//        // Do something with the date chosen by the user
//        val dateText = "$year-${month + 1}-$day"
//        onDateSetCallback.invoke(dateText)
//    }
}
