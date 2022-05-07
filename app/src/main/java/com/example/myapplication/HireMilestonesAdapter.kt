package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity.Companion.WORK_DAYS_MAX
import com.example.myapplication.MainActivity.Companion.WORK_DAYS_MIN
import com.example.myapplication.databinding.ItemHireMilestonesBinding

/**
 * @author Perry Lance
 * @since 2021-09-17 Created
 */
class HireMilestonesAdapter(
    private val onDeleteClick: (HireMilestones, Int) -> Unit
) : ListAdapter<HireMilestones, HireMilestonesAdapter.ViewHolder>(HireMilestonesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemHireMilestonesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick)
    }

    private lateinit var context: Context
    private lateinit var mBinding: ItemHireMilestonesBinding

    inner class ViewHolder(
        private val binding: ItemHireMilestonesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            mBinding = binding
        }

        fun bind(
            item: HireMilestones,
            onDeleteClick: (HireMilestones, Int) -> Unit
        ) {
            context = itemView.context

            if (MainActivity.isNextClicked){
                if (!item.isDays){
                    if (!item.isError){
                        if(binding.editTextDays.text.isNullOrBlank()){
                            binding.textInputLayoutDays.error = context.getString(R.string.please_enter_days)
                        }
                    }
                }
            }
            with(binding) {
                delete.setOnClickListener {
                    onDeleteClick(item, absoluteAdapterPosition)
                }
                editTextDays.doOnTextChanged { text, _, _, _ ->
                    checkDays(context, this, text, position)
                }
            }
        }
    }

    private fun checkDays(
        context: Context,
        binding: ItemHireMilestonesBinding,
        text: CharSequence?,
        position: Int
    ): Boolean {
        if (text.isNullOrBlank()) {
            binding.textInputLayoutDays.error = context.getString(R.string.please_enter_days)
            currentList.get(position).isDays = false
            currentList.get(position).isError = true
            return false
        }
        return try {
            val days = Integer.parseInt(text.toString())
            if (text.length > 1 && text.startsWith("0")) {
                binding.editTextDays.text?.replace(0, 1, "")
                currentList.get(position).isDays = false
                currentList.get(position).isError = true
                false
            } else if (days < WORK_DAYS_MIN) {
                binding.textInputLayoutDays.error =
                    context.getString(
                        R.string.days_cannot_be_less_than,
                        WORK_DAYS_MIN
                    )
                currentList.get(position).isDays = false
                currentList.get(position).isError = true
                false
            } else if (days > WORK_DAYS_MAX) {
                binding.textInputLayoutDays.error =
                    context.getString(
                        R.string.days_cannot_be_greater_than,
                        WORK_DAYS_MAX
                    )
                currentList.get(position).isDays = false
                currentList.get(position).isError = true
                false
            } else {
                currentList.get(position).isDays = true
                binding.textInputLayoutDays.isErrorEnabled = false
                currentList.get(position).isError = false
                true
            }
        } catch (nfe: NumberFormatException) {
            println(nfe)
            false
        }
    }

    class HireMilestonesDiffCallback : DiffUtil.ItemCallback<HireMilestones>() {

        override fun areItemsTheSame(oldItem: HireMilestones, newItem: HireMilestones): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HireMilestones, newItem: HireMilestones): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
