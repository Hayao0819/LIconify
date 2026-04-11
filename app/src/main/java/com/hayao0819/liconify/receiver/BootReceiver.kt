package com.hayao0819.liconify.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hayao0819.liconify.data.common.Const.ACTION_BOOT_COMPLETED
import com.hayao0819.liconify.data.common.Const.SYSTEMUI_PACKAGE
import com.hayao0819.liconify.services.UpdateScheduler
import com.hayao0819.liconify.services.WeatherScheduler

class BootReceiver : BroadcastReceiver() {

    private val tag = this::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.i(tag, "Broadcast received: " + intent.action)

            // Schedule updates
            UpdateScheduler.scheduleUpdates(context)

            // Schedule Weather Updates
            WeatherScheduler.scheduleUpdates(context)

            // Update QS clock on boot
            val broadcast: Intent = Intent(ACTION_BOOT_COMPLETED)
            broadcast.putExtra("packageName", SYSTEMUI_PACKAGE)
            context.sendBroadcast(broadcast)
        }
    }
}