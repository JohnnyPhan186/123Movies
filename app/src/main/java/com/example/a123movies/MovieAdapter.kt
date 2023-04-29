package com.example.a123movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private val movieList : List<List<String>>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val moviePoster : ImageView
            val movieTitle : TextView
            val movieOverview : TextView
            val movieRating : TextView

            init {
                moviePoster = view.findViewById(R.id.movie_poster)
                movieTitle = view.findViewById(R.id.movie_title)
                movieOverview = view.findViewById(R.id.movie_overview)
                movieRating = view.findViewById(R.id.movie_rating)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(movieList[position][3])
            .centerCrop()
            .into(holder.moviePoster)

        holder.movieTitle.text = movieList[position][0]
        holder.movieRating.text = "Rating: " + movieList[position][1]
        holder.movieOverview.text = movieList[position][2]
    }
}