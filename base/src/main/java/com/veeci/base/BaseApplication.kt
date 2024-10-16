package com.veeci.base

import android.app.Application
import com.veeci.base.main.action.navigator.BaseNavigator
import com.veeci.base.main.viewmodel.BaseViewModel
import com.veeci.base.utils.SharePreference
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@BaseApplication)
            // Load modules
            modules(appModule())
            androidFileProperties()
        }
    }

    open fun appModule() =
        module {
            factory<BaseNavigator> { BaseNavigator() }
            factory { BaseViewModel() }
            factory<SharePreference> { SharePreference(get()) }
        }
}
