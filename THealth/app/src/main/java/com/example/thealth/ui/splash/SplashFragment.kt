package com.example.thealth.ui.splash

import android.Manifest
import android.animation.ValueAnimator
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.thealth.MainActivity
import com.example.thealth.R
import com.example.thealth.myApplication
import com.example.thealth.myApplication.Companion.PERMISSION_REQUEST_ACTIVITY_RECOGNITION
import com.example.thealth.myApplication.Companion.TAG
import com.example.thealth.ui.main.MainFragment

class SplashFragment : Fragment() {

    /**
     * This enum is used to define actions that can be performed after a successful sign in to Fit.
     * One of these values is passed to the Fit sign-in, and returned in a successful callback, allowing
     * subsequent execution of the desired action.
     */
    enum class FitActionRequestCode {
        SUBSCRIBE
    }

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        var appTitleText = view?.findViewById<TextView>(R.id.appTitleText)
        changeTextColor(appTitleText!!, Color.BLACK, Color.WHITE, View.LAYOUT_DIRECTION_LTR, 3000)

        var appTitleIconWater = view?.findViewById<ImageView>(R.id.appTitleIconWater)
        var appTitleIconWaterAni = AnimationUtils.loadAnimation(activity!!, R.anim.apptitleiconwater)
        appTitleIconWater!!.startAnimation(appTitleIconWaterAni)

        var appTitleIconEx1 = view?.findViewById<ImageView>(R.id.appTitleIconEx1)
        var appTitleIconEx1Ani = AnimationUtils.loadAnimation(activity!!, R.anim.apptitleiconex1)
        appTitleIconEx1!!.startAnimation(appTitleIconEx1Ani)
        var appTitleIconEx2 = view?.findViewById<ImageView>(R.id.appTitleIconEx2)
        var appTitleIconEx2Ani = AnimationUtils.loadAnimation(activity!!, R.anim.apptitleiconex2)
        appTitleIconEx2!!.startAnimation(appTitleIconEx2Ani)
    }

    private fun changePage() {
        var fragment: Fragment = MainFragment.newInstance()
        MainActivity().replaceFragment(activity!! as MainActivity, fragment, false)
    }

    private fun checkPermissionsAndRun(fitActionRequestCode: FitActionRequestCode) {
        if (runningQOrLater) {
            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACTIVITY_RECOGNITION)) {
                Log.d(TAG, "Permission is granted")
                Handler().postDelayed({
                    changePage()
                }, 500)
            } else {
                Log.d(TAG, "Permission is not granted")
                Handler().postDelayed({
                    requestRuntimePermissions(fitActionRequestCode)
                }, 500)
            }
        } else {
            //Android Version below Q, no need to request permission.
        }
    }

    private fun requestRuntimePermissions(requestCode: FitActionRequestCode) {
        requestCode.let {
            Log.d(TAG, "Requesting permission")
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), requestCode.ordinal)
        }
    }

    fun onRequestPermissionsResultWithFragment(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changePage()
            } else {
                android.os.Process.killProcess( android.os.Process.myPid() )
            }
        }
    }

    private fun changeTextColor(textView: TextView, fromColor: Int, toColor: Int, direction: Int = View.LAYOUT_DIRECTION_LTR, duration:Long = 200) {
        var startValue = 0
        var endValue = 0

        if (direction == View.LAYOUT_DIRECTION_LTR) {
            startValue = 0
            endValue = textView.text.length
        } else if (direction == View.LAYOUT_DIRECTION_RTL) {
            startValue = textView.text.length
            endValue = 0
        }

        textView.setTextColor(fromColor)
        val valueAnimator = ValueAnimator.ofInt(startValue, endValue)
        valueAnimator.addUpdateListener { animator ->
            val spannableString = SpannableString(textView.text)

            if (direction == View.LAYOUT_DIRECTION_LTR) spannableString.setSpan(
                ForegroundColorSpan(toColor), startValue, animator.animatedValue.toString().toInt(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            else if (direction == View.LAYOUT_DIRECTION_RTL) spannableString.setSpan(
                ForegroundColorSpan(toColor), animator.animatedValue.toString().toInt(),spannableString.length , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannableString
        }
        valueAnimator.duration = duration
        valueAnimator.start()

        valueAnimator.doOnEnd {
            checkPermissionsAndRun(FitActionRequestCode.SUBSCRIBE)
        }
    }
}


