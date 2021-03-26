package com.example.thealth

import android.app.Application

class myApplication : Application() {
    companion object {
        var TAG = "THealth"

        val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 0
    }
}