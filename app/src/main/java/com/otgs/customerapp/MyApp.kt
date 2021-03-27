package com.otgs.customerapp

import android.app.Application
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class MyApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}