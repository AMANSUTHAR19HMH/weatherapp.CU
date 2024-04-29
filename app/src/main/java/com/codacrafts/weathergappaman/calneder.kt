package com.codacrafts.weathergappaman

import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class calneder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calneder)

        MobileAds.initialize(this) {}

        val adView = findViewById<AdView>(R.id.adview)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val textView = findViewById<TextView>(R.id.textView)
        textView.setTypeface(null, Typeface.BOLD)

//        loadBanner(adView)
    }

//    private fun loadBanner(adView: AdView) {
//        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
//        adView.adUnitId = "ca-app-pub-4681171705756049/9153721629"
//        adView.setAdSize(AdSize.BANNER)
//
//        // Create an ad request.
//        val adRequest = AdRequest.Builder().build()
//
//        // Start loading the ad in the background.
//        adView.loadAd(adRequest)
//    }
}
