package com.example.quarantinelogin.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quarantinelogin.R
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [TimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()
    var password: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)
        val bt1: Button = view.findViewById(R.id.register_btn) as Button

        bt1.setOnClickListener {
            val randomPIN = ((Math.random() * 9000).toInt() + 1000).toString(10)
            password = randomPIN
            if (phone_et.text.toString().equals("")) {
                Toast.makeText(activity,"Phone Number is required",Toast.LENGTH_SHORT)
                return@setOnClickListener
            }


            registerUser(
                phone_et.text.toString(),
                firstName_et.text.toString(),
                lastName_et.text.toString(),
                address_et.text.toString(),
                randomPIN,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Request Failure.")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseData = response.body?.string()
                        activity?.runOnUiThread {
                            try {
                                println("Request Successful to Register User!")
                                println(responseData)
                                Toast.makeText(
                                    activity,
                                    "Successfully Registered User",
                                    Toast.LENGTH_SHORT
                                ).show()

                                AlertDialog.Builder(context)
                                    .setTitle("Succesfully Registered User")
                                    .setMessage("User unique password is: $password") // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(
                                        android.R.string.yes,
                                        DialogInterface.OnClickListener { dialog, which ->
                                            // Continue with delete operation
                                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show()

                                phone_et.text.clear()
                                firstName_et.text.clear()
                                lastName_et.text.clear()
                                address_et.text.clear()
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    activity,
                                    "An Error Occurred while attempting to Register User",
                                    Toast.LENGTH_SHORT
                                ).show()
                                e.printStackTrace()
                            }
                        }
                    }
                })
        }
        // Inflate the layout for this fragment
        return view
    }

    @Throws(IOException::class)
    fun registerUser(
        phoneNumber: String?,
        firstName: String?,
        lastName: String?,
        address: String?,
        password: String?,
        callback: Callback
    ): Unit {
        val postBody =
            "{\"phoneNumber\":\"$phoneNumber\", \"firstName\":\"$firstName\", \"lastName\":\"$lastName\", \"address\":\"$address\", \"password\":\"$password\"}"
        val request = Request.Builder()
            .url("https://e2d600v4b3.execute-api.us-east-1.amazonaws.com/dev/register")
            .post(postBody.toRequestBody(JSON))
            .build()

        return client.newCall(request).enqueue(callback)
    }
}