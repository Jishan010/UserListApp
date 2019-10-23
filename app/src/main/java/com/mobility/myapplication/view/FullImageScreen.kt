package com.mobility.myapplication.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mobility.myapplication.R
import com.mobility.myapplication.view.MainActivity.Companion.AVATAR_URL_USER
import com.mobility.myapplication.view.MainActivity.Companion.ID
import com.mobility.myapplication.view.MainActivity.Companion.LOGIN_USER
import com.mobility.myapplication.view.MainActivity.Companion.TYPE_USER
import kotlinx.android.synthetic.main.activity_full_image_screen.*

class FullImageScreen : AppCompatActivity() {
    private var bundle: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image_screen)
        bundle = intent.extras
        val imageUrl = bundle?.getString(AVATAR_URL_USER)
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(this).load(imageUrl).apply(requestOptions)
            .into(full_screen_image)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(ID, bundle?.getInt(ID))
                intent.putExtra(LOGIN_USER, bundle?.getString(LOGIN_USER))
                intent.putExtra(TYPE_USER, bundle?.getString(TYPE_USER))
                intent.putExtra(AVATAR_URL_USER, bundle?.getString(AVATAR_URL_USER))
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
