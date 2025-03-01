package com.example.hw51.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharacterEntity::class], version = 4)
abstract class CharacterDataBase : RoomDatabase() {

    abstract fun charactersDao(): CharacterDao
}