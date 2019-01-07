package com.chen.interview

import android.app.Application
import com.chen.interview.blockcanary.AppBlockContext
import com.github.moduth.blockcanary.BlockCanary

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // block canary
        BlockCanary.install(this, AppBlockContext()).start()
    }
}