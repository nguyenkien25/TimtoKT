package org.kn.kotlin.timto.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View

import kotlinx.android.synthetic.main.activity_launch.*
import org.kn.kotlin.timto.R
import org.kn.kotlin.timto.adapter.ImageFragmentPagerAdapter
import org.kn.kotlin.timto.utils.Constants
import java.util.*


class LaunchActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val pagerAdapter = ImageFragmentPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        pageDots.setViewPager(viewPager)
        autoSwipe()
    }

    fun registerClick(view: View) {
        showLoginActivity(Constants.REGISTER)
    }

    fun loginClick(view: View) {
        showLoginActivity(Constants.LOGIN)
    }

    private fun showLoginActivity(flag: String) {
        val intent = Intent(this, LoginRegisterActivity::class.java)
        intent.putExtra(Constants.FLAG_LOGIN, flag)
        startActivity(intent)
    }

    private fun autoSwipe() {
        val delay: Long = 50//delay in milliseconds before task is to be executed
        val period: Long = 3000 // time in milliseconds between successive task executions.
        var numberPage: Int = (viewPager.adapter as ImageFragmentPagerAdapter).count

        val handler = Handler()
        val update = Runnable {
            var currentPage = viewPager.currentItem
            if (currentPage == numberPage - 1) {
                currentPage = 0
            } else {
                currentPage += 1
            }
            viewPager.setCurrentItem(currentPage, true)
        }

        var timer = Timer() // This will create a new Thread
        timer.schedule(object : TimerTask() { // task to be scheduled
            override fun run() {
                handler.post(update)
            }
        }, delay, period)
    }
}
