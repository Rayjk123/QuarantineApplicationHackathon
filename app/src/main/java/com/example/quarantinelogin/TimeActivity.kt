package com.example.quarantinelogin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.quarantinelogin.fragments.MapFragment
import com.example.quarantinelogin.fragments.TimeFragment
import kotlinx.android.synthetic.main.activity_time.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONException
import java.io.IOException

class TimeActivity : AppCompatActivity() {
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        val timeFragment = TimeFragment()
        val mapFragment = MapFragment()

        makeCurrentFragment(timeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_time -> makeCurrentFragment(timeFragment)
                R.id.ic_map -> makeCurrentFragment(mapFragment)
            }
            true
        }

        val phoneNumber = LoginActivity.Companion.user?.getJSONObject("Item")?.getJSONObject("phoneNumber")?.getString("S")
        getQuarantineEndTime(phoneNumber, object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Request Failure.")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                if (body != null) {
                    val QuarantineEndTimeInMilliseconds = body.toLong()

                    runOnUiThread {
                        try {
                            // TODO Get Time Left = QuarantineEndTimeInMilliseconds - CURRENT TIME IN MILLISECONDS

                            // TODO DISPLAY TIME LEFT IN TEXT FORMAT ON SCREEN

                            println("Request Successful to Get Quarantine Time!")
                            println(QuarantineEndTimeInMilliseconds)
                            Toast.makeText(
                                this@TimeActivity,
                                "Created Geofence Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@TimeActivity,
                                "An Error occurred while creating the Geofence. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                        }
                    }
                }
            }
        });
    }

    @Throws(IOException::class)
    fun getQuarantineEndTime(phoneNumber: String?, callback: Callback): Unit {
        val request = Request.Builder()
            .url("https://e2d600v4b3.execute-api.us-east-1.amazonaws.com/dev/user/$phoneNumber/quarantineTimer")
            .get()
            .build()

        return client.newCall(request).enqueue(callback)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}