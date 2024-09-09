package com.example.apitest.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apitest.adapter.FilmAdapter
import com.example.apitest.api.ApiClient
import com.example.apitest.api.ApiService
import com.example.apitest.databinding.ActivityMainBinding
import com.example.apitest.response.FilmListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val moviesAdapter by lazy { FilmAdapter() }
    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.prgBarFilm.visibility = View.VISIBLE
        val callMoviesApi = api.getPopularMovies(1)
        callMoviesApi.enqueue(object : Callback<FilmListResponse> {
            override fun onResponse(call: Call<FilmListResponse>, response: Response<FilmListResponse>) {
                binding.prgBarFilm.visibility = View.GONE
                when (response.code()) {
                    in 200..299 -> {
                        Log.d("Response Code", " success messages : ${response.code()}")
                        response.body()?.let { itBody ->
                            itBody.results.let { itData ->
                                if (itData.isNotEmpty()) {
                                    moviesAdapter.differ.submitList(itData)
                                    binding.rvFilm.apply {
                                        layoutManager = LinearLayoutManager(this@MainActivity)
                                        adapter = moviesAdapter
                                    }
                                }
                            }
                        }
                    }
                    in 300..399 -> {
                        Log.d("Response Code", " Redirection messages : ${response.code()}")
                    }
                    in 400..499 -> {
                        Log.d("Response Code", " Client error responses : ${response.code()}")
                    }
                    in 500..599 -> {
                        Log.d("Response Code", " Server error responses : ${response.code()}")
                    }
                }
            }
            override fun onFailure(call: Call<FilmListResponse>, t: Throwable) {
                binding.prgBarFilm.visibility = View.GONE
                Log.e("onFailure", "Err : ${t.message}")
            }
        })
    }
}