package com.example.quarantinelogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.quarantinelogin.fragments.MapFragment
import com.example.quarantinelogin.fragments.TimeFragment
import kotlinx.android.synthetic.main.activity_time.*

class TimeActivity : AppCompatActivity() {
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
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}