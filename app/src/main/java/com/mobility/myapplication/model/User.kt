package com.mobility.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


/**
 *
 * Created By J7202687 On 10/22/2019
 */

@Entity (tableName = "User_table")
class User {

    @ColumnInfo(name = "login_column")
    @SerializedName("login")
    val login: String? = null

    @ColumnInfo(name = "avatar_url_column")
    @SerializedName("avatar_url")
    val avatarUrl: String? = null

    @ColumnInfo(name = "type_column")
    @SerializedName("type")
    val type: String? = null

}