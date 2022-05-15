package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * @author Perry Lance
 * @since 2021-09-17 Created
 */
@HiltViewModel
class HireViewModel @Inject constructor(
    private val repository: HireRepository
) : ViewModel() {

    val isAdd = MutableLiveData<Boolean>()
    val milestones = MutableLiveData<List<HireMilestones>>()
    private val itemId = AtomicInteger(0)

    fun loadMilestones() {
        milestones.value = repository.getRegions()
    }

    fun addMilestones() {
        val list = mutableListOf<HireMilestones>()
        list.add(0, HireMilestones(itemId.incrementAndGet().toString()))
        list.addAll(milestones.value.orEmpty())
        isAdd.value = true
        milestones.value = list
    }

    fun deleteMilestones(hireMilestones: HireMilestones) {
        val list = mutableListOf<HireMilestones>()
        list.addAll(milestones.value.orEmpty())
        list.remove(hireMilestones)
        isAdd.value = false
        milestones.value = list
    }
}
