package com.mobility.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mobility.myapplication.Constants.AVATAR_URL_USER
import com.mobility.myapplication.Constants.ID
import com.mobility.myapplication.Constants.LOGIN_USER
import com.mobility.myapplication.Constants.TYPE_USER
import com.mobility.myapplication.R
import com.mobility.myapplication.model.User
import kotlinx.android.synthetic.main.activity_full_image_screen.*
import org.greenrobot.eventbus.EventBus

class FullImageScreen : AppCompatActivity() {
    private var bundle: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image_screen)
        bundle = intent.extras
        bundle?.let {
            supportActionBar!!.title = it.getString(LOGIN_USER)
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(this).load(it.getString(AVATAR_URL_USER)).apply(requestOptions)
                .into(full_screen_image)
        }
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
                bundle?.let {

                    //commented to demonstrate eventBus lib
                 /*   intent.putExtra(ID, it.getInt(ID))
                    intent.putExtra(LOGIN_USER, it.getString(LOGIN_USER))
                    intent.putExtra(TYPE_USER, it.getString(TYPE_USER))
                    intent.putExtra(AVATAR_URL_USER, it.getString(AVATAR_URL_USER))
                    setResult(Activity.RESULT_OK, intent)*/
                    with(User())
                    {
                        id =  it.getInt(ID)
                        if (id != -1) {
                            id =  it.getInt(ID)
                            login = it.getString(LOGIN_USER)
                            type = it.getString(TYPE_USER)
                            avatarUrl = it.getString(AVATAR_URL_USER)
                            //deleting the user locally
                            EventBus.getDefault().post(this)
                        }
                    }
                    finish()
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
