package com.example.apitest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.apitest.R
import com.example.apitest.databinding.ItemRowBinding
import com.example.apitest.response.FilmListResponse
import com.example.apitest.ui.FilmDetayiActivity
import com.example.apitest.utils.Constants.POSTER_BASEURL


class FilmAdapter: RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {
    private lateinit var binding: ItemRowBinding
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRowBinding.inflate(inflater, parent, false)
        context = parent.context
        return FilmViewHolder()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    inner class FilmViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilmListResponse.Results) {
            binding.apply {
                filmisim.text = item.title
                puanlamaitem.text=item.vote_average.toString()
                val filmPosterUrl=POSTER_BASEURL+item.poster_path
                imgFilm.load(filmPosterUrl){
                    crossfade(true)
                    placeholder(R.drawable.baseline_movie_24)
                    scale(coil.size.Scale.FILL)
                }
                dilitem.text=item.original_language
                tarihitem.text=item.release_date

                root.setOnClickListener {
                    val intent= Intent(context, FilmDetayiActivity::class.java)
                    intent.putExtra("id",item.id)
                    context.startActivity(intent)

                }
            }
        }

    }
    private val differCallback=object : DiffUtil.ItemCallback<FilmListResponse.Results>(){
        override fun areItemsTheSame(
            oldItem: FilmListResponse.Results,
            newItem: FilmListResponse.Results
        ): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FilmListResponse.Results,
            newItem: FilmListResponse.Results
        ): Boolean {
            return oldItem==newItem
        }
    }
    val differ= AsyncListDiffer(this,differCallback)


    }
