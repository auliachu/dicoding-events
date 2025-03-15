package com.example.dicodingevents.ui.finished

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.response.DicodingEventsResponse
import com.example.dicodingevents.data.response.ListEventsItem
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.databinding.FragmentFinishedBinding
import com.example.dicodingevents.ui.CardAdapter
import com.example.dicodingevents.ui.detail.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CardAdapter

    companion object {
        private const val TAG = "SecondFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        findFinishedEvents()
    }

    private fun findFinishedEvents(){
        showLoading(true)
        val client = ApiConfig.getApiService().getEvent(0)
        client.enqueue(object: Callback<DicodingEventsResponse> {
            override fun onResponse(
                call: Call<DicodingEventsResponse>,
                response: Response<DicodingEventsResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null ){
                        setEventData(responseBody.listEvents)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
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
        binding.rvEventsFinished.layoutManager = layoutManager
        binding.rvEventsFinished.adapter = adapter
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvEventsFinished.addItemDecoration(itemDecoration)
    }

    private fun setEventData(events : List<ListEventsItem>){
        adapter.submitList(events)
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}