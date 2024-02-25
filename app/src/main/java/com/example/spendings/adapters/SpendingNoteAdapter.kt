package com.example.spendings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.spendings.data.models.SpendingNote
import com.example.spendings.databinding.ItemSpendingNoteBinding
import java.text.SimpleDateFormat
import java.util.Date

class SpendingNoteAdapter : RecyclerView.Adapter<SpendingNoteAdapter.NewViewHolder>(){

    inner class NewViewHolder(val binding: ItemSpendingNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<SpendingNote>() {
        override fun areItemsTheSame(oldItem: SpendingNote, newItem: SpendingNote): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: SpendingNote, newItem: SpendingNote): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val binding = ItemSpendingNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((SpendingNote) -> Unit)? = null

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val current = differ.currentList[position]
        val binding = holder.binding
        holder.itemView.apply {
            binding.tvMoneySpent.text = current.moneySpent.toString()
            binding.tvTimestamp.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(
                Date(current.timestamp)
            )

            binding.root.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (SpendingNote) -> Unit) {
        onItemClickListener = listener
    }
}