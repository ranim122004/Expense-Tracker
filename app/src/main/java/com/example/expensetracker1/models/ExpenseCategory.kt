package com.example.expensetracker1.models

enum class ExpenseCategory(val displayName: String) {
    FOOD("Food"),
    TRANSPORT("Transport"),
    SHOPPING("Shopping"),
    OTHER("Other");

    companion object {
        fun fromDisplayName(name: String): ExpenseCategory {
            return values().firstOrNull { it.displayName == name } ?: OTHER
        }
    }
}
