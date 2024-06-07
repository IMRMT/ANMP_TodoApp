package com.ubaya.todoapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
import java.util.concurrent.TimeUnit

class CreateTodoFragment : Fragment(), RadioClickListener, TodoEditClickListener {
    private lateinit var binding:FragmentCreateTodoBinding
    private lateinit var viewModel:DetailTodoViewModel
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
            binding.todo=Todo("","",3,0)
            binding.radioListener = this
            binding.addListener = this


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
        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo created",
                    "message" to "Stay focus"
                )
            )
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)

        val list = binding.todo
        viewModel.addTodo(list)
        Toast.makeText(context, "Todo Created", Toast.LENGTH_LONG).show()
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }
}