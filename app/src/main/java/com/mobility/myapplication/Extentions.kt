package com.mobility.myapplication

import android.content.Context
import android.widget.Toast

/**
 *
 * Created By J7202687 On 10/24/2019
 */

fun Context.showMessage(message : String)
{
    Toast.makeText(this ,message,Toast.LENGTH_LONG).show()
}