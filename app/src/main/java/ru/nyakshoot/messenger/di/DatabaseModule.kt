package ru.nyakshoot.messenger.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nyakshoot.messenger.data.chat.local.MessageDao
import ru.nyakshoot.messenger.data.core.db.MessengerDb
import ru.nyakshoot.messenger.data.chats.local.chats.ChatsDao
import ru.nyakshoot.messenger.data.chats.local.user.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMessengerDb(
        @ApplicationContext appContext: Context
    ): MessengerDb = MessengerDb.create(appContext)

    @Provides
    fun provideChatsDao(messengerDb: MessengerDb): ChatsDao = messengerDb.chatsDao()

    @Provides
    fun provideMessageDao(messengerDb: MessengerDb): MessageDao = messengerDb.messageDao()

    @Provides
    fun provideUserDao(messengerDb: MessengerDb): UserDao = messengerDb.userDao()
}