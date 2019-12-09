package com.mobility.myapplication.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jsibbold.zoomage.ZoomageView
import com.mobility.myapplication.Constants
import com.mobility.myapplication.R
import com.mobility.myapplication.model.User
import org.greenrobot.eventbus.EventBus

/**
 * A simple [Fragment] subclass.
 */
class FullScreenImageFragment : Fragment() {

    private var myView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_blank, container, false)
        val fullImageScreen: ZoomageView = myView!!.findViewById(R.id.full_screen_image_fragment)
        val floatingActionButton: FloatingActionButton =
            myView!!.findViewById(R.id.floatingActionButton)
        val imageUrl = arguments!!.getString(Constants.AVATAR_URL_USER)
        arguments?.let {
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(this).load(imageUrl).apply(requestOptions)
                .into(fullImageScreen)
        }

        floatingActionButton.setOnClickListener {
            arguments?.let {
                with(User())
                {
                    id = it.getInt(Constants.ID)
                    if (id != -1) {
                        id = it.getInt(Constants.ID)
                        login = it.getString(Constants.LOGIN_USER)
                        type = it.getString(Constants.TYPE_USER)
                        avatarUrl = it.getString(Constants.AVATAR_URL_USER)
                        EventBus.getDefault().post(this)
                    }
                }
            }
            fragmentManager!!.popBackStack()


        }

        return myView
    }

    companion object {

        fun newInstance(
            login: String,
            id: Int,
            type: String,
            avatarUrl: String
        ): FullScreenImageFragment {
            val bundle = Bundle()
            bundle.putInt(Constants.ID, id!!)
            bundle.putString(Constants.LOGIN_USER, login)
            bundle.putString(Constants.TYPE_USER, type)
            bundle.putString(Constants.AVATAR_URL_USER, avatarUrl)
            val fragment = FullScreenImageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}
