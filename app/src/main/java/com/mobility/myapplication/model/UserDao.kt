package com.mobility.myapplication.model

import androidx.room.*

/**
 *
 * Created By J7202687 On 10/22/2019
 */
@Dao
interface UserDao {

    @Query("Select * from User_table")
    fun getUserList(): List<User>

    @Insert
    fun inserUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(user: User)

}