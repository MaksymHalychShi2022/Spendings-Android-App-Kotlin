package com.example.spendings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.spendings.data.models.SpendingWithProductName
import com.example.spendings.databinding.ItemSpendingBinding
import java.text.SimpleDateFormat
import java.util.Date

class SpendingAdapter : RecyclerView.Adapter<SpendingAdapter.NewViewHolder>(){

    inner class NewViewHolder(val binding: ItemSpendingBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<SpendingWithProductName>() {
        override fun areItemsTheSame(oldItem: SpendingWithProductName, newItem: SpendingWithProductName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SpendingWithProductName, newItem: SpendingWithProductName): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val binding = ItemSpendingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((SpendingWithProductName) -> Unit)? = null

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val current = differ.currentList[position]
        val binding = holder.binding
        holder.itemView.apply {
            binding.tvProduct.text = current.productName
            binding.tvMoneySpent.text = current.moneySpent.toString()
            binding.tvTimestamp.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(
                Date(current.timestamp)
            )

            binding.root.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (SpendingWithProductName) -> Unit) {
        onItemClickListener = listener
    }
}