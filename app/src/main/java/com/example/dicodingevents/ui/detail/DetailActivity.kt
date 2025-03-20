package com.example.dicodingevents.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.response.DetailResponse
import com.example.dicodingevents.data.response.Event
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity  : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var isBookmarked : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)
        if(eventId != null){
            fetchEventDetail(eventId)

        } else {
            Toast.makeText(this, "Event ID tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }


    private fun fetchEventDetail(eventId: String){
        showLoading(true)

        val client = ApiConfig.getApiService().getDetailEvent(eventId)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                showLoading(false)
                if(response.isSuccessful){
                    val responseBody = response.body()
                    val result = responseBody?.event
                    if(result != null){
                        displayEventDetail(result)
                    }
                } else{
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    private fun displayEventDetail(result: Event?){
        val totalQuota = (result?.quota ?: 0) - (result?.registrants ?: 0)
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(result?.mediaCover)
                .into(imageEvent)
            descEvent.text = HtmlCompat.fromHtml(result?.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
            titleEvent.text = result?.name
            sisaQuota.text = "${totalQuota} orang"
            summaryEvent.text = result?.summary
            textCategory.text = result?.category
            ownerName.text = result?.ownerName
            cityName.text = result?.cityName
            registrant.text = result?.registrants.toString()
            quota.text = result?.quota.toString()
            beginTime.text = result?.beginTime
            endTime.text = result?.endTime

            buttonLink.setOnClickListener{
                val eventLink = result?.link
                if(!eventLink.isNullOrEmpty()){
                    openLink(eventLink)
                } else {
                    Toast.makeText(this@DetailActivity, "URL tidak tersedia lagi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openLink(link : String){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }
        try {
            startActivity(intent)
        } catch (e: Exception){
            Toast.makeText(this, "Gagal membuka link", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onFailure: ${e.message}")
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "DetailActivity"
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}