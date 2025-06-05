package com.example.expensetracker1.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker1.R
import com.example.expensetracker1.data.Expense
import com.example.expensetracker1.data.ExpenseDatabase
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var pieChart: PieChart
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var totalAmountText: TextView
    private lateinit var spinnerMonth: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pieChart = findViewById(R.id.pie_chart)
        recyclerView = findViewById(R.id.recycler_view)
        fabAdd = findViewById(R.id.fab_add)
        totalAmountText = findViewById(R.id.tvTotalAmount)
        spinnerMonth = findViewById(R.id.spinnerMonth)

        expenseAdapter = ExpenseAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = expenseAdapter

        val months = arrayOf(
            "All", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        spinnerMonth.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)

        ExpenseDatabase.getDatabase(this).expenseDao().getAllExpenses().observe(this, Observer { expenses ->
            val selectedMonth = spinnerMonth.selectedItemPosition
            val filteredExpenses = if (selectedMonth == 0) {
                expenses
            } else {
                expenses.filter { expense ->
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = expense.date
                    }
                    calendar.get(Calendar.MONTH) + 1 == selectedMonth
                }
            }

            expenseAdapter.updateList(filteredExpenses)
            updateTotalAmount(filteredExpenses)
            updatePieChart(filteredExpenses)
        })

        fabAdd.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateTotalAmount(expenses: List<Expense>) {
        val total = expenses.sumOf { it.amount }
        totalAmountText.text = "Total: $%.2f".format(total)
    }

    private fun updatePieChart(expenses: List<Expense>) {
        if (expenses.isEmpty()) {
            pieChart.clear()
            pieChart.invalidate()
            return
        }

        val categoryTotals = expenses.groupBy { it.category }
            .mapValues { it.value.sumOf { e -> e.amount } }

        val entries = categoryTotals.map { (category, total) ->
            PieEntry(total.toFloat(), category)
        }

        val dataSet = PieDataSet(entries, "Expenses by Category").apply {
            colors = listOf(
                Color.parseColor("#FFA500"),
                Color.parseColor("#00BFFF"),
                Color.parseColor("#FF69B4"),
                Color.parseColor("#A9A9A9")
            )
            valueTextColor = Color.WHITE
            valueTextSize = 14f
        }

        pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            setUsePercentValues(true)
            isDrawHoleEnabled = false
            setEntryLabelColor(Color.BLACK)
            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
            invalidate()
        }
    }
}
