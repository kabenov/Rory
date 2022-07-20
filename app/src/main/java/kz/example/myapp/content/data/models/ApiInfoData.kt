package kz.example.myapp.content.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApiInfoData(
    @JsonProperty("id")
    val id: Long?,
    @JsonProperty("time")
    val time: Long?,
    @JsonProperty("text")
    val text: String?,
    @JsonProperty("image")
    val image: String?
)