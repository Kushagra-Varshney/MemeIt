package com.example.memeit

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var memeUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.btnShare.setOnClickListener {
            shareMeme()
        }

        binding.btnNext.setOnClickListener {
            nextMeme()
        }

        loadMeme()
    }

    private fun loadMeme() {
        binding.progressBar.visibility = View.VISIBLE

        val url = "https://meme-api.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                memeUrl = response.getString("url")
                Glide.with(this).load(memeUrl)
                    .listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }

                    })
                    .into(binding.ivMemeImg)
            },
            { error ->
                // TODO: Handle error
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun nextMeme() {
        loadMeme()
    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey check this meme out! 😂 $memeUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using..")
        startActivity(chooser)
    }
}