package com.example.expensetracker1.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker1.R
import com.example.expensetracker1.data.Expense
import com.example.expensetracker1.utils.CategoryUtils
import com.example.expensetracker1.utils.DateUtils

class ExpenseAdapter(private var expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amountText: TextView = itemView.findViewById(R.id.tv_amount)
        val categoryText: TextView = itemView.findViewById(R.id.tv_category)
        val dateText: TextView = itemView.findViewById(R.id.tv_date)
        val iconImage: ImageView = itemView.findViewById(R.id.iv_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]

        holder.amountText.text = "$%.2f".format(expense.amount)
        holder.categoryText.text = expense.category

        // Use shared DateUtils for formatting
        holder.dateText.text = DateUtils.formatDate(expense.date)

        // set category icon
        holder.iconImage.setImageResource(CategoryUtils.getCategoryIcon(expense.category))

        // Optionally set text color by category
        val color = CategoryUtils.getCategoryColor(holder.itemView.context, expense.category)
        holder.amountText.setTextColor(color)
    }

    override fun getItemCount(): Int = expenses.size

    fun updateList(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}
