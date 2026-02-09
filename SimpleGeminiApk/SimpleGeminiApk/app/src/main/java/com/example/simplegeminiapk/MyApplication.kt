package com.example.simplegeminiapk

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


class MyApplication : Application(), LifecycleObserver {
     fun onCreate(context: Context) {
        super.onCreate()
        //ProcessLifecycleOwner.get().getLifecycle().addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Log.d("MyApp", "App in background")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("MyApp", "App in foreground")
    }
}