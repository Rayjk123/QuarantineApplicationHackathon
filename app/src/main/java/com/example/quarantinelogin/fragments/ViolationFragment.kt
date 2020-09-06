package com.example.quarantinelogin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quarantinelogin.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [TimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViolationFragment : Fragment() {
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_violation, container, false)
        val table: TableLayout = view.findViewById(R.id.table) as TableLayout

        getViolators(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Request Failure.")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                println(responseData)
                if (responseData != null) {
                    activity?.runOnUiThread {
                        try {
                            var json = JSONArray(responseData)

                            println("Request Successful!!")
                            var layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT)
                            layoutParams.weight = 1.0f
                            layoutParams.width = 0

                            for (i in 0 until json.length()) {
                                var newRow = TableRow(activity)
                                val item = json.getJSONObject(i)

                                val firstName = item.getJSONObject("firstName").getString("S")
                                val lastName = item.getJSONObject("lastName").getString("S")
                                val phoneNumber = item.getJSONObject("phoneNumber").getString("S")
                                val address = item.getJSONObject("address").getString("S")

                                var text = TextView(activity)
                                text.text = firstName

                                var text1 = TextView(activity)
                                text1.text = lastName

                                var text2 = TextView(activity)
                                text2.text = phoneNumber

                                var text3 = TextView(activity)
                                text3.text = address

                                newRow.addView(text, layoutParams)
                                newRow.addView(text1, layoutParams)
                                newRow.addView(text2, layoutParams)
                                newRow.addView(text3, layoutParams)
                                table.addView(newRow)
                                // Your code here
                            }


                            println("Request Successful to Get Quarantine Time!")
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
    fun getViolators( callback: Callback): Unit {
        val request = Request.Builder()
            .url("https://e2d600v4b3.execute-api.us-east-1.amazonaws.com/dev/violators")
            .get()
            .build()

        return client.newCall(request).enqueue(callback)
    }
}