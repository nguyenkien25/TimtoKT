package org.kn.kotlin.timto.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.kn.kotlin.timto.R
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.MenuItem
import org.kn.kotlin.timto.utils.Constants
import org.kn.kotlin.timto.fragment.RegisterFragment
import org.kn.kotlin.timto.fragment.LoginFragment
import org.kn.kotlin.timto.fragment.VerifyFragment

import kotlinx.android.synthetic.main.activity_login_register.*


class LoginRegisterActivity : AppCompatActivity() {

    var numberPhoneRegister: String = ""
    var passwordRegister: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        setupContentUI()
    }

    private fun setupContentUI() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_black)

        val intent = intent
        if (intent != null) {
            val flag = intent.getStringExtra(Constants.FLAG_LOGIN)
            loadFragment(flag)
        }
    }

    fun gotoVerify(number: String, password: String) {
        this.numberPhoneRegister = Constants.PEX_PHONE + number.substring(1)
        this.passwordRegister = password
        loadFragment(Constants.VERIFY)
    }

    fun loadFragment(flag: String?) {
        if (flag != null && !flag.isEmpty()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, getFragment(flag))
                    .addToBackStack(null)
                    .commit()
        }
    }

    private fun getFragment(flag: String): Fragment {
        return when (flag) {
            Constants.LOGIN -> LoginFragment()
            Constants.REGISTER -> RegisterFragment()
            else -> {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black)
                VerifyFragment()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        finish()
    }
}
