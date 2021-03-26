package com.example.thealth

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.thealth.myApplication.Companion.PERMISSION_REQUEST_ACTIVITY_RECOGNITION
import com.example.thealth.ui.splash.SplashFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SplashFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            SplashFragment().onRequestPermissionsResultWithFragment(requestCode, permissions, grantResults);
        }
    }

    @Suppress("DEPRECATION")
    fun replaceFragment(mainActivity: MainActivity, newFragment: Fragment, stackOption: Boolean) {
        if( isActivityAvailable(this) ){
            var transaction: FragmentTransaction = mainActivity.supportFragmentManager.beginTransaction()

            if(!newFragment.isAdded) {
                try {
                    mainActivity.fragmentManager!!.beginTransaction();
                    transaction.replace(R.id.container, newFragment);
                    if( stackOption ){
                        transaction.addToBackStack(null);
                    }
                    transaction.commit();
                } catch (e: Exception) {
                    Log.e("test", "replaceFragment : $e")
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun isActivityAvailable(mainActivity: MainActivity): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            !mainActivity.isFinishing && !mainActivity.isDestroyed
        } else {
            !mainActivity.isFinishing
        }
    }
}