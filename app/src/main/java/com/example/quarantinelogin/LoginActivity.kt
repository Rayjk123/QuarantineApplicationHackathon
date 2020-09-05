package com.example.quarantinelogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            var status= if(phone_et.text.toString().equals("12345")
                && password_et.text.toString().equals("password")) "Logged In Successfully" else "LogIn Fail"
            Toast.makeText(this,status,Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
                //start your next activity
                startActivity(intent)
        }
    }
}
