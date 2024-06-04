package com.prismana.storyku.data.remote.response

import com.google.gson.annotations.SerializedName

class ErrorResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
) {
}