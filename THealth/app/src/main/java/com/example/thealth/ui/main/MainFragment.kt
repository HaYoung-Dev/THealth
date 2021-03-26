package com.example.thealth.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.thealth.MainActivity
import com.example.thealth.R
import com.example.thealth.ui.overview.OverviewFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        val RC_SIGN_IN: Int = 9001
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
        //updateUI(account)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Set the dimensions of the sign-in button.
        var signInButton = view?.findViewById<SignInButton>(R.id.sign_in_button)
        signInButton!!.setOnClickListener {
            when (it.id) {
                R.id.sign_in_button -> {
                    signIn()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == RC_SIGN_IN ){
            var task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            var account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
//            updateUI(account)
            temporaryupdateUI()
        } catch (e: ApiException){
            Log.e("test", "signInResult: Failed code = ${e.statusCode}")
            temporaryupdateUI()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(userInfo: GoogleSignInAccount?) {
        Log.i("test", "Google Login Success [USER EMAIL] : ${userInfo!!.email}")
        changePage()
    }

    private fun temporaryupdateUI(){
        changePage()
    }

    private fun changePage() {
        var fragment: Fragment = OverviewFragment.newInstance()
        MainActivity().replaceFragment(activity!! as MainActivity, fragment, false)
    }

}