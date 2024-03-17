package com.example.spendings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spendings.R
import com.example.spendings.adapters.SpendingAdapter
import com.example.spendings.data.models.Spending
import com.example.spendings.data.models.SpendingWithProductName
import com.example.spendings.databinding.DialogAddSpendingBinding
import com.example.spendings.databinding.FragmentAddSpendingBinding
import com.example.spendings.ui.MainActivity
import com.example.spendings.ui.MainViewModel

class AddSpendingFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentAddSpendingBinding? = null
    private val binding get() = _binding!!
    private lateinit var spendingAdapter: SpendingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = (activity as MainActivity).viewModel
        _binding = FragmentAddSpendingBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {
            showAddDialog()
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

        setupRecyclerView()
        return binding.root
    }

    private fun setupAutoCompleteTextView(bindingDialog: DialogAddSpendingBinding) {
        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            val productNames = products.map { it.name }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                productNames
            )
            bindingDialog.autoCompleteTextView.setAdapter(adapter)
        }

        bindingDialog.autoCompleteTextView.threshold = 1 // Start searching from first character
        bindingDialog.autoCompleteTextView.setOnItemClickListener { adapterView, view, position, id ->
            val selectedName = adapterView.getItemAtPosition(position) as String
            viewModel.getProductByName(selectedName)?.let {
                bindingDialog.etUnitOfMeasurement.setText(it.defaultUnit)
            }
        }
    }

    private fun showAddDialog() {
        val bindingDialog = DialogAddSpendingBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext()).apply {
            setView(bindingDialog.root)
            setTitle("Custom Dialog")
            setPositiveButton("OK", null)
            setNegativeButton("Cancel", null)
        }.create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val productName = bindingDialog.autoCompleteTextView.text.trim().toString()
            val quantity = bindingDialog.etQuantity.text.trim().toString()
            val unit = bindingDialog.etUnitOfMeasurement.text.trim().toString()
            val moneySpent = bindingDialog.etMoneySpent.text.trim().toString()
            if (productName.isEmpty()) {
                bindingDialog.autoCompleteTextView.error = "This field cannot be empty"
                bindingDialog.autoCompleteTextView.requestFocus()
            } else if (quantity.isEmpty()) {
                bindingDialog.etQuantity.error = "This field cannot be empty"
                bindingDialog.etQuantity.requestFocus()
            } else if (unit.isEmpty()) {
                bindingDialog.etUnitOfMeasurement.error = "This field cannot be empty"
                bindingDialog.etUnitOfMeasurement.requestFocus()
            } else if (moneySpent.isEmpty()) {
                bindingDialog.etMoneySpent.error = "This field cannot be empty"
                bindingDialog.etMoneySpent.requestFocus()
            } else {
                val product = viewModel.getProductByName(productName)
                if (product == null) {
                    bindingDialog.autoCompleteTextView.error = "There is no such product"
                    bindingDialog.autoCompleteTextView.requestFocus()
                } else {
                    val newSpending = SpendingWithProductName(
                        productName = product.name,
                        productId = product.id,
                        moneySpent = moneySpent.toDouble(),
                        quantity = quantity.toDouble(),
                        unit = product.defaultUnit,
                        timestamp = System.currentTimeMillis(),
                    )
                    viewModel.addSpending(newSpending)
                    dialog.dismiss()
                }
            }
        }

        setupAutoCompleteTextView(bindingDialog)
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