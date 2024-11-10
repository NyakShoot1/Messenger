package ru.nyakshoot.messenger.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nyakshoot.messenger.data.chat.ChatRepository
import ru.nyakshoot.messenger.data.chat.ChatRepositoryImpl
import ru.nyakshoot.messenger.data.chat.local.MessageLocalDataSource
import ru.nyakshoot.messenger.data.chat.local.MessageLocalDataSourceImpl
import ru.nyakshoot.messenger.data.chat.remote.MessageRemoteDataSource
import ru.nyakshoot.messenger.data.chat.remote.MessageRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ChatModule {

    @Binds
    fun bindMessageLocalDataSource(
        impl: MessageLocalDataSourceImpl,
    ): MessageLocalDataSource

    @Binds
    fun bindMessageRemoteDataSource(
        impl: MessageRemoteDataSourceImpl,
    ): MessageRemoteDataSource

    @Binds
    @Singleton
    fun bindChatRepository(
        impl: ChatRepositoryImpl,
    ): ChatRepository

}