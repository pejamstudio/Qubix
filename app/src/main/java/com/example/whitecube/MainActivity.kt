package com.example.whitecube

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.whitecube.Fragment.HomeFragment
import com.example.whitecube.Fragment.SayaFragment
import com.example.whitecube.Keamanan.KodeKeamanan
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var sayaFragment: SayaFragment
    private lateinit var SP : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav_ds)
        SP = getSharedPreferences("WhiteCube", Context.MODE_PRIVATE)
        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content,homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_NONE)
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){

                R.id.nav_home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit()

                }
                R.id.nav_saya -> {
                    sayaFragment = SayaFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(android.R.id.content,sayaFragment)
                        .setTransition(FragmentTransaction.TRANSIT_NONE)
                        .commit()

                }

            }
            true
        }
    }





}
