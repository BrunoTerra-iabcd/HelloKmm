package com.abcd.hellokmm.android

import android.app.Application
import com.abcd.hellokmm.L
import com.abcd.hellokmm.localizationContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        localizationContext = this
    }
}