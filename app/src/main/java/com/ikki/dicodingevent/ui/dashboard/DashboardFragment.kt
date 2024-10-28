package com.ikki.dicodingevent.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ikki.dicodingevent.data.response.ListEventsItem
import com.ikki.dicodingevent.databinding.FragmentDashboardBinding
import com.ikki.dicodingevent.ui.EventAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private lateinit var dashboardViewModel: DashboardViewModel
    private var eventList: List<ListEventsItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        setupRecyclerView()
        setupSearchView()
        dashboardViewModel.upcomingEvents.observe(viewLifecycleOwner) { upcomingEventList ->
            eventList = upcomingEventList
            eventAdapter.submitList(upcomingEventList)
        }
        observeEventData()

        return binding.root
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter()
        binding.recyclerViewUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterEvents(newText ?: "")
                return true
            }
        })
    }

    private fun filterEvents(query: String) {
        val filteredList = if (query.isEmpty()) {
            eventList
        } else {
            eventList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        eventAdapter.submitList(filteredList)
    }

    private fun observeEventData() {
        dashboardViewModel.upcomingEvents.observe(viewLifecycleOwner) { upcomingEventList ->
            eventAdapter.submitList(upcomingEventList)
        }

        dashboardViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        dashboardViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                dashboardViewModel.clearErrorMessage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}