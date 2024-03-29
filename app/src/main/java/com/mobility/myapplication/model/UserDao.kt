package com.mobility.myapplication.model

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 *
 * Created By J7202687 On 10/22/2019
 */
@Dao
interface UserDao {

    @Query("Select * from User_table")
    fun getUserList(): LiveData<List<User>>

    @Insert
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(user: User)

}