package com.ikki.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ikki.dicodingevent.databinding.FragmentHomeBinding
import com.ikki.dicodingevent.ui.EventAdapter
import com.ikki.dicodingevent.ui.EventCarouselAdapter
import com.ikki.dicodingevent.ui.FinishedEventAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var upcomingEventAdapter: EventAdapter
    private lateinit var carouselAdapter: EventCarouselAdapter
    private lateinit var finishedEventAdapter: FinishedEventAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setupRecyclerView()
        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { finishedEventList ->
            finishedEventAdapter.submitList(finishedEventList)
        }
        observeEventData()
        observeErrorMessages()

        return binding.root
    }

    private fun setupRecyclerView() {
        finishedEventAdapter = FinishedEventAdapter()
        binding.recyclerViewFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedEventAdapter
        }
    }

    private fun observeEventData() {
        binding.progressBar.visibility = View.VISIBLE
        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { finishedEventList ->
            if (finishedEventList.isNotEmpty()) {
                finishedEventAdapter.submitList(finishedEventList)
                binding.recyclerViewFinishedEvents.visibility = View.VISIBLE
            } else {
                binding.recyclerViewFinishedEvents.visibility = View.GONE
            }
            binding.progressBar.visibility = View.GONE
        }

        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { upcomingEventList ->
            if (upcomingEventList.isNotEmpty()) {
                val currentTime = System.currentTimeMillis()

                val upcomingEvents = upcomingEventList.filter { event ->
                    event.beginTime.toMillis() > currentTime
                }

                carouselAdapter = EventCarouselAdapter(upcomingEvents)
                binding.viewPagerUpcomingEvents.adapter = carouselAdapter
                binding.viewPagerUpcomingEvents.visibility = View.VISIBLE
            } else {
                binding.viewPagerUpcomingEvents.visibility = View.GONE
            }
        }
    }

    private fun observeErrorMessages() {
        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun String.toMillis(): Long {

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.parse(this)?.time ?: 0L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}