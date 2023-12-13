package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.back_button)

        backButton.setOnClickListener {

            finish()
        }

        val shareButton = findViewById<ImageButton>(R.id.settings_share_button)

        shareButton.setOnClickListener {

            val shareBody = getString(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

            val smsIntent = Intent(Intent.ACTION_VIEW)
            smsIntent.data = Uri.parse("sms:")
            val chooserIntent = Intent.createChooser(shareIntent, "Share via")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(smsIntent))

            startActivity(chooserIntent)
        }
        val supportButton = findViewById<ImageButton>(R.id.settings_support_button)

        supportButton.setOnClickListener {



            val sendIntent = Intent(Intent.ACTION_SENDTO)
                sendIntent.data = Uri.parse("mailto:")
                sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_recipient)))
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                startActivity(sendIntent)


    }
        val EULAButton = findViewById<ImageButton>(R.id.EULA_button)

        EULAButton.setOnClickListener {

            val link = getString(R.string.EULA_link)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
}}