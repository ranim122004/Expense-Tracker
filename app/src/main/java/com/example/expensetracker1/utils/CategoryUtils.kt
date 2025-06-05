package com.example.expensetracker1.utils

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.expensetracker1.R
import com.example.expensetracker1.models.ExpenseCategory

object CategoryUtils {

    @DrawableRes
    fun getCategoryIcon(category: String): Int {
        return when (category) {
            "Food" -> R.drawable.ic_food // Replace with your own icon resources
            "Transport" -> R.drawable.ic_transport
            "Shopping" -> R.drawable.ic_shopping
            else -> R.drawable.ic_other
        }
    }

    @ColorInt
    fun getCategoryColor(context: Context, category: String): Int {
        return when (category) {
            "Food" -> ContextCompat.getColor(context, R.color.category_food)
            "Transport" -> ContextCompat.getColor(context, R.color.category_transport)
            "Shopping" -> ContextCompat.getColor(context, R.color.category_shopping)
            else -> ContextCompat.getColor(context, R.color.category_other)
        }
    }
}
