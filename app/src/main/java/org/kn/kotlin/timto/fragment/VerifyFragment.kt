package org.kn.kotlin.timto.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_verify.*

import org.kn.kotlin.timto.R
import org.kn.kotlin.timto.activity.LoginRegisterActivity
import org.kn.kotlin.timto.utils.Constants
import java.util.concurrent.TimeUnit


class VerifyFragment : Fragment() {

    private var numberPhone: String = ""
    private var password: String = ""

    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    // TODO: Timer
    private lateinit var customHandler: Handler
    private var timeInMilliseconds: Long = 0
    private var timeSwapBuff: Long = 0
    private var updatedTime: Long = 0
    private var startTime: Long = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupContentUI()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    private fun setupContentUI() {
        numberPhone = (activity as LoginRegisterActivity).numberPhoneRegister
        password = (activity as LoginRegisterActivity).passwordRegister

        FirebaseApp.initializeApp(activity)
        auth = FirebaseAuth.getInstance()
        initCallbackVerify()
        sendCodeVerify(numberPhone, callbacks)

        btnResend.setOnClickListener {
            btnResend.isEnabled = false
            resendVerificationCode(numberPhone, callbacks, resendToken)
        }

        btnNext.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(verificationId, pinEntry.text.toString())
            signInWithPhoneAuthCredential(credential)
        }
        pinEntry.setOnPinEnteredListener { str ->
            btnNext.isEnabled = (str.toString().length == 6)
        }
    }

    private fun sendCodeVerify(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                                    // Phone number to verify
                Constants.TIME_OUT_SEND_CODE,                   // Timeout duration
                TimeUnit.SECONDS,                               // Unit of timeout
                activity as LoginRegisterActivity,              // Activity (for callback binding)
                callbacks)                                      // OnVerificationStateChangedCallbacks
    }

    private fun resendVerificationCode(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks, token: PhoneAuthProvider.ForceResendingToken?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                                    // Phone number to verify
                Constants.TIME_OUT_SEND_CODE,                   // Timeout duration
                TimeUnit.SECONDS,                               // Unit of timeout
                activity as LoginRegisterActivity,              // Activity (for callback binding)
                callbacks,                                      // OnVerificationStateChangedCallbacks
                token)                                          // ForceResendingToken from callbacks
    }

    private fun initCallbackVerify() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // verification completed
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked if an invalid request for verification is made,
                // for instance if the the phone number format is invalid.
                Toast.makeText(activity, (activity as LoginRegisterActivity).getString(R.string.send_code_fail), Toast.LENGTH_LONG).show()

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(activity, (activity as LoginRegisterActivity).getString(R.string.number_phone_fail), Toast.LENGTH_LONG).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(activity, (activity as LoginRegisterActivity).getString(R.string.quota_exceeded), Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
            }

            override fun onCodeSent(verifiId: String, token: PhoneAuthProvider.ForceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Toast.makeText(activity, (activity as LoginRegisterActivity).getString(R.string.send_code) + " " + numberPhone, Toast.LENGTH_LONG).show()
                // Save verification ID and resending token so we can use them later
                verificationId = verifiId
                resendToken = token

                startTime = System.currentTimeMillis()
                customHandler = Handler()
                customHandler.postDelayed(updateTimerThread, 1000)
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
                // called when the timeout duration has passed without triggering onVerificationCompleted
                super.onCodeAutoRetrievalTimeOut(verificationId)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity as LoginRegisterActivity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        auth.signOut()
                        performFirebaseRegistration(numberPhone + Constants.EXTENSION_EMAIL, password)
                    } else {
                        // Sign in failed, display a message and update the UI
                        Toast.makeText(activity, (activity as LoginRegisterActivity).getString(R.string.register_fail), Toast.LENGTH_LONG).show()
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(activity, (activity as LoginRegisterActivity).getString(R.string.code_fail), Toast.LENGTH_LONG).show()
                        }
                        // Sign in failed
                    }
                }
    }

    private fun performFirebaseRegistration(email: String, pass: String) {
        activity?.let {
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(it) { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(it, it.getString(R.string.register_fail), Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(activity, it.getString(R.string.register_successful), Toast.LENGTH_LONG).show()
                            (activity as LoginRegisterActivity).goToMain()
                        }
                    }
        }
    }

    private val updateTimerThread = object : Runnable {
        override fun run() {
            timeInMilliseconds = System.currentTimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds
            var seconds: Long = Constants.TIME_OUT_SEND_CODE - (updatedTime / 1000)
            if (seconds == 0L) {
                btnResend.text = getString(R.string.resend)
                btnResend.isEnabled = true
            } else {
                if (btnResend != null) {
                    btnResend.text = getString(R.string.resend) + "($seconds)"
                    customHandler.postDelayed(this, 1000)
                }
            }
        }
    }
}
