package com.mobility.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


/**
 *
 * Created By J7202687 On 10/22/2019
 */

@Entity(tableName = "User_table")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,

    @ColumnInfo(name = "login_column")
    @SerializedName("login")
    val login: String? = null,

    @ColumnInfo(name = "avatar_url_column")
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @ColumnInfo(name = "type_column")
    @SerializedName("type")
    val type: String? = null

)