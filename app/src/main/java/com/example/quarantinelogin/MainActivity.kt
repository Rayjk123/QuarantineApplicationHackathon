package com.example.quarantinelogin

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.radar.sdk.Radar
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    public val INTERNET_REQUEST_CODE = 1;
    public val ACCESS_FINE_LOCATION_CODE = 2;
    public val ACCESS_NETWORK_STATE_CODE = 3;
    public val RECEIVE_BOOT_COMPLETED_CODE = 4;
    public val ACCESS_BACKGROUND_LOCATION_CODE = 5;
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Radar.initialize(this, "prj_live_pk_8440ebb4edeb83015a30e26e3f0b292de8b98bc4")
        showInternetPermission()
        showFineLocationPermission()
        showAccessNetworkStatePermission()
        showReceiveBootPermission()
        showAccessBackgroundLocationPermission()

        start_btn.setOnClickListener {
            Radar.getLocation  { status, location, stopped ->
                println("location: $location , and Status: $status")
                if (location != null) {
                    println("Latitude: " + location.latitude + " Longitude: " + location.longitude)
                }
                val LoginActivity = LoginActivity()
                val phoneNumber = LoginActivity.user?.getJSONObject("Item")?.getString("phoneNumber")

                if (location != null) {
                    startGeoFencing(phoneNumber, location.longitude, location.latitude, object: Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            println("Request Failure.")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val responseData = response.body?.string()
                            runOnUiThread{
                                try {
                                    println("Request Successful to Put Geofence!")
                                    println(responseData)
                                    Toast.makeText(this@MainActivity,"Created Geofence Successfully",Toast.LENGTH_SHORT).show()

                                } catch (e: JSONException) {
                                    Toast.makeText(this@MainActivity,"An Error occurred while creating the Geofence. Please try again.",Toast.LENGTH_SHORT).show()
                                    e.printStackTrace()
                                }
                            }
                        }
                    });
                }
            }

            val intent = Intent(this, TimeActivity::class.java)
            //start your next activity
            startActivity(intent)
        }


    }

    @Throws(IOException::class)
    fun startGeoFencing(phoneNumber: String?, longitude: Double, latitude: Double, callback: Callback): Unit {
        val putBody = "{\"phoneNumber\":\"$phoneNumber\", \"longitude\":\"$longitude\", \"latitude\":\"$latitude\"}"
        val request = Request.Builder()
            .url("https://e2d600v4b3.execute-api.us-east-1.amazonaws.com/dev/geofence")
            .put(putBody.toRequestBody(JSON))
            .build()

        return client.newCall(request).enqueue(callback)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            INTERNET_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this@MainActivity, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun showInternetPermission() {
        println("Trying to request Permissions")
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.INTERNET
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.INTERNET
                )
            ) {
                showExplanation(
                    "Permission Needed",
                    "Rationale",
                    Manifest.permission.INTERNET,
                    INTERNET_REQUEST_CODE
                )
            } else {
                requestPermission(
                    Manifest.permission.INTERNET,
                    INTERNET_REQUEST_CODE
                )
            }
        } else {
            Toast.makeText(this@MainActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showFineLocationPermission() {
        println("Trying to request Permissions")
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showExplanation(
                    "Permission Needed",
                    "Rationale",
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    ACCESS_FINE_LOCATION_CODE
                )
            } else {
                requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    ACCESS_FINE_LOCATION_CODE
                )
            }
        } else {
            Toast.makeText(this@MainActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showAccessNetworkStatePermission() {
        println("Trying to request Permissions")
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_NETWORK_STATE
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_NETWORK_STATE
                )
            ) {
                showExplanation(
                    "Permission Needed",
                    "Rationale",
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    ACCESS_NETWORK_STATE_CODE
                )
            } else {
                requestPermission(
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    ACCESS_NETWORK_STATE_CODE
                )
            }
        } else {
            Toast.makeText(this@MainActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showReceiveBootPermission() {
        println("Trying to request Permissions")
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECEIVE_BOOT_COMPLETED
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED
                )
            ) {
                showExplanation(
                    "Permission Needed",
                    "Rationale",
                    Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    RECEIVE_BOOT_COMPLETED_CODE
                )
            } else {
                requestPermission(
                    Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    RECEIVE_BOOT_COMPLETED_CODE
                )
            }
        } else {
            Toast.makeText(this@MainActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showAccessBackgroundLocationPermission() {
        println("Trying to request Permissions")
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                showExplanation(
                    "Permission Needed",
                    "Rationale",
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    ACCESS_BACKGROUND_LOCATION_CODE
                )
            } else {
                requestPermission(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    ACCESS_BACKGROUND_LOCATION_CODE
                )
            }
        } else {
            Toast.makeText(this@MainActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showExplanation(
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { dialog, id ->
                    requestPermission(
                        permission,
                        permissionRequestCode
                    )
                })
        builder.create().show()
    }

    private fun requestPermission(
        permissionName: String,
        permissionRequestCode: Int
    ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permissionName),
            permissionRequestCode
        )
    }
}