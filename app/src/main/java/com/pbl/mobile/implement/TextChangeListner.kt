package com.pbl.mobile.implement

import android.text.TextWatcher

interface TextChangeListner : TextWatcher {
    override fun beforeTextChanged(var1: CharSequence?, var2: Int, var3: Int, var4: Int) {}
    override fun onTextChanged(var1: CharSequence?, var2: Int, var3: Int, var4: Int) {}
}