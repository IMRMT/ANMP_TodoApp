package com.ubaya.todoapp.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.todoapp.Model.Todo
import com.ubaya.todoapp.databinding.TodoItemLayoutBinding


class TodoListAdapter(val todoList:ArrayList<Todo>,val adapterOnClick:(Todo)->Unit)
    :RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    class TodoViewHolder(var binding: TodoItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int)
    {
        holder.binding.checkTask.text = todoList[position].title
        holder.binding.checkTask.isChecked = false
        holder.binding.checkTask.setOnCheckedChangeListener {
                compoundButton, b ->
            if(compoundButton.isPressed) {
                adapterOnClick(todoList[position])
            }
        }
        holder.binding.imgEdit.setOnClickListener{
            val action = TodoListFragmentDirections.actionEditTodo(todoList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun updateTodoList(newTodoList:List<Todo>){
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }
}