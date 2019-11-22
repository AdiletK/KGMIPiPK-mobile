package com.kstu.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import com.kstu.myapplication.R
import com.kstu.myapplication.model.CourseModel

import com.kstu.myapplication.ui.lessons.LessonListActivity
import com.kstu.myapplication.ui.lessons.NewLessonActivity
import com.kstu.myapplication.model.GroupNavigation
import com.kstu.myapplication.model.TeacherModel
import com.kstu.myapplication.ui.api.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    lateinit var teacherSpinner:Spinner
    lateinit var groupSpinner:Spinner
    lateinit var courseSpinner:Spinner
    lateinit var btnLessnons:MaterialButton
    lateinit var btnNewLessnon:MaterialButton
    lateinit var listGroups : List<GroupNavigation>
    lateinit var listTeachers : List<TeacherModel>
    lateinit var listCourses : List<CourseModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(root)

        initialData()
        initSpinners()
        initApiService()
        return root
    }

    private fun initialData() {
        listGroups = ArrayList()
        listGroups = listOf(GroupNavigation(-1, "Группа...", 1, null))
        listTeachers = ArrayList()
        listTeachers = listOf(TeacherModel(-1, "Преподаватель...", null, null, null, null, null))
        listCourses = ArrayList()
        listCourses = listOf(CourseModel(-1, "", "Курс...", null, null))
    }

    private fun initApiService() {
        NetworkService.instance
            .courseApi
            .getCourses()
            .enqueue(object : Callback<List<CourseModel>>{
                override fun onFailure(call: Call<List<CourseModel>>, t: Throwable) {
                    Toast.makeText(context,"Couldn't connect to server",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<CourseModel>>,
                    response: Response<List<CourseModel>>
                ) {
                    if (response.body()!=null) {
                        listCourses = response.body()!!
                        initSpinners()
                    }
                }

            })

        NetworkService.instance
            .teacherApi
            .getTeachers()
            .enqueue(object : Callback<List<TeacherModel>>{
                override fun onFailure(call: Call<List<TeacherModel>>, t: Throwable) {
                    Log.e("Fail",t.message+"")
                }

                override fun onResponse(
                    call: Call<List<TeacherModel>>,
                    response: Response<List<TeacherModel>>
                ) {
                    Log.e("Success",response.body()?.size.toString()  )
                    if (response.body()!=null) {
                        listTeachers = response.body()!!
                        initSpinners()
                    }
                }

            })

    }

    private fun initViews(root: View) {
        teacherSpinner = root.findViewById(R.id.teacher_spinner)
        groupSpinner = root.findViewById(R.id.group_spinner)
        btnLessnons = root.findViewById(R.id.btn_history_list)
        btnNewLessnon = root.findViewById(R.id.btn_new_lesson)
        courseSpinner = root.findViewById(R.id.course_spinner)

        btnLessnons.setOnClickListener {
            startActivity(startIntent("old"))
        }
        btnNewLessnon.setOnClickListener {
            startActivity(startIntent("new"))
        }
    }

    private fun startIntent(type:String):Intent {
        val intent : Intent = if (type == "new"){
            Intent(activity, NewLessonActivity::class.java)
        }else{
            Intent(activity, LessonListActivity::class.java)
        }
        if (teacherSpinner.isNotEmpty()  ) {
            val teacher = teacherSpinner.selectedItem as TeacherModel
            intent.putExtra("teacher", teacher.id)
        }
        if(groupSpinner.isNotEmpty()){
            val group = groupSpinner.selectedItem as GroupNavigation
            intent.putExtra("group", group.code)
        }
        return intent
    }

    private fun initSpinners() {
        val courseAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, listCourses)
        val teacherAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, listTeachers)
        val groupAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, listGroups)
        groupSpinner.adapter = groupAdapter
        teacherSpinner.adapter = teacherAdapter
        courseSpinner.adapter = courseAdapter


        courseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("Selected listener ","$position  $id")
                val selectedItem =  parent?.getItemAtPosition(position) as CourseModel
                if (selectedItem.groups!=null) {
                    updateGroupSpinner(selectedItem.groups)
                }else{updateGroupSpinner(listGroups)}
            }

        }
    }
    private fun updateGroupSpinner( list:List<GroupNavigation>){
        val groupAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, list)
        groupSpinner.adapter = groupAdapter
    }

}