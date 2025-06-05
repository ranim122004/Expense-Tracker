package com.example.expensetracker1.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetracker1.R
import com.example.expensetracker1.data.Expense
import com.example.expensetracker1.data.ExpenseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var dateButton: Button
    private lateinit var saveButton: Button

    private var selectedDate: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Initialize views
        amountEditText = findViewById(R.id.et_amount)
        categorySpinner = findViewById(R.id.spinner_category)
        dateButton = findViewById(R.id.btn_date)
        saveButton = findViewById(R.id.btn_save)

        // Spinner items
        val categories = listOf("Food", "Transport", "Shopping", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        categorySpinner.adapter = adapter

        // Display current date
        updateDateButtonText(selectedDate)

        // Show DatePicker
        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate }
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    selectedDate = selectedCalendar.timeInMillis
                    updateDateButtonText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Save expense to DB
        saveButton.setOnClickListener {
            val amountText = amountEditText.text.toString()
            val category = categorySpinner.selectedItem?.toString() ?: ""

            if (amountText.isNotBlank()) {
                val amount = amountText.toDoubleOrNull()
                if (amount != null) {
                    val expense = Expense(
                        amount = amount,
                        category = category,
                        date = selectedDate
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        ExpenseDatabase.getDatabase(applicationContext).expenseDao().insert(expense)
                        runOnUiThread { finish() }
                    }
                } else {
                    Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Amount is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateDateButtonText(timestamp: Long) {
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        dateButton.text = "$day/$month/$year"
    }
}
