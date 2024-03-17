package com.example.spendings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendings.data.models.Product
import com.example.spendings.adapters.ProductAdapter
import com.example.spendings.databinding.DialogAddProductBinding
import com.example.spendings.databinding.FragmentProductsBinding
import com.example.spendings.ui.MainActivity
import com.example.spendings.ui.MainViewModel
import com.example.spendings.utils.Resource

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
            val bindingDialog = DialogAddProductBinding.inflate(layoutInflater)

            val dialog = AlertDialog.Builder(requireContext()).apply {
                setView(bindingDialog.root)
                setTitle("Custom Dialog")
                setPositiveButton("OK", null)
                setNegativeButton("Cancel", null)
            }.create()

            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val productName = bindingDialog.etProductName.text.toString().trim()
                val defaultUnit = bindingDialog.etDefaultUnit.text.toString().trim()
                if (productName.isNotEmpty() && defaultUnit.isNotEmpty()) {
                    val newProduct = Product(
                        name = productName,
                        defaultUnit = defaultUnit
                    )
                    viewModel.createProduct(newProduct).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Resource.Success -> {
                                (activity as MainActivity).hideProgressBar()
                                dialog.dismiss()
                            }

                            is Resource.Error -> {
                                (activity as MainActivity).hideProgressBar()
                                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                if (result.message.equals("Product with such name already exist")) {
                                    bindingDialog.etProductName.error =
                                        "Product with such name already exist"
                                    bindingDialog.etProductName.requestFocus()
                                }
                            }

                            is Resource.Loading -> {
                                (activity as MainActivity).showProgressBar()
                            }
                        }
                    }
                }
                if (productName.isEmpty()) {
                    bindingDialog.etProductName.error = "This field cannot be empty"
                    bindingDialog.etProductName.requestFocus()
                }
                if (defaultUnit.isEmpty()) {
                    bindingDialog.etDefaultUnit.error = "This field cannot be empty"
                    bindingDialog.etDefaultUnit.requestFocus()
                }
            }
        }

        setupRecyclerView()
        return binding.root
    }

    private fun addProduct(product: Product) {

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