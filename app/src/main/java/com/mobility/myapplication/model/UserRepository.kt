package com.mobility.myapplication.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 *
 * Created By J7202687 On 10/22/2019
 */

class UserRepository(application: Application) {

    private var userDatabase: UserDatabase? = null
    private var userList: LiveData<List<User>>? = null

    val users: LiveData<List<User>>
        get() {
            userList = userDao?.getUserList()!!
            return userList!!
        }

    init {
        userDatabase = UserDatabase.getInstance(application)
        userDao = userDatabase!!.getNoteDao()
    }

    fun insertNote(user: User) {
        insertAsyncTask().execute(user)
    }

    fun updateNote(user: User) {
        updateAsyncTask().execute(user)
    }

    fun deleteNote(user: User) {
        deleteAsyncTask().execute(user)
    }


    //async task for inserting users
    class insertAsyncTask : AsyncTask<User, Void, Void>() {
        override fun doInBackground(vararg users: User): Void? {
            userDao?.insertUser(users[0])
            return null
        }
    }

    //async task for deleting users
    class deleteAsyncTask : AsyncTask<User, Void, Void>() {
        override fun doInBackground(vararg users: User): Void? {
            userDao?.deleteUser(users[0])
            return null
        }
    }

    //async task for updating users
    class updateAsyncTask : AsyncTask<User, Void, Void>() {
        override fun doInBackground(vararg users: User): Void? {
            userDao?.updateUser(users[0])
            return null
        }
    }

    companion object {
        var userDao: UserDao? = null
    }

}
