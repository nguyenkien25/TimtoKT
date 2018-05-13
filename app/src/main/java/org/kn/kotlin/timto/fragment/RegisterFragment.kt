package org.kn.kotlin.timto.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.fragment_register.*

import org.kn.kotlin.timto.R
import org.kn.kotlin.timto.activity.LoginRegisterActivity

class RegisterFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupContentUI()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    private fun setupContentUI() {
        edtPassword.setOnEditorActionListener() { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                attemptLogin()
                true
            } else {
                false
            }
        }
        btnRegister.setOnClickListener { attemptLogin() }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {

        // Reset errors.
        edtNumberPhone.error = null
        edtPassword.error = null

        // Store values at the time of the login attempt.
        val numberPhoneStr = edtNumberPhone.text.toString().trim()
        val passwordStr = edtPassword.text.toString().trim()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            edtPassword.error = getString(R.string.error_invalid_password)
            focusView = edtPassword
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(numberPhoneStr)) {
            edtNumberPhone.error = getString(R.string.error_field_required)
            focusView = edtNumberPhone
            cancel = true
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            (activity as LoginRegisterActivity).gotoVerify(numberPhoneStr, passwordStr)
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }
}
