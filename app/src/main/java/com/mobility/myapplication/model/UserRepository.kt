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

    fun insertUser(user: User) {
        InsertAsyncTask().execute(user)
    }

    fun updateUser(user: User) {
        UpdateAsyncTask().execute(user)
    }

    fun deleteUser(user: User) {
        DeleteAsyncTask().execute(user)
    }


    //async task for inserting users
    class InsertAsyncTask : AsyncTask<User, Void, Void>() {
        override fun doInBackground(vararg users: User): Void? {
            userDao?.insertUser(users[0])
            return null
        }
    }

    //async task for deleting users
    class DeleteAsyncTask : AsyncTask<User, Void, Void>() {
        override fun doInBackground(vararg users: User): Void? {
            userDao?.deleteUser(users[0])
            return null
        }
    }

    //async task for updating users
    class UpdateAsyncTask : AsyncTask<User, Void, Void>() {
        override fun doInBackground(vararg users: User): Void? {
            userDao?.updateUser(users[0])
            return null
        }
    }

    companion object {
        var userDao: UserDao? = null
    }

}
