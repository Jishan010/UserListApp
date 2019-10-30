package com.mobility.myapplication.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.mobility.myapplication.Constants.UPDATE
import com.mobility.myapplication.Constants.DELETE
import com.mobility.myapplication.Constants.INSERT
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
        DataBaseOperationAsyncTask(INSERT).execute(user)
    }

    fun updateUser(user: User) {
        DataBaseOperationAsyncTask(UPDATE).execute(user)
    }

    fun deleteUser(user: User) {
        DataBaseOperationAsyncTask(DELETE).execute(user)
    }


    //async task for database operations
    class DataBaseOperationAsyncTask(operationType: String) : AsyncTask<User, Void, Void>() {
        private var operationType: String? = null

        init {
            this.operationType = operationType
        }

        override fun doInBackground(vararg user: User): Void? {
            when (operationType) {
                UPDATE -> userDao?.updateUser(user[0])
                DELETE -> userDao?.deleteUser(user[0])
                INSERT -> userDao?.insertUser(user[0])
            }
            return null
        }
    }


    companion object {
        var userDao: UserDao? = null
    }

}
