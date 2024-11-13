package ru.nyakshoot.messenger.di

import cafe.adriel.voyager.hilt.ScreenModelFactory
import cafe.adriel.voyager.hilt.ScreenModelFactoryKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.nyakshoot.messenger.presentation.chat.viewmodel.ChatScreenModel
import ru.nyakshoot.messenger.presentation.chats.viewmodel.ChatsScreenModel

@Module
@InstallIn(SingletonComponent::class)
interface ScreenModelModule {

    @Binds
    @IntoMap
    @ScreenModelFactoryKey(ChatScreenModel.Factory::class)
    fun bindChatScreenModelFactory(factory: ChatScreenModel.Factory): ScreenModelFactory

    @Binds
    @IntoMap
    @ScreenModelFactoryKey(ChatsScreenModel.Factory::class)
    fun bindChatsScreenModelFactory(factory: ChatsScreenModel.Factory): ScreenModelFactory

}