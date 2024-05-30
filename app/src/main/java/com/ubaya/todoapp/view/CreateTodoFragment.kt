package com.ubaya.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ubaya.todoapp.model.Todo
import com.ubaya.todoapp.viewModel.DetailTodoViewModel
import com.ubaya.todoapp.databinding.FragmentCreateTodoBinding

class CreateTodoFragment : Fragment() {
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
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        binding.btnSubmit.setOnClickListener {
            val radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
            val todo = Todo(binding.txtTitle.text.toString(),
                binding.txtNotes.text.toString(),
                radio.tag.toString().toInt())
            viewModel.addTodo(todo)
            Toast.makeText(context, "Todo Created", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()
        }
    }
}