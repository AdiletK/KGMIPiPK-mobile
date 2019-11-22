package com.kstu.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kstu.myapplication.R
import com.kstu.myapplication.model.LectureModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_lesson.view.*


class LectureAdapter(val listener: (LectureModel)->Unit): RecyclerView.Adapter<LectureAdapter.LectureViewHolder>() {
    var items: List<LectureModel> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val convertView = inflater.inflate(R.layout.item_lesson,parent,false)
        return LectureViewHolder(convertView)
    }

    fun updateData(data : List<LectureModel>){

        val diffCallback =object : DiffUtil.Callback(){
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].Nom == data[newPos].Nom

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

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) = holder.bind(items[position],listener)


    inner class LectureViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
        LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(lecture: LectureModel, listener: (LectureModel) -> Unit){
            itemView.item_lesson_data.text = lecture.Day
            val group = "${lecture.groupNav.courseNavigation!!.fullName} ${lecture.groupNav.grup}"
            itemView.item_lesson_group.text =group
            itemView.item_lesson_theme.text = lecture.themeNav.name
            itemView.setOnClickListener {
                listener.invoke(lecture)
            }
        }
    }
}