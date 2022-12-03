package com.example.myapplication

/**
 * @author Perry Lance
 * @since 2021-09-17 Created
 */
data class HireMilestones(
    val id: String,
    var days: String? = null,
    var title: String? = null,
    var amount: String? = null,
    var isError: Boolean = false
)
