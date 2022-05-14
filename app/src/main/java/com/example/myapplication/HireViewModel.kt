package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val milestones = MutableLiveData<ArrayList<HireMilestones>>()

    fun loadMilestones() {
        milestones.value = repository.getRegions()
    }

    fun addMilestones(milestones: List<HireMilestones>) {
        val list = arrayListOf<HireMilestones>()
        milestones.let {
            list.addAll(it)
            list.add(0, HireMilestones(milestones.size.toString()))
        }
        this.isAdd.value = true
        this.milestones.value = list
    }

    fun deleteMilestones(milestones: List<HireMilestones>, hireMilestones: HireMilestones) {
        val list = arrayListOf<HireMilestones>()
        list.addAll(milestones)
        list.remove(hireMilestones)
        this.isAdd.value = false
        this.milestones.value = list
    }
}
