package com.ikki.dicodingevent.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikki.dicodingevent.R
import com.ikki.dicodingevent.data.DetailViewModelFactory
import com.ikki.dicodingevent.data.di.Injection
import com.ikki.dicodingevent.ui.DetailViewModel
import com.ikki.dicodingevent.ui.FavouriteAdapter

class FavouriteFragment : Fragment() {

    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = Injection.provideRepository(requireContext())
        val factory = DetailViewModelFactory(repository)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        favouriteAdapter = FavouriteAdapter()

        progressBar = view.findViewById(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerFavouriteEvents)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favouriteAdapter

        val emptyStateTextView = view.findViewById<TextView>(R.id.emptyStateText)

        detailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        detailViewModel.getAllBookmarkedEvents().observe(viewLifecycleOwner) { bookmarkedEvents ->
            if (bookmarkedEvents.isNullOrEmpty()) {
                emptyStateTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyStateTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                favouriteAdapter.submitList(bookmarkedEvents)
            }
            progressBar.visibility = View.GONE
        }
    }
}