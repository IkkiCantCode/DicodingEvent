package com.ikki.dicodingevent.ui.notifications

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
import com.ikki.dicodingevent.databinding.FragmentNotificationsBinding
import com.ikki.dicodingevent.ui.FinishedEventAdapter

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: FinishedEventAdapter
    private lateinit var notificationsViewModel: NotificationsViewModel
    private var eventList: List<ListEventsItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        setupRecyclerView()
        setupSearchView()
        notificationsViewModel.finishedEvents.observe(viewLifecycleOwner) { finishedEventList ->
            eventList = finishedEventList
            eventAdapter.submitList(finishedEventList)
        }

        observeEventData()

        return binding.root
    }

    private fun setupRecyclerView() {
        eventAdapter = FinishedEventAdapter()
        binding.recyclerViewFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
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
        binding.progressBar.visibility = View.VISIBLE
        notificationsViewModel.finishedEvents.observe(viewLifecycleOwner) { finishedEventList ->
            binding.progressBar.visibility = View.GONE
            eventAdapter.submitList(finishedEventList)
        }
        notificationsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        notificationsViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                notificationsViewModel.clearErrorMessage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}