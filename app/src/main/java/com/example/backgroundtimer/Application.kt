package com.example.backgroundtimer

import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlin.system.exitProcess

class Application : android.app.Application(), LifecycleEventObserver {

    companion object {
        var elapsedTimePause:Long? = null
        var elapsedTime:Long? = null
        val elapsedTimeLimit = 1000*60*0.5 // 30초
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Log.e("🥶APP","ONSTATECHANGED ${event}")
        when(event){
            Lifecycle.Event.ON_PAUSE -> elapsedTimePause = SystemClock.elapsedRealtime()
            Lifecycle.Event.ON_RESUME -> {
                if(elapsedTimePause != null ) elapsedTime = SystemClock.elapsedRealtime() - elapsedTimePause!!
                Log.e("🥶APP","ON_RESULE ${elapsedTime}")

                if(elapsedTime != null && elapsedTime!! > elapsedTimeLimit ){
                    val componentName = packageManager.getLaunchIntentForPackage(packageName)!!.component
                    val intent = Intent.makeRestartActivityTask(componentName)
                    startActivity(intent)
                    exitProcess(0)
                }
            }
            else -> null
        }
    }

}