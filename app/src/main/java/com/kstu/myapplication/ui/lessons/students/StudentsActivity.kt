package com.kstu.myapplication.ui.lessons.students

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.kstu.myapplication.R
import com.kstu.myapplication.model.StudentModel
import com.kstu.myapplication.ui.adapters.StudentAdapter
import com.kstu.myapplication.ui.api.NetworkService
import kotlinx.android.synthetic.main.activity_students.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentsActivity : AppCompatActivity(){
    var lextureId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)
        lextureId = intent.getIntExtra("id",0)
        initApiService()
        initActionBar()
    }

    private fun initActionBar() {
        supportActionBar?.title = "Студенты"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeState(studentModel: StudentModel) {
       if(studentModel.prisutstvie =="+"){
           studentModel.prisutstvie = "-"
       }else{
           studentModel.prisutstvie = "+"
       }
        updateStudent(studentModel)
    }

    private fun updateRecView(list: List<StudentModel>) {
        if (list.isEmpty()){
            text_if_empty.visibility = View.VISIBLE
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val studentAdapter = StudentAdapter()
        studentAdapter.updateData(list)
        with(rec_student) {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@StudentsActivity)
            addItemDecoration(divider)
        }
        studentAdapter.listener = {
            openDialog(it)
        }
        studentAdapter.checkedListener = {
            changeState(it)
        }
    }

    private fun openDialog(student: StudentModel) {
        val dialog = Dialog(this)
        dialog .setContentView(R.layout.note_dialog)
        val body = dialog.findViewById(R.id.student_note) as TextInputEditText
        body.setText(student.prim)
        val yesBtn = dialog .findViewById(R.id.btn_yes) as MaterialButton
        yesBtn.setOnClickListener {
            student.prim = body.text.toString()
            updateStudent(student)
            dialog .dismiss()
        }

        dialog .show()
    }

    private fun updateStudent(student: StudentModel) {
        NetworkService.instance
            .lextureApi
            .updateLecture(student.id,student)
            .enqueue(object: Callback<Boolean>{
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    showMessage("Could't get data")
                }

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.body()!=null) {
                        val res = response.body()!!
                        if (res) {
                            showMessage("Updated")
                        }
                    }
                }

            })
    }

    private fun showMessage(msg:String) {
        Toast.makeText(this@StudentsActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun initApiService(){
        NetworkService.instance
            .lextureApi
            .getStudentsInLecture(lextureId)
            .enqueue(object : Callback<List<StudentModel>> {
                override fun onFailure(call: Call<List<StudentModel>>, t: Throwable) {
                    Log.e("Fail","$t")
                }

                override fun onResponse(
                    call: Call<List<StudentModel>>,
                    response: Response<List<StudentModel>>
                ) {
                    if (response.body()!=null) {
                        text_if_empty.visibility = View.GONE
                        updateRecView(response.body()!!)
                    }


                }

            })
    }

}
