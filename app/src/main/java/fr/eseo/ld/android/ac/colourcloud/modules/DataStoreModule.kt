package fr.eseo.ld.android.ac.colourcloud.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.eseo.ld.android.ac.colourcloud.model.WhatColourDataStore
import javax.inject.Singleton

@Module
@InstallIn(
    SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideWhatColourDataStore(
        @ApplicationContext context : Context
    ) : WhatColourDataStore {
        return WhatColourDataStore(context)
    }
}