package com.mobility.myapplication.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 *
 * Created By J7202687 On 10/22/2019
 */

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract val noteDao: UserDao

    fun cleanUp() {
        userDataBaseInstance = null
    }

    companion object {

        @Volatile
        private var userDataBaseInstance: UserDatabase? = null

        internal fun getInstance(context: Context): UserDatabase? {
            if (userDataBaseInstance == null) {
                userDataBaseInstance =
                    Room.databaseBuilder(context, UserDatabase::class.java, "UserDatabase").build()
            }
            return userDataBaseInstance
        }
    }


}
