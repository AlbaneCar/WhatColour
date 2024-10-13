package fr.eseo.ld.android.ac.colourcloud

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WhatColourHiltApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}