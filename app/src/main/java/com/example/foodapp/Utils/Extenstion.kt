package com.example.foodapp.Utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidPassword(): Boolean{
    val PASSWORD_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}"
    )
    return !TextUtils.isEmpty(this.toString()) && PASSWORD_PATTERN.matcher(this.toString()).matches()
}