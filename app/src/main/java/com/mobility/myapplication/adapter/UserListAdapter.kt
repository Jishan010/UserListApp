package com.mobility.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mobility.myapplication.R
import com.mobility.myapplication.model.User


/**
 *
 * Created By J7202687 On 10/22/2019
 */


/**
 * here we're using listAdapter abstract class which is base class of recyclerView
 * to make use of awesome api diffutil which enables add and remove animation on list item notify data set change
 */
class UserListAdapter : ListAdapter<User, UserListAdapter.MyUserViewHolder>(diffCallback) {

    //    private List<Note> notes = new ArrayList<>();
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_adapter, null)
        return MyUserViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyUserViewHolder, position: Int) {
        val note = getItem(position)
        holder.onBind(note)
    }


    fun getUsers(position: Int): User? {
        return getItem(position)
    }


    inner class MyUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val userTypeTextView: TextView = itemView.findViewById(R.id.userTypeTextView)
        private var imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener { onItemClickListener!!.updateUser(getUsers(adapterPosition)!!) }
        }

        fun onBind(user: User) {
            userNameTextView.text = user.login
            userTypeTextView.text = user.type

            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(itemView.context).load(user.avatarUrl).apply(requestOptions).into(imageView)
        }
    }

    interface OnItemClickListener {
        fun updateUser(user: User)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id === newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.login.equals(newItem.login) && oldItem.type.equals(
                    newItem.type
                )
            }
        }
    }

}
