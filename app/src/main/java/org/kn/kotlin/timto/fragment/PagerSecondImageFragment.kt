package org.kn.kotlin.timto.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kn.kotlin.timto.R

class PagerSecondImageFragment : Fragment() {


    private fun loadImageUsingGlide() {
        //secondFragmentProgressBar.visibility = View.VISIBLE
//        Picasso.get()
//                .load(R.drawable.ic_launcher_background)
//                .centerCrop()
//                .fit()
//                .placeholder(android.R.drawable.ic_menu_report_image)
//                .into(secondFragmentImageView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadImageUsingGlide()
    }

    companion object {
        fun newInstance() = PagerSecondImageFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pager_second_image, container, false)
    }
}