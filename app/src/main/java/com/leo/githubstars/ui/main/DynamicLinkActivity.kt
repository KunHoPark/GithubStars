package com.leo.githubstars.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.leo.githubstars.R
import kotlinx.android.synthetic.main.dynamiclink_activity.*


class DynamicLinkActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dynamiclink_activity)

        initView()
    }

    private fun initView() {
        btnShared.setOnClickListener {
            val link = generateContentLink()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, link.toString())
            startActivity(Intent.createChooser(intent, "Share Link"))
        }
    }

    private fun generateContentLink(): Uri {
        val promotionCode = "DF3DY1"
        val baseUrl = Uri.parse("https://leopark.page.link/mainactivity?promotion=$promotionCode")
        val domain = "https://leopark.page.link"

        val link = FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink(baseUrl)
            .setDomainUriPrefix(domain)
            .setIosParameters(DynamicLink.IosParameters.Builder("com.your.bundleid").build())
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder("com.leo.githubstars").build())
            .buildDynamicLink()
        return link.uri
    }
}