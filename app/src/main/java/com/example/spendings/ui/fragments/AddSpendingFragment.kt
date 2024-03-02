package com.example.spendings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendings.R
import com.example.spendings.adapters.SpendingAdapter
import com.example.spendings.data.models.Product
import com.example.spendings.data.models.Spending
import com.example.spendings.data.models.SpendingWithProductName
import com.example.spendings.databinding.FragmentAddSpendingBinding
import com.example.spendings.ui.MainActivity
import com.example.spendings.ui.MainViewModel

class AddSpendingFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentAddSpendingBinding? = null
    private val binding get() = _binding!!
    private lateinit var spendingAdapter: SpendingAdapter
    private var selectedProduct: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = (activity as MainActivity).viewModel
        _binding = FragmentAddSpendingBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {
            val moneySpent = binding.etMoneySpent.text.toString()
            val quantity = binding.etQuantity.text.toString()
            if (moneySpent.isBlank() || quantity.isBlank()) {
                Toast.makeText(context, "Fill the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            selectedProduct?.let { product ->
                val newSpending = SpendingWithProductName(
                    id = -1, // unused, will be generated
                    productName = product.name,
                    productId = product.id,
                    moneySpent = moneySpent.toDouble(),
                    quantity = quantity.toDouble(),
                    unit = product.defaultUnit,
                    timestamp = System.currentTimeMillis(),
                )
                viewModel.addSpending(newSpending)
                clearInput()
            }
        }

        binding.btnSubmit.setOnClickListener {
            val spendingListWithProductName = viewModel.spendingList.value ?: emptyList()
            val timestamp = System.currentTimeMillis()
            val spendingList: List<Spending> = spendingListWithProductName.map {
                Spending(
                    productId = it.productId,
                    moneySpent = it.moneySpent,
                    quantity = it.quantity,
                    unit = it.unit,
                    timestamp = timestamp
                )
            }
            viewModel.addSpendingList(spendingList)
            viewModel.clearSpendingList()
            findNavController().navigate(R.id.action_nav_add_spending_to_nav_spending)
        }

        setupAutoCompleteTextView()
        setupRecyclerView()
        return binding.root
    }

    private fun clearInput() {
        selectedProduct = null
        binding.tvUnit.text = "???"
        binding.etQuantity.text.clear()
        binding.autoCompleteTextView.text.clear()
        binding.etMoneySpent.text.clear()
    }

    private fun setupAutoCompleteTextView() {
        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            val productNames = products.map { it.name }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                productNames
            )
            binding.autoCompleteTextView.setAdapter(adapter)
        }

        binding.autoCompleteTextView.threshold = 1 // Start searching from first character
        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, position, id ->
            val selectedName = adapterView.getItemAtPosition(position) as String

            val products = viewModel.allProducts.value ?: emptyList()
            selectedProduct = products.find { it.name == selectedName }
            binding.tvUnit.text = selectedProduct?.defaultUnit?.unit ?: "???"
        }
    }

    private fun setupRecyclerView() {
        spendingAdapter = SpendingAdapter()
//        spendingAdapter.setOnItemClickListener {
//        }
        binding.rvSpendings.apply {
            adapter = spendingAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.spendingList.observe(viewLifecycleOwner) {
            spendingAdapter.differ.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}