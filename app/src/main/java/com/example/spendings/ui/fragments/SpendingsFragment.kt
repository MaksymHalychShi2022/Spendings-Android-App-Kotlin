package com.example.spendings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendings.R
import com.example.spendings.adapters.SpendingAdapter
import com.example.spendings.adapters.SpendingNoteAdapter
import com.example.spendings.databinding.FragmentSpendingsBinding
import com.example.spendings.ui.MainActivity
import com.example.spendings.ui.MainViewModel

class SpendingsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentSpendingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var spendingNoteAdapter: SpendingNoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = (activity as MainActivity).viewModel
        _binding = FragmentSpendingsBinding.inflate(inflater, container, false)

        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        spendingNoteAdapter = SpendingNoteAdapter()
        spendingNoteAdapter.setOnItemClickListener {
            findNavController().navigate(
                SpendingsFragmentDirections.actionNavSpendingsToNavSpendingList(
                    it.timestamp
                )
            )
        }
        binding.rvSpendings.apply {
            adapter = spendingNoteAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.allSpendingNotes.observe(viewLifecycleOwner) {
            spendingNoteAdapter.differ.submitList(it)
            binding.tvNoSpendings.isVisible = it.isEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}