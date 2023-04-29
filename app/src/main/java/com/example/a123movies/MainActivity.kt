package com.example.a123movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private lateinit var rvMovies : RecyclerView

    val sortings = arrayOf("Rating", "No Sort")
    var movies = arrayListOf<List<String>>()
    lateinit var sortedMovies : List<List<String>>

    lateinit var autoCompleteTextView: AutoCompleteTextView
    lateinit var adapterItems: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMovies = findViewById(R.id.movie_list)

        getUpcomingMovies()

        autoCompleteTextView = findViewById(R.id.auto_complete_txt)
        adapterItems = ArrayAdapter(this, R.layout.list_item, sortings)

        autoCompleteTextView.setAdapter(adapterItems)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                var item = adapterView.getItemAtPosition(i).toString()
                if(item == "Rating"){
                    val adapter = MovieAdapter(sortedMovies)
                    rvMovies.adapter = adapter
                    rvMovies.layoutManager = LinearLayoutManager(this@MainActivity)
                } else{
                    val adapter = MovieAdapter(movies)
                    rvMovies.adapter = adapter
                    rvMovies.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
    }

    /**
     * Gets upcoming movies from themoviedb api
     * Also sorts them by rating and stores them in a separate lists
     */
    private fun getUpcomingMovies() {
        val client = AsyncHttpClient()

        client["https://api.themoviedb.org/3/movie/upcoming?api_key=aa31cc8651e1fab5b5d6cfc66ab73b32&language=en-US&page=1", object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String,
                throwable: Throwable?
            ) {
                Log.d("Movie error", response)
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                var movieArray = json?.jsonObject?.getJSONArray("results")
                if (movieArray != null) {
                    for (i in 0 until movieArray.length()) {
                        movies.add(
                            listOf(
                                movieArray.getJSONObject(i)?.getString("title").toString(),
                                movieArray.getJSONObject(i)?.getString("vote_average").toString(),
                                movieArray.getJSONObject(i)?.getString("overview").toString(),
                                "https://image.tmdb.org/t/p/w500" +
                                movieArray.getJSONObject(i)?.getString("poster_path").toString()
                            )
                        )
                    }
                }
                sortedMovies = movies.sortedWith(compareByRating)
                val adapter = MovieAdapter(movies)
                rvMovies.adapter = adapter
                rvMovies.layoutManager = LinearLayoutManager(this@MainActivity)
                Log.d("Movie", "$sortedMovies")
            }

        }]
    }

    public fun onItemClick(adapterView: AdapterView<ArrayAdapter<String>>, view: View, i: Int, l: Long){

    }

    //Custom comparator to sort movies by rating
    val compareByRating = fun(a: List<String>, b: List<String>): Int{
        return if(b[1].toFloat() > a[1].toFloat())
            1
        else if(b[1].toFloat() == a[1].toFloat())
            0
        else
            -1
    }
}