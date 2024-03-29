package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<HireViewModel>()
    private lateinit var milestonesAdapter: HireMilestonesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        milestonesAdapter = HireMilestonesAdapter { hireMilestones, position ->
            viewModel.deleteMilestones(hireMilestones)
        }
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = milestonesAdapter
        }
        viewModel.loadMilestones()

        binding.addMilestone.setOnClickListener {
            viewModel.addMilestones()
        }

        observe(viewModel.milestones) {
            milestonesAdapter.submitList(it) {
                if (viewModel.isAdd.value == true) {
                    binding.recyclerView.smoothScrollToPosition(0)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hire_milestones, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_next -> {
                if (checkData()) {
                    Toast.makeText(this, "OPEN", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkData(): Boolean {
        milestonesAdapter.currentList.indices.forEach { position ->
            binding.recyclerView.findViewHolderForAdapterPosition(position)?.let {
                with(it as HireMilestonesAdapter.ViewHolder) {
                    if (checkData().not()) {
                        milestonesAdapter.notifyDataSetChanged()
                        return false
                    }
                }
            }
        }
        return true
    }
}