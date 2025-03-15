package com.example.dicodingevents.data.response

import com.google.gson.annotations.SerializedName

data class DicodingEventsResponse(

	@field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)