package com.kstu.myapplication.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.kstu.myapplication.R
import com.kstu.myapplication.model.StudentModel
import kotlinx.android.extensions.LayoutContainer


class StudentAdapter
    : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    var listener: ((StudentModel) -> Unit)? = null
    var checkedListener:((StudentModel)->Unit)?=null

    var items: List<StudentModel> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.item_student,parent,false)
        return StudentViewHolder(convertView)
    }

    fun updateData(data : List<StudentModel>){

        val diffCallback =object : DiffUtil.Callback(){
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos : Int, newPos : Int): Boolean = items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        items = data
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) = holder.bind(items[position])


    inner class StudentViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        @SuppressLint("SetTextI18n")
        fun bind(student: StudentModel){
            itemView.findViewById<TextView>(R.id.student_name).text = student.Name
            itemView.findViewById<CheckBox>(R.id.student_presence).isChecked = student.prisutstvie== "+"

            itemView.findViewById<CheckBox>(R.id.student_presence).setOnCheckedChangeListener { _, isChecked ->
                checkedListener?.invoke(student)
            }
            itemView.findViewById<MaterialButton>(R.id.student_note).setOnClickListener {
                listener?.invoke(student)
            }
        }
    }
}