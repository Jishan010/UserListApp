package com.mobility.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mobility.myapplication.model.User
import com.mobility.myapplication.model.UserRepository

/**
 *
 * Created By J7202687 On 10/22/2019
 */

open class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var userRepository: UserRepository? = null
    private var userList: LiveData<List<User>>? = null

    init {
        userRepository = UserRepository(application)
        userList = userRepository!!.users
    }

    fun getUserList(): LiveData<List<User>>? {
        return userList
    }

    fun insertUser(user: User) {
        userRepository?.insertUser(user)
    }

    fun updateUser(user: User) {
        userRepository?.updateUser(user)
    }

    fun deleteUser(user: User) {
        userRepository?.deleteUser(user)
    }
}