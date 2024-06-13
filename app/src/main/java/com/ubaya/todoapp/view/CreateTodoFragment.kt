package com.ubaya.todoapp.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ubaya.todoapp.model.Todo
import com.ubaya.todoapp.viewModel.DetailTodoViewModel
import com.ubaya.todoapp.databinding.FragmentCreateTodoBinding
import com.ubaya.todoapp.util.NotificationHelper
import com.ubaya.todoapp.util.TodoWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CreateTodoFragment : Fragment(), RadioClickListener, TodoEditClickListener, DateClickListener, TimeClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding:FragmentCreateTodoBinding
    private lateinit var viewModel:DetailTodoViewModel

    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateTodoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),NotificationHelper.REQUEST_NOTIF)
            }



//        binding.btnSubmit.setOnClickListener {
//            val notif = NotificationHelper(view.context)
//            notif.createNotification("Todo Created", "Stay focus!")
//
//            val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
//                .setInitialDelay(30, TimeUnit.SECONDS)
//                .setInputData(
//                    workDataOf(
//                        "title" to "Todo created",
//                        "message" to "Stay focus"
//                    )
//                )
//                .build()
//            WorkManager.getInstance(requireContext()).enqueue(workRequest)
//
//
//            val radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//            val todo = Todo(binding.txtTitle.text.toString(),
//                binding.txtNotes.text.toString(),
//                radio.tag.toString().toInt())
//            viewModel.addTodo(todo)
//            Toast.makeText(context, "Todo Created", Toast.LENGTH_LONG).show()
//            Navigation.findNavController(it).popBackStack()}

            viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
            binding.todo=Todo("","",3,0, 0)
            binding.radioListener = this
            binding.addListener = this
            binding.datelistener = this
            binding.timelistener = this

    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,
                                            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode ==NotificationHelper.REQUEST_NOTIF) {
            if(grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                val notif = NotificationHelper(requireContext())
                notif.createNotification("Todo Created", "Stay focus!")
            }
        }
    }

    override fun onTodoEditClick(v: View) {
        val today = Calendar.getInstance()
        val c = Calendar.getInstance()
        c.set(year, month, day, hour, minute,0)

        val delay = (c.timeInMillis/1000L) - (today.timeInMillis/1000L)
//        binding.todo.let {
//            it.todo_date = (c.timeInMillis/1000L).toInt()
//        }
        binding.todo!!.todo_date = (c.timeInMillis/1000L).toInt()


        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo created",
                    "message" to "Stay focus"
                )
            )
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)

        viewModel.addTodo(binding.todo!!)
        Toast.makeText(context, "Todo Created", Toast.LENGTH_LONG).show()
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }

    override fun onDateClick(v: View) {
        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(),this, y,m,d).show()
    }

    override fun onTimeClick(v: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Calendar.getInstance().let {
            it.set(year, month, dayOfMonth)
            binding.txtDate.setText(
                day.toString().padStart(2, '0')+"-"+
                        (month+1).toString().padStart(2,'0')+"-"+ year)
            this.year=year
            this.month=month
            this.day=dayOfMonth
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        binding.txtTime.setText(
            hourOfDay.toString().padStart(2, '0')+":"+
            minute.toString().padStart(2,'0')
        )
        this.hour = hourOfDay
        this.minute = minute
    }
}