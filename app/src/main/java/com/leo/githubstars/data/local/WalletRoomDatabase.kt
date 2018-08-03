package com.leo.githubstars.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [
    UserData::class
]
        , version = 1)

abstract class WalletRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}
