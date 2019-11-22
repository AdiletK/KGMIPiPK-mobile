package com.kstu.myapplication.ui.lessons

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.kstu.myapplication.model.*
import com.kstu.myapplication.ui.api.NetworkService
import com.kstu.myapplication.ui.lessons.students.StudentsActivity
import kotlinx.android.synthetic.main.activity_new_lesson.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*
import android.widget.ArrayAdapter
import com.kstu.myapplication.R


class NewLessonActivity : AppCompatActivity() {

    var isEditMode = false
    var lextureId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_lesson)
        val data = intent.getSerializableExtra("lecture")
        initSetup(data)
        initViews()
    }

    private fun initSetup(data: Serializable?) {
        if (data != null) {
            supportActionBar?.title = "Занятие"
            data as LectureModel
            lextureId = data.Nom
            isEditMode = true
            editLecture(data)
        } else {
            supportActionBar?.title = "Новое занятие"
            initApiService()
            newLecture()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initApiService() {
        NetworkService.instance
            .courseApi
            .getCourses()
            .enqueue(object : Callback<List<CourseModel>>{
                override fun onFailure(call: Call<List<CourseModel>>, t: Throwable) {
                    Toast.makeText(this@NewLessonActivity,"Couldn't connect to server",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<CourseModel>>,
                    response: Response<List<CourseModel>>
                ) {
                    if (response.body()!=null)
                    updateCourseSpin(response.body()!!)
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
                    if (response.body()!=null)
                    updateTeacherSpin(response.body()!!)

                }

            })
        NetworkService.instance
            .themeApi
            .getThemes()
            .enqueue(object :Callback<List<ThemeNavigation>>{
                override fun onFailure(call: Call<List<ThemeNavigation>>, t: Throwable) {
                    Log.e("Fail",t.message+"")
                }

                override fun onResponse(
                    call: Call<List<ThemeNavigation>>,
                    response: Response<List<ThemeNavigation>>
                ) {
                    if (response.body()!=null)
                    updateThemeSpin(response.body()!!)
                }
            })
        NetworkService.instance
            .typeOfLessonApi
            .getTypesOfLesson()
            .enqueue(object :Callback<List<TypeOfLesson>>{
                override fun onFailure(call: Call<List<TypeOfLesson>>, t: Throwable) {
                    Log.e("Fail",""+t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<List<TypeOfLesson>>,
                    response: Response<List<TypeOfLesson>>
                ) {
                    if (response.body()!=null)
                    updateTypeSpin(response.body()!!)
                }

            })
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {

        btn_form.setOnClickListener {
            if (isEditMode){
                openStudentActivity(lextureId)
            }else{
                insertLecture()
            }
        }

        btn_date_picker.setOnClickListener {
            showDatePicker()
        }

        spin_course.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.e("Selected listener ", "$position  $id")
                val selectedItem = parent?.getItemAtPosition(position) as CourseModel
                if (selectedItem.groups != null) {
                    updateGroupSpin(selectedItem.groups)
                }
            }
        }
        }

    private fun newLecture() {
        val date = initDate()
        val textDate ="${date.first}-${date.second+1}-${date.third}"
        btn_date_picker.text = textDate
    }

    private fun insertLecture() {
        loadingProgressBar.visibility = View.VISIBLE
        val lectureModel = getLectureData()
        if (lectureModel!=null){
            NetworkService.instance
                .lextureApi
                .insertLecture(lectureModel)
                .enqueue(object : Callback<Int>{
                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Log.e("Fail",""+t.message)
                    }

                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        if(response.body()!=null) {
                            val id = response.body()
                            Log.e("ID"," $id")
                            openStudentActivity(id!!)
                        }
                    }
                })
        }else{
            Toast.makeText(this@NewLessonActivity,"Выберите все поля!",Toast.LENGTH_LONG).show()
            hideProgressBar()
        }

    }

    private fun getLectureData(): InsertLectureDataModel? {
        val theme = spin_theme.selectedItem as ThemeNavigation
        val teacher = spin_teacher.selectedItem as TeacherModel
        val type = spin_type.selectedItem as TypeOfLesson
        return if (theme.nom<=0||teacher.id<=0||edt_count.text.toString().isEmpty()){
            null
        } else{
            val group = spin_group.selectedItem as GroupNavigation
            InsertLectureDataModel(
                btn_date_picker.text.toString(),
                group.code,
                theme.nom,
                teacher.id,
                edt_count.text.toString().toByte(),
                type.nom
            )
        }
    }

    private fun openStudentActivity(id: Int) {
        //startActivity(Intent(this, StudentsActivity::class.java))
        val intent = Intent(this,StudentsActivity::class.java)
        intent.putExtra("id",id)
        startActivity(intent)
        hideProgressBar()
        finish()
    }

    private fun hideProgressBar() {
        loadingProgressBar.visibility = View.GONE
    }

    private fun showDatePicker() {
        val c = initDate()

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val date = "$year-${month+1}-$day"
            btn_date_picker.text = date
        }, c.first, c.second, c.third)

        dpd.show()
    }
    private fun initDate():Triple<Int,Int,Int>{
        val c = Calendar.getInstance()
        return Triple(
        c.get(Calendar.YEAR),
        c.get(Calendar.MONTH),
        c.get(Calendar.DAY_OF_MONTH))
    }

    private fun updateCourseSpin(list:List<CourseModel>){
        val courseAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        spin_course.adapter = courseAdapter
    }
    private fun updateGroupSpin(list:List<GroupNavigation>){
        val groupAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        spin_group.adapter = groupAdapter
    }
    private fun updateThemeSpin(list:List<ThemeNavigation>){
        val themeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        spin_theme.adapter = themeAdapter
    }
    private fun updateTeacherSpin(list:List<TeacherModel>){
        val teacherAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        spin_teacher.adapter = teacherAdapter
    }
    private fun updateTypeSpin(list:List<TypeOfLesson>){
        val typeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        spin_type.adapter = typeAdapter
    }

    private fun editLecture(data: Serializable?) {
        data as LectureModel
        val course = data.groupNav.courseNavigation
        updateCourseSpin(listOf(CourseModel(course!!.id,course.name,course.fullName,course.plan,null)))
        updateGroupSpin(listOf(data.groupNav))
        updateTypeSpin(listOf(data.typeOfLesson))
        updateTeacherSpin(listOf(data.teacher))
        updateThemeSpin(listOf(data.themeNav))
        edt_count.setText(data.Hours)
        edt_count.isEnabled = false
        edt_count.setTextColor(resources.getColor(android.R.color.black,theme))
        btn_date_picker.text = data.Day
        btn_form.text = "Отметить"
    }
}
