package com.mobility.myapplication.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mobility.myapplication.R
import com.mobility.myapplication.adapter.UserListAdapter
import com.mobility.myapplication.model.User
import com.mobility.myapplication.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var userViewModel: UserViewModel? = null
    private var userList: List<User>? = null
    private var recyclerView: RecyclerView? = null
    private var userListAdapter: UserListAdapter? = null


    companion object {
        val LOGIN_USER: String? = "login_user"
        val TYPE_USER: String? = "type_user"
        val AVATAR_URL_USER: String? = "avatar_user"
        val INSERT_REQUEST_CODE :Int?= 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initRecycleView()
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
                    userViewModel?.deleteUser(
                        it
                    )
                }
            }
        }).attachToRecyclerView(recyclerView)


        userListAdapter?.setOnItemClickListener(object : UserListAdapter.OnItemClickListener {
            override fun updateUser(user: User) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        fab.setOnClickListener { view ->
            val intent = Intent(this, AddUserActivity::class.java)
            startActivityForResult(intent, INSERT_REQUEST_CODE!!)

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun initRecycleView() {
        recyclerView = findViewById(R.id.recycle_view)
        recyclerView?.layoutManager = (LinearLayoutManager(this, RecyclerView.VERTICAL, false))
        recyclerView?.hasFixedSize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == INSERT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val user = User()
            user.login = data!!.getStringExtra(LOGIN_USER)
            user.type = data!!.getStringExtra(TYPE_USER)
            user.avatarUrl = data!!.getStringExtra(AVATAR_URL_USER)
            //adding the new user locally
            userViewModel?.insertUser(user)

        }

    }

}
