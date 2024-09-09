package com.example.apitest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.apitest.R
import com.example.apitest.api.ApiClient
import com.example.apitest.api.ApiService
import com.example.apitest.databinding.ActivityFilmDetayiBinding
import com.example.apitest.response.FilmDetayResponse
import com.example.apitest.utils.Constants.POSTER_BASEURL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmDetayiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilmDetayiBinding
    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityFilmDetayiBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        val movieId=intent.getIntExtra("id",1)
        binding.apply {
            val callFilmDetayiActivity=api.getMovieDetails(movieId)
            callFilmDetayiActivity.enqueue(object : Callback<FilmDetayResponse>{
                override fun onResponse(
                    call: Call<FilmDetayResponse>,
                    response: Response<FilmDetayResponse>
                ) {
                    when(response.code()){
                        in 200..299 -> {
                            response.body().let{
                                    itBody->
                                val imagePoster=POSTER_BASEURL + itBody!!.posterPath
                                imgFilm.load(imagePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.baseline_movie_24)
                                    scale(coil.size.Scale.FILL)
                                }
                                imgBackground.load(imagePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.baseline_movie_24)
                                    scale(coil.size.Scale.FILL)
                                }
                                filmisim.text=itBody.title
                                tagline.text=itBody.tagline
                                relaseDate.text=itBody.releaseDate
                                puan.text=itBody.voteAverage.toString()
                                izlenme.text=itBody.runtime.toString()
                                butce.text=itBody.budget.toString()
                                hasilat.text=itBody.revenue.toString()
                                aciklama.text=itBody.overview

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

                override fun onFailure(call: Call<FilmDetayResponse>, t: Throwable) {
                    Log.e("onFailure", "Err : ${t.message}")
                }

            })

        }
        }
    }