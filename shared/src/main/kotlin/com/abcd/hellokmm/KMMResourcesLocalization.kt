package com.abcd.hellokmm

import com.abcd.hellokmm.R
import android.content.Context

var localizationContext: Context? = null

actual fun L.General.Home.title(): String = localizationContext?.getString(R.string.l_general_home_title) ?: "Ola Mundo!"
