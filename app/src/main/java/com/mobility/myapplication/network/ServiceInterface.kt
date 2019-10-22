package com.mobility.myapplication.network

import com.mobility.myapplication.model.User
import retrofit2.Call
import retrofit2.http.GET

/**
 *
 * Created By J7202687 On 10/22/2019
 */

interface ServiceInterface {

    @GET("users")
    fun getUserList() : Call<List<User>>
}