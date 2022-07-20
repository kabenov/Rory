package kz.example.myapp.content.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieApiData(
    @JsonProperty("id")
    val id: Long?,
    @JsonProperty("title")
    val title: String?,
    @JsonProperty("poster_path")
    val poster_path: String?
)