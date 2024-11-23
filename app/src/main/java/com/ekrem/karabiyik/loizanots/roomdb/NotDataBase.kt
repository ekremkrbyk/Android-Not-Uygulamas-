package com.ekrem.karabiyik.loizanots.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ekrem.karabiyik.loizanots.model.Notlar

@Database(entities = [Notlar::class], version = 1)
abstract class NotDataBase:RoomDatabase() {
    abstract fun NotDao():NotDAO
}