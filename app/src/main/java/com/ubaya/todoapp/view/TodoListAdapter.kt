package com.ubaya.todoapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.todoapp.model.Todo
import com.ubaya.todoapp.databinding.TodoItemLayoutBinding


class TodoListAdapter(val todoList:ArrayList<Todo>,val adapterOnClick:(Todo)->Unit)
    :RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>(), TodoCheckedChangeListener, TodoEditClickListener {
    class TodoViewHolder(var binding: TodoItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int)
    {
//        holder.binding.checkTask.text = todoList[position].title
//        holder.binding.checkTask.isChecked = false
//        holder.binding.checkTask.setOnCheckedChangeListener {
//                compoundButton, b ->
//            if(compoundButton.isPressed) {
//                adapterOnClick(todoList[position])
//            }
//        }
//        holder.binding.imgEdit.setOnClickListener{
//            val action = TodoListFragmentDirections.actionEditTodo(todoList[position].uuid)
//            Navigation.findNavController(it).navigate(action)
//        }
        holder.binding.todo = todoList[position]
    //databinding bisa dipake kalo ui rame/banyakk
        holder.binding.listener = this//ini karena yang ditekan adalah dirinya sendiri
        holder.binding.editlistener = this

    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun updateTodoList(newTodoList:List<Todo>){
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }

    override fun onCheckedChanged(cb: CompoundButton, isChecked: Boolean, todo: Todo) {
        if(cb.isPressed){
            adapterOnClick(todo)
        }
    }

    override fun onTodoEditClick(v: View) {
        val uuid = v.tag.toString().toInt()
        val action  = TodoListFragmentDirections.actionEditTodo(uuid)
        Navigation.findNavController(v).navigate(action)
    }
}
