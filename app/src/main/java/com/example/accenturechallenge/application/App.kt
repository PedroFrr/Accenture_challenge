package com.example.accenturechallenge.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application which lives along the application
 * Annotated with [HiltAndroidApp] for dependency injection with Hilt
 */
@HiltAndroidApp
class App: Application() {
}