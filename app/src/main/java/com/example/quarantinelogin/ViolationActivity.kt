package com.example.quarantinelogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.quarantinelogin.fragments.ViolationFragment
import com.example.quarantinelogin.fragments.RegisterFragment
import kotlinx.android.synthetic.main.activity_time.*

class ViolationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_violation)

        val violationFragment = ViolationFragment()
        val registerFragment = RegisterFragment()

        makeCurrentFragment(violationFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_userviolation -> makeCurrentFragment(violationFragment)
                R.id.ic_register -> makeCurrentFragment(registerFragment)
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