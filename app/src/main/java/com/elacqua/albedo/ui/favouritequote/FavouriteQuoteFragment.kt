package com.elacqua.albedo.ui.favouritequote

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elacqua.albedo.AlbedoApp
import com.elacqua.albedo.R
import com.elacqua.albedo.data.local.model.FavouriteQuote
import com.elacqua.albedo.ui.ViewModelFactory
import kotlinx.android.synthetic.main.favourite_quote_fragment.*
import timber.log.Timber
import javax.inject.Inject

class FavouriteQuoteFragment : Fragment() {

    @Inject
    lateinit var vmFactory: ViewModelFactory
    private val viewModel: FavouriteQuoteViewModel by viewModels { vmFactory }
    private lateinit var rvAdapter: FavouriteQuoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initQuotesObserver()
    }

    private fun initRecyclerView() {
        initAdapter()
        recycler_favourite_quote.run {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initAdapter() {
        rvAdapter = FavouriteQuoteAdapter(
            object: OnFavouriteQuoteClickListener{
                override fun onClick(quote: FavouriteQuote) {
                    viewModel.removeQuote(quote)
                }
            }
        )
    }

    private fun initQuotesObserver() {
        viewModel.quotes.observe(viewLifecycleOwner, {
            rvAdapter.setQuotes(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.favourite_quote_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AlbedoApp).appComponent.inject(this)
    }

    override fun onStart() {
        recycler_favourite_quote.adapter = rvAdapter
        super.onStart()
    }

    override fun onStop() {
        recycler_favourite_quote.adapter = null
        super.onStop()
    }
}