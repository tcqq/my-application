package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
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

    override fun getItemViewType(position: Int): Int {
        return getItem(position).id.hashCode()
    }

    inner class ViewHolder(
        private val binding: ItemHireMilestonesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var context: Context

        fun bind(
            item: HireMilestones,
            onDeleteClick: (HireMilestones, Int) -> Unit
        ) {
            context = itemView.context
            with(binding) {
                item.days?.let { days.setText(it) }
                delete.setOnClickListener {
                    onDeleteClick(item, absoluteAdapterPosition)
                }
                if(!item.daysError.isNullOrEmpty()){
                    binding.textInputLayoutDays.error = context.getString(R.string.please_enter_days)
                }
                days.doAfterTextChanged {
                    if(checkDays(context, binding, absoluteAdapterPosition)){
                        item.days = days.text.toString()
                    }
                }
            }
        }

        fun checkData(): Boolean {
            return checkDays(context, binding, absoluteAdapterPosition)
        }
    }

    fun checkDays(
        context: Context,
        binding: ItemHireMilestonesBinding,
        position: Int
    ): Boolean {
        val text = binding.days.text
        if (text.isNullOrBlank()) {
            binding.textInputLayoutDays.error = context.getString(R.string.please_enter_days)
            return false
        }
        return try {
            val days = Integer.parseInt(text.toString())
            if (text.length > 1 && text.startsWith("0")) {
                binding.days.text?.replace(0, 1, "")
                false
            } else if (days < WORK_DAYS_MIN) {
                binding.textInputLayoutDays.error =
                    context.getString(
                        R.string.days_cannot_be_less_than,
                        WORK_DAYS_MIN
                    )
                false
            } else if (days > WORK_DAYS_MAX) {
                binding.textInputLayoutDays.error =
                    context.getString(
                        R.string.days_cannot_be_greater_than,
                        WORK_DAYS_MAX
                    )
                false
            } else {
                currentList[position].days = text.toString()
                binding.textInputLayoutDays.isErrorEnabled = false
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
