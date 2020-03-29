package com.example.coronaaware.ui.ui

import android.content.Context
import android.content.Intent
import com.example.coronaaware.R

class FirstWalkthroughActivity : WalkthroughActivity() {


    val hello = "Hello"
    val schedule = "Hello1"
    val message = "Hello2"

    override val pages = listOf(
            FirstWalkthroughFragment.newInstance(
                    hello,
                    R.drawable.covid_19_1
            ),
            FirstWalkthroughFragment.newInstance(
                    schedule,
                    R.drawable.covid_19_2
            ),
            FirstWalkthroughFragment.newInstance(
                    message,
                    R.drawable.covid_19_3
            ),
            StartFragment.newInstance()
    )

    override fun onComplete() {

        val intent = Intent(this, OTPAuthentication::class.java)
        startActivity(intent)
        finish()
    }

    companion object {

        fun createIntent(context: Context) = Intent(context, FirstWalkthroughActivity::class.java)
    }
}
