package com.example.spendings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendings.data.models.Product
import com.example.spendings.adapters.ProductAdapter
import com.example.spendings.databinding.FragmentProductsBinding
import com.example.spendings.ui.MainActivity
import com.example.spendings.ui.MainViewModel

class ProductsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = (activity as MainActivity).viewModel
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        productAdapter = ProductAdapter()

        binding.btnAdd.setOnClickListener {
            val productName = binding.etProductName.text.toString()
            if (productName.isBlank()) {
                return@setOnClickListener
            }
            val newProduct = Product(name = productName)
            viewModel.createProduct(newProduct)
            binding.etProductName.text.clear()
        }

        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter()
        productAdapter.setOnItemClickListener {
            viewModel.deleteProduct(it)
        }
        binding.rvProducts.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.allProducts.observe(viewLifecycleOwner) {
            productAdapter.differ.submitList(it)
            binding.tvNoProducts.isVisible = it.isEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}