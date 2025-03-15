package com.example.dicodingevents.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.response.DicodingEventsResponse
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.databinding.FragmentUpcomingBinding
import com.example.dicodingevents.ui.CardAdapter
import com.example.dicodingevents.ui.detail.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CardAdapter

    companion object{
        private const val TAG = "MainFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        findEvents()
    }

    private fun findEvents() {
        showLoading(true)
        val client = ApiConfig.getApiService().getEvent(1)
        client.enqueue(object : Callback<DicodingEventsResponse> {
            override fun onResponse(
                call: Call<DicodingEventsResponse>,
                response: Response<DicodingEventsResponse>
            ) {
                showLoading(false)
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        setEventData(responseBody.listEvents)
                    }
                } else{
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DicodingEventsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setupRecyclerView(){
        adapter = CardAdapter{
                eventId -> Toast.makeText(requireContext(), "Clicked event ID: $eventId", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_EVENT_ID, eventId)
                startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.layoutManager = layoutManager
        binding.rvEvents.adapter = adapter
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvEvents.addItemDecoration(itemDecoration)
    }

    private fun setEventData(events: List<ListEventsItem>) {
        adapter.submitList(events)
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}