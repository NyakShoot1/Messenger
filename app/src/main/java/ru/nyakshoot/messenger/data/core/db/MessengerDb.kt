package ru.nyakshoot.messenger.data.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.nyakshoot.messenger.data.chat.local.MessageDao
import ru.nyakshoot.messenger.data.chat.local.MessageEntity
import ru.nyakshoot.messenger.data.chats.local.chats.ChatEntity
import ru.nyakshoot.messenger.data.chats.local.chats.ChatsDao
import ru.nyakshoot.messenger.data.chats.local.user.UserDao
import ru.nyakshoot.messenger.data.chats.local.user.UserEntity
import ru.nyakshoot.messenger.data.core.db.converters.TimestampConverter

@Database(
    entities = [
        ChatEntity::class,
        MessageEntity::class,
        UserEntity::class
    ],
    version = 1
)
@TypeConverters(
    TimestampConverter::class
)
abstract class MessengerDb : RoomDatabase() {

    abstract fun chatsDao(): ChatsDao
    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao

    companion object {

        fun create(@ApplicationContext appContext: Context): MessengerDb =
            Room.databaseBuilder(
                appContext,
                MessengerDb::class.java,
                "messenger_database"
            )
                .fallbackToDestructiveMigration()
                .build()

    }
}