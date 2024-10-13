package fr.eseo.ld.android.ac.colourcloud.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.eseo.ld.android.ac.colourcloud.repositories.FirebaseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase() : FirebaseRepository {
        return FirebaseRepository()
    }
}