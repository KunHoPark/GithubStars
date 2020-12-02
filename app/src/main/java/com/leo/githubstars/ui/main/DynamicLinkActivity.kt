package com.leo.githubstars.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.leo.githubstars.R
import com.leo.githubstars.util.LeoLog
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
            startActivity(Intent.createChooser(intent, "Share DynamicLink"))
        }

        btnShortShared.setOnClickListener {
            generateContentShortLink().let {
                it.addOnCompleteListener(this, object : OnCompleteListener<ShortDynamicLink>{
                override fun onComplete(task: Task<ShortDynamicLink>) {
                    if (task.isSuccessful) {
                        LeoLog.i("DynamicLinkActivity", "Short DyanmickLink Uri=${task.result!!.shortLink}")
                        val link = task.result!!.shortLink
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, link.toString())
                        startActivity(Intent.createChooser(intent, "Share Short DynamicLink"))
                    }
                }
            })
            }

        }

    }

    private fun generateContentLink(): Uri {
        val url = "https://www.example.com/"
        val promotionCode = "AAAAAA"
        val baseUrl = Uri.parse("${url}?promotion=$promotionCode")
//        val baseUrl = Uri.parse("$url")
        val domain = "https://leopark.page.link"

        val link = FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink(baseUrl)
            .setDomainUriPrefix(domain)
            .setIosParameters(DynamicLink.IosParameters.Builder("com.your.bundleid").build())
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder("com.leo.githubstars").build())
            .buildDynamicLink()

        LeoLog.i("DynamicLinkActivity", "DyanmickLink Uri=${link.uri}")
        return link.uri
    }

    private fun generateContentShortLink(): Task<ShortDynamicLink> {
        val url = "https://www.example.com/"
        val promotionCode = "AAAAAA"
        val baseUrl = Uri.parse("${url}?promotion=$promotionCode")
//        val baseUrl = Uri.parse("$url")
        val domain = "https://leopark.page.link"

        return FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink(baseUrl)
            .setDomainUriPrefix(domain)
            .setIosParameters(DynamicLink.IosParameters.Builder("com.your.bundleid").build())
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder("com.leo.githubstars").build())
            .buildShortDynamicLink()

    }
}