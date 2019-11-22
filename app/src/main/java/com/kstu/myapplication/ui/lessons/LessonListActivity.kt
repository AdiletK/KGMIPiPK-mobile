package com.kstu.myapplication.ui.lessons

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.kstu.myapplication.R
import com.kstu.myapplication.model.LectureModel
import com.kstu.myapplication.ui.adapters.LectureAdapter
import com.kstu.myapplication.ui.api.NetworkService
import kotlinx.android.synthetic.main.activity_lessons.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LessonListActivity : AppCompatActivity() {
    var group:Int = 0
    var teacher:Short = 0

    lateinit var lectureAdapter:LectureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lessons)
        supportActionBar?.title = "Список"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        getData()
        initViews()
        listOfLextures()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getData() {
        group = intent.getIntExtra("group",0)
        teacher = intent.getShortExtra("teacher",0)
        Toast.makeText(this, " $group $teacher",Toast.LENGTH_LONG).show()
    }

    private fun initViews() {
        lectureAdapter = LectureAdapter{
            openActivity(it)
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)


        with(rec_lesson){
            adapter = lectureAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@LessonListActivity)
            addItemDecoration(divider)
        }
    }

    private fun openActivity(lectureModel: LectureModel) {
        val intent = Intent(this,NewLessonActivity::class.java)
        intent.putExtra("lecture",lectureModel)
        startActivity(intent)
    }

    private fun listOfLextures() {
        NetworkService
            .instance
            .lextureApi
            .getLextures(group,teacher)
            .enqueue(object : Callback<List<LectureModel>> {
                override fun onFailure(call: Call<List<LectureModel>>, t: Throwable) {
                    Log.e("Fail retrofit",""+t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<List<LectureModel>>,
                    response: Response<List<LectureModel>>
                ) {
                    if(response.body()!!.isEmpty()){
                        text_if_empty.visibility = View.VISIBLE
                    }else{
                        text_if_empty.visibility = View.GONE
                        lectureAdapter.updateData(response.body()!!)
                    }
                }
            })

    }

}
