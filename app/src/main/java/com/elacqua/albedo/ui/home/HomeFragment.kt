package com.elacqua.albedo.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.elacqua.albedo.AlbedoApp
import com.elacqua.albedo.R
import com.elacqua.albedo.ui.OnAnimeSelectedListener
import com.elacqua.albedo.ui.OnMangaSelectedListener
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val homeViewModel: HomeViewModel by viewModels { vmFactory }
    private lateinit var adapter: HomeRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_home.layoutManager = LinearLayoutManager(requireContext())
        recycler_home.setHasFixedSize(true)
        adapter = HomeRecyclerAdapter(
            object : OnAnimeSelectedListener {
                override fun onClick(animeId: Int) {
                    val args = bundleOf(getString(R.string.key_anime_id) to animeId)
                    findNavController()
                        .navigate(R.id.action_navigation_home_to_animeDetailFragment, args)
                }
            },
            object : OnMangaSelectedListener {
                override fun onClick(mangaId: Int) {
                    val args = bundleOf(getString(R.string.key_manga_id) to mangaId)
                    findNavController()
                        .navigate(R.id.action_navigation_home_to_mangaDetailFragment, args)
                }
            },
            object : OnGenreSelected {
                override fun onGenreClick(type: Int) {
                    val args = bundleOf(getString(R.string.key_genre_type) to type)
                    findNavController()
                        .navigate(R.id.action_navigation_home_to_genreFragment, args)
                }
            })
    }

    private fun initObservers() {
        homeViewModel.airingAnime.observe(viewLifecycleOwner, {
            adapter.setAirings(it.results)
        })

        homeViewModel.upcomingAnime.observe(viewLifecycleOwner, {
            adapter.setUpcomings(it.results)
        })

        homeViewModel.topMovies.observe(viewLifecycleOwner, {
            adapter.setMovies(it.results)
        })

        homeViewModel.topManga.observe(viewLifecycleOwner, {
            adapter.setManga(it.results)
        })

        homeViewModel.topNovels.observe(viewLifecycleOwner, {
            adapter.setNovels(it.results)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragment_home_menu_anime -> {
                findNavController().navigate(R.id.action_navigation_home_to_animeFragment)
            }
            R.id.fragment_home_menu_manga -> {
                findNavController().navigate(R.id.action_navigation_home_to_mangaFragment)
            }
        }
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AlbedoApp).appComponent.inject(this)
    }

    override fun onStart() {
        recycler_home.adapter = adapter
        super.onStart()
    }

    override fun onPause() {
        recycler_home.adapter = null
        super.onPause()
    }
}