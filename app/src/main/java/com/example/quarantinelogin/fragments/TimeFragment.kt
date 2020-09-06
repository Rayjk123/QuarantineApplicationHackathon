package com.example.quarantinelogin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quarantinelogin.LoginActivity
import com.example.quarantinelogin.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONException
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 * Use the [TimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeFragment : Fragment() {

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_time, container, false)
        val timerTextView: TextView = view.findViewById(R.id.timeLeftTextView) as TextView
        // Inflate the layout for this fragment
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

                    activity?.runOnUiThread {
                        try {
                            // TODO Get Time Left = QuarantineEndTimeInMilliseconds - CURRENT TIME IN MILLISECONDS
                            val calendar = Calendar.getInstance()
                            val currentTime = calendar.timeInMillis
                            val timeLeft = QuarantineEndTimeInMilliseconds - currentTime
                            println("Time Left is: $timeLeft")

                            val daysLeft = String.format("%02d", TimeUnit.MILLISECONDS.toDays(timeLeft))
                            val hoursLeft = String.format("%02d", TimeUnit.MILLISECONDS.toHours(timeLeft)-
                                    TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(timeLeft)))
                            val minutesLeft = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(timeLeft) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft)))
                            val secondsLeft = String.format("%02d",  TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft)))

                            val timer = "$daysLeft days $hoursLeft hours $minutesLeft minutes $secondsLeft seconds"
                            timerTextView.text = timer
                            println("$timer")



                            // TODO DISPLAY TIME LEFT IN TEXT FORMAT ON SCREEN

                            println("Request Successful to Get Quarantine Time!")
                            println(QuarantineEndTimeInMilliseconds)
                            Toast.makeText(
                                activity,
                                "Created Geofence Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                        } catch (e: JSONException) {
                            Toast.makeText(
                                activity,
                                "An Error occurred while creating the Geofence. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
        return view
    }


    @Throws(IOException::class)
    fun getQuarantineEndTime(phoneNumber: String?, callback: Callback): Unit {
        val request = Request.Builder()
            .url("https://e2d600v4b3.execute-api.us-east-1.amazonaws.com/dev/user/$phoneNumber/quarantineTimer")
            .get()
            .build()

        return client.newCall(request).enqueue(callback)
    }
}