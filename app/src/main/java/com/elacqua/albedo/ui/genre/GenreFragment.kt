package com.elacqua.albedo.ui.genre

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.elacqua.albedo.AlbedoApp
import com.elacqua.albedo.R
import com.elacqua.albedo.data.remote.jikan_api.model.Anime
import com.elacqua.albedo.data.remote.jikan_api.model.Manga
import com.elacqua.albedo.ui.OnAnimeSelectedListener
import com.elacqua.albedo.ui.OnMangaSelectedListener
import kotlinx.android.synthetic.main.genre_fragment.*
import javax.inject.Inject

class GenreFragment : Fragment() {

    @Inject lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: GenreViewModel by viewModels{ vmFactory }
    private lateinit var adapter: GenreRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        val type = arguments?.get("genreType") as Int
        val genreId = arguments?.get("genreId") ?: 0
        viewModel.getItemsInGenre(type, genreId as Int)

        initObservers()
    }

    private fun initRecyclerView() {
        adapter = GenreRecyclerAdapter(
            object : OnAnimeSelectedListener {
                override fun onClick(anime: Anime) {
                    val args = bundleOf("animeId" to anime.malId)
                    findNavController()
                        .navigate(R.id.action_genreFragment_to_animeDetailFragment, args)
                }
            },
            object : OnMangaSelectedListener {
                override fun onClick(manga: Manga) {
                    val args = bundleOf("mangaId" to manga.malId)
                    findNavController()
                        .navigate(R.id.action_genreFragment_to_mangaDetailFragment, args)
                }
            }
        )
        val glm = GridLayoutManager(requireContext(), 2)
        recycler_genre.adapter = adapter
        recycler_genre.layoutManager = glm
    }

    private fun initObservers() {
        observeAnimeItems()
        observeMangaItems()
    }

    private fun observeAnimeItems() {
        viewModel.animeItems.observe(viewLifecycleOwner, {
            txt_genre_title.text = it.malUrl.name
            adapter.addAnimeList(it.anime)
        })
    }

    private fun observeMangaItems() {
        viewModel.mangaItems.observe(viewLifecycleOwner, {
            txt_genre_title.text = it.malUrl.name
            adapter.addMangaList(it.manga)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.genre_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AlbedoApp).appComponent.inject(this)
    }
}