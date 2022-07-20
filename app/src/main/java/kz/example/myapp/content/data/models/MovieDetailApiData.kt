package kz.example.myapp.content.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieDetailApiData(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("backdrop_path")
    val backdropPath: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("genres")
    val genres: List<String>,
    @JsonProperty("tagline")
    val tagline: String,
    @JsonProperty("overview")
    val overview: String
)
