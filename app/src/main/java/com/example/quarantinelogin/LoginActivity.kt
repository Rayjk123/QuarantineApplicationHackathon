package com.example.quarantinelogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.radar.sdk.Radar
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Radar.initialize(this, "prj_live_pk_8440ebb4edeb83015a30e26e3f0b292de8b98bc4")

        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            auth(phone_et.text.toString(), password_et.text.toString(), object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Request Failure.")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    runOnUiThread{
                        try {
                            var json = JSONObject(responseData)
                            println("Request Successful!!")
                            println(json)
                            val validUser = json.getBoolean("validUser")
                            Toast.makeText(this@LoginActivity,"Logged In Successfully",Toast.LENGTH_SHORT).show()
                            if (validUser) {
                                Radar.setUserId(json.getJSONObject("user").getJSONObject("Item").getString("phoneNumber"))
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(this@LoginActivity,"Login Failed",Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }
                }
            });
        }
    }

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

    var client = OkHttpClient()

    @Throws(IOException::class)
    fun auth(phoneNumber: String, password: String, callback: Callback): Unit {
        val postBody = "{\"phoneNumber\":\"$phoneNumber\", \"password\":\"$password\"}"
        val request = Request.Builder()
            .url("https://e2d600v4b3.execute-api.us-east-1.amazonaws.com/dev/auth")
            .post(postBody.toRequestBody(JSON))
            .build()

        return client.newCall(request).enqueue(callback)
    }
}
