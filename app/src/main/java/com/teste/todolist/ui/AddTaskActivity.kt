package com.teste.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.teste.todolist.databinding.ActivityAddTaskBinding
import com.teste.todolist.datasource.TaskDataSource
import com.teste.todolist.extensions.format
import com.teste.todolist.extensions.text
import com.teste.todolist.model.Task
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding:ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.txtInputTitle.text = it.title
                binding.txtInputDesc.text = it.description
                binding.txtInputData.text = it.date
                binding.txtInputHour.text = it.hour
            }
        }

        insertListeners()

    }

    private fun insertListeners() {
        binding.txtInputData.editText?.setOnClickListener{
            val datePicker =  MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
               val timeZone = TimeZone.getDefault()
               val offset =  timeZone.getOffset(Date().time) * -1
               binding.txtInputData.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
        binding.txtInputHour.editText?.setOnClickListener{
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener{
               val minute = if (timePicker.minute in 0..9)"0${timePicker.minute}" else timePicker.minute
               val hour =  if (timePicker.hour in 0..9)"0${timePicker.hour}" else timePicker.hour


                binding.txtInputHour.text = "${hour}:${minute}"

            }
            timePicker.show(supportFragmentManager,null)

        }
        binding.btnCalcel.setOnClickListener{
            finish()
        }
        binding.btnNewTask.setOnClickListener{
            val task = Task(
                title = binding.txtInputTitle.text,
                description = binding.txtInputDesc.text,
                date = binding.txtInputData.text,
                hour = binding.txtInputHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
    companion object {
        const val TASK_ID = "TASK_id"
    }

}