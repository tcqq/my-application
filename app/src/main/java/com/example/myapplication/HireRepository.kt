package com.example.myapplication

import javax.inject.Inject

/**
 * @author Perry Lance
 * @since 2021-09-17 Created
 */
class HireRepository @Inject constructor() {

    fun getRegions(): ArrayList<HireMilestones> {
        val items = arrayListOf<HireMilestones>()
        items.add(HireMilestones("1", false, false))
        return items
    }
}
