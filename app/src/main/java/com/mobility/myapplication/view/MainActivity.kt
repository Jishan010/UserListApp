package com.mobility.myapplication.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobility.myapplication.Constants.AVATAR_URL_USER
import com.mobility.myapplication.Constants.ID
import com.mobility.myapplication.Constants.INSERT_REQUEST_CODE
import com.mobility.myapplication.Constants.LOGIN_USER
import com.mobility.myapplication.Constants.TYPE_USER
import com.mobility.myapplication.Constants.UPDATE_REQUEST_CODE
import com.mobility.myapplication.R
import com.mobility.myapplication.adapter.UserListAdapter
import com.mobility.myapplication.model.User
import com.mobility.myapplication.showMessage
import com.mobility.myapplication.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {

    private var userViewModel: UserViewModel? = null
    private var userList: List<User>? = null
    private var recyclerView: RecyclerView? = null
    private var userListAdapter: UserListAdapter? = null
    private var fragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity","onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initRecycleView()
        fragmentManager = supportFragmentManager
        userListAdapter = UserListAdapter()
        recyclerView!!.adapter = userListAdapter

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel?.getUserList()?.observe(this,
            Observer<List<User>> { users ->
                userList = users
                userListAdapter!!.submitList(users)
            })

        //swipe to delete functionality starts from here
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                userListAdapter?.getUsers(viewHolder.adapterPosition)?.let {
                    userViewModel?.deleteUser(it)
                    showMessage(resources.getString(R.string.delete_success))
                }
            }
        }).attachToRecyclerView(recyclerView)


        userListAdapter?.setOnItemClickListener(object : UserListAdapter.OnItemClickListener {
            override fun updateUser(user: User) {
             /*   val intent = Intent(this@MainActivity, FullImageScreen::class.java)
                intent.putExtra(ID, user.id)
                intent.putExtra(LOGIN_USER, user.login)
                intent.putExtra(TYPE_USER, user.type)
                intent.putExtra(AVATAR_URL_USER, user.avatarUrl)
//                startActivityForResult(intent, UPDATE_REQUEST_CODE!!)  //commented to demonstrate eventbus
                startActivity(intent)*/

                val fragment = FullScreenImageFragment.newInstance(user.login!!,user.id!!,user.type!!,user.avatarUrl!!)
                val fragmentTransaction =
                    fragmentManager!!.beginTransaction()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.fragContainer, fragment)
                fragmentTransaction.commit()
            }
        })

        fab.setOnClickListener {
            startActivityForResult(
                Intent(this@MainActivity, AddUserActivity::class.java),
                INSERT_REQUEST_CODE!!
            )
        }


    }

    /*EventBus Implementation starts from here*/
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        Log.d("MainActivity","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity","onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity","onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity","onPause")
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        Log.d("MainActivity","onStop")
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity","onDestroy")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(user: User) {
        userViewModel?.deleteUser(user)
        showMessage(resources.getString(R.string.delete_success))
    }
    /*EventBus Implementation ends here*/


    private fun initRecycleView() {
        recyclerView = findViewById(R.id.recycle_view)
        recyclerView?.layoutManager = (LinearLayoutManager(this, RecyclerView.VERTICAL, false))
        recyclerView?.hasFixedSize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INSERT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                with(User())
                {
                    login = it.getStringExtra(LOGIN_USER)
                    type = it.getStringExtra(TYPE_USER)
                    avatarUrl = it.getStringExtra(AVATAR_URL_USER)
                    //adding the new user locally
                    userViewModel?.insertUser(this)
                    showMessage(resources.getString(R.string.insert_success))
                }

            }


        } else if (requestCode == UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                with(User())
                {
                    id = it.getIntExtra(ID, -1)
                    if (id != -1) {
                        login = it.getStringExtra(LOGIN_USER)
                        type = it.getStringExtra(TYPE_USER)
                        avatarUrl = it.getStringExtra(AVATAR_URL_USER)
                        //deleting the user locally
                        userViewModel?.deleteUser(this)
                        showMessage(resources.getString(R.string.delete_success))
                    }
                }

            }
        }
    }

}
