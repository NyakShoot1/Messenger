package ru.nyakshoot.messenger.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nyakshoot.messenger.data.auth.AuthRepository
import ru.nyakshoot.messenger.data.auth.AuthRepositoryImpl
import ru.nyakshoot.messenger.data.auth.local.AuthLocalDataSource
import ru.nyakshoot.messenger.data.auth.local.AuthLocalDataSourceImpl
import ru.nyakshoot.messenger.data.auth.remote.AuthRemoteDataSource
import ru.nyakshoot.messenger.data.auth.remote.AuthRemoteDataSourceImpl
import ru.nyakshoot.messenger.domain.auth.AuthManager
import ru.nyakshoot.messenger.domain.auth.AuthManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindAuthLocalDataSource(
        impl: AuthLocalDataSourceImpl,
    ): AuthLocalDataSource

    @Binds
    fun bindAuthRemoteDataSource(
        impl: AuthRemoteDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    fun bindAuthManager(
        impl: AuthManagerImpl,
    ): AuthManager

}