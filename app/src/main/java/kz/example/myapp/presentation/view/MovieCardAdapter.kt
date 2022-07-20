package kz.example.myapp.presentation.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.example.myapp.R
import kz.example.myapp.content.data.models.MovieApiData

class MovieCardAdapter(onMovieListener: OnMovieListener) : RecyclerView.Adapter<MovieCardViewHolder>() {

    private val dataList: MutableList<MovieApiData> = mutableListOf()
    private val onMovieListener: OnMovieListener = onMovieListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardViewHolder {
        return MovieCardViewHolder(View.inflate(parent.context, R.layout.movie_card, null), onMovieListener)
    }

    override fun onBindViewHolder(holder: MovieCardViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(movieApiDataList: List<MovieApiData>){
        dataList.clear()
        dataList.addAll(movieApiDataList)
        notifyDataSetChanged()
    }
}