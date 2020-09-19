package sim.coder.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.PhoneAuthProvider

import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential

import android.content.Intent









class MainActivity : AppCompatActivity() {

    lateinit var btnGenerateOTP : Button
    lateinit var btnSignIn : Button
    lateinit var etPhoneNumber : EditText
    lateinit var etOTP : EditText

    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth : FirebaseAuth
     lateinit var verificationCode:String
    lateinit var phoneNumber:String
    lateinit var otp:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        StartFirebaseLogin()


        btnGenerateOTP.setOnClickListener {
            phoneNumber = etPhoneNumber.text.toString()

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
            )

        }

        btnSignIn.setOnClickListener {

            otp =etOTP.text.toString()

            val credential = PhoneAuthProvider.getCredential(verificationCode, otp)
            SigninWithPhone(credential)

        }


    }

    private fun findViews() {
        btnGenerateOTP = findViewById(R.id.btn_generate_otp)
        btnSignIn = findViewById(R.id.btn_sign_in)

        etPhoneNumber = findViewById(R.id.et_phone_number)
        etOTP = findViewById(R.id.et_otp)
    }


    private fun StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance()
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Toast.makeText(this@MainActivity, "verification completed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@MainActivity, "verification fialed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken?
            ) {
                super.onCodeSent(s, forceResendingToken)
                verificationCode = s
                Toast.makeText(this@MainActivity, "Code sent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun SigninWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@MainActivity, SignedIn::class.java))
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }



}
