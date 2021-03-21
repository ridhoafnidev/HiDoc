package com.ismealdi.hidoc.utils.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ismealdi.hidoc.utils.commons.Logs

class AmTaskService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Logs.fireBase("ClearFromRecentService")
        return START_STICKY_COMPATIBILITY
    }

    override fun onLowMemory() {
        AmMessagingService().storeOnline(false)
        super.onLowMemory()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        AmMessagingService().storeOnline(false)
        stopSelf()
    }


}