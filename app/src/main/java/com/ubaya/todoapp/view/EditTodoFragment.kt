package com.ubaya.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ubaya.todoapp.R
import com.ubaya.todoapp.model.Todo
import com.ubaya.todoapp.viewModel.DetailTodoViewModel
import com.ubaya.todoapp.databinding.FragmentCreateTodoBinding
import com.ubaya.todoapp.databinding.FragmentEditTodoBinding

class EditTodoFragment : Fragment(), RadioClickListener, TodoEditClickListener {
    private lateinit var binding: FragmentEditTodoBinding
    private lateinit var viewModel: DetailTodoViewModel
    private lateinit var todo:Todo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        binding.txtJudulTodo.text = "edit Todo"
        val uuid = EditTodoFragmentArgs.fromBundle(requireArguments()).uuid

        viewModel.fetch(uuid)
//        binding.btnSubmit.setOnClickListener {
//            todo.title = binding.txtTitle.text.toString()
//            todo.notes = binding.txtNotes.text.toString()
//            val radio = view.findViewById<RadioButton>(
//                binding.radioGroupPriority.checkedRadioButtonId)
//            todo.priority = radio.tag.toString().toInt()
//            viewModel.update(todo)
//            Toast.makeText(context, "Todo Updated", Toast.LENGTH_SHORT).show()
//        }
        binding.radiolistener = this
        binding.submitlistener = this

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.todoLD.observe(viewLifecycleOwner,{
            binding.todo = it//todo bawaan dari variable
        })
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }

    override fun onTodoEditClick(v: View) {
        viewModel.update(binding.todo!!)
        Toast.makeText(context,"Todo Updated", Toast.LENGTH_SHORT).show()
    }
}