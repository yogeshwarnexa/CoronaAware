package com.example.coronaaware.utlies

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ContextHelper {

    @JvmStatic
    lateinit var applicationContext: Context
}

inline val appContext get() = ContextHelper.applicationContext
