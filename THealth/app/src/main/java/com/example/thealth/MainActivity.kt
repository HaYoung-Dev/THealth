package com.example.thealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.thealth.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    companion object {
        @Suppress("DEPRECATION")
        fun replaceFragment(mainActivity: MainActivity, newFragment: Fragment) {
            var transaction: FragmentTransaction = mainActivity.supportFragmentManager.beginTransaction()

            if(!newFragment.isAdded) {
                try {
                    mainActivity.fragmentManager!!.beginTransaction();
                    transaction.replace(R.id.container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } catch (e: Exception) {
                    Log.e("test", "replaceFragment : $e")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}