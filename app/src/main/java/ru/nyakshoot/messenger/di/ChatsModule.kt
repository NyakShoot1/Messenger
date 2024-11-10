package ru.nyakshoot.messenger.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nyakshoot.messenger.data.chats.ChatsRepository
import ru.nyakshoot.messenger.data.chats.ChatsRepositoryImpl
import ru.nyakshoot.messenger.data.chats.local.chats.ChatsLocalDataSource
import ru.nyakshoot.messenger.data.chats.local.chats.ChatsLocalDataSourceImpl
import ru.nyakshoot.messenger.data.chats.local.user.UserLocalDataSource
import ru.nyakshoot.messenger.data.chats.local.user.UserLocalDataSourceImpl
import ru.nyakshoot.messenger.data.chats.remote.chats.ChatsRemoteDataSource
import ru.nyakshoot.messenger.data.chats.remote.chats.ChatsRemoteDataSourceImpl
import ru.nyakshoot.messenger.data.chats.remote.user.UserRemoteDataSource
import ru.nyakshoot.messenger.data.chats.remote.user.UserRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ChatsModule {

    @Binds
    fun bindChatsLocalDataSource(
        impl: ChatsLocalDataSourceImpl,
    ): ChatsLocalDataSource

    @Binds
    fun bindChatsRemoteDataSource(
        impl: ChatsRemoteDataSourceImpl,
    ): ChatsRemoteDataSource

    @Binds
    fun bindUserLocalDataSource(
        impl: UserLocalDataSourceImpl,
    ): UserLocalDataSource

    @Binds
    fun bindUserRemoteDataSource(
        impl: UserRemoteDataSourceImpl,
    ): UserRemoteDataSource

    @Binds
    @Singleton
    fun bindChatsRepository(
        impl: ChatsRepositoryImpl,
    ): ChatsRepository

}