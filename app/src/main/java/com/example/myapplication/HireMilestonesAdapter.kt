package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
                days.doAfterTextChanged {
                    checkDays(context, binding, absoluteAdapterPosition)
                }
                if (item.validData.not()) checkData()
            }
        }

        fun checkData(): Boolean {
            var validData = false
            checkDays(context, binding, absoluteAdapterPosition)
            run breaking@{
                currentList.forEachIndexed { index, model ->
                    if (model.days == null) {
                        validData = false
                        model.validData = false
                        return@breaking
                    } else {
                        validData = true
                        model.validData = true
                    }
                }
            }
            return validData
        }
    }

    fun checkDays(
        context: Context,
        binding: ItemHireMilestonesBinding,
        position: Int
    ): Boolean {
        val text = binding.days.text
        currentList[position].days = null
        return if (text.isNullOrBlank()) {
            binding.textInputLayoutDays.error = context.getString(R.string.please_enter_days)
            false
        } else {
            currentList[position].days = text.toString()
            binding.textInputLayoutDays.isErrorEnabled = false
            true
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
