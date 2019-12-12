package com.kstu.myapplication

import android.animation.Animator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.kstu.myapplication.model.AuthResultModel
import com.kstu.myapplication.ui.api.NetworkService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View.GONE
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.progress_dialog.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                app_name.visibility = GONE
                loadingProgressBar.visibility = GONE
                rootView.setBackgroundColor(ContextCompat.getColor(this@LoginActivity, R.color.colorSplashText))
                app_icon.setImageResource(R.drawable.ic_ac_unit_black_24dp)
                startAnimation()
            }

            override fun onTick(p0: Long) {}
        }.start()

        initViews()
    }

    private fun initViews() {
        loginButton.setOnClickListener {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, HIDE_NOT_ALWAYS)

            llProgressBar.visibility = VISIBLE
            txt_new_lesson.visibility = GONE
            checkLogin()
        }
    }

    private fun checkLogin() {
        if (login.text?.trim()!!.isEmpty() || password.text?.trim()!!.isEmpty()){
            Toast.makeText(this,"Заполните поля!",Toast.LENGTH_SHORT).show()
            llProgressBar.visibility = GONE
        }else {
            signIn(login.text?.trim().toString(),password.text?.trim().toString())
        }
    }

    private fun signIn(login: String, psw: String) {
        NetworkService.instance
            .login
            .login(login,psw)
            .enqueue(object : Callback<AuthResultModel> {
                override fun onFailure(call: Call<AuthResultModel>, t: Throwable) {
                    Log.e("fail"," ${t.localizedMessage}")
                    Toast.makeText(this@LoginActivity,"Проверьте подключение к интернету.",Toast.LENGTH_SHORT).show()
                    llProgressBar.visibility = GONE
                }

                override fun onResponse(call: Call<AuthResultModel>, response: Response<AuthResultModel>) {
                    if (response.isSuccessful){
                        if (response.body()!=null)
                            openMainPage(response.body()!!)
                    }else{
                        Log.e("Body","Unsec")
                        Toast.makeText(this@LoginActivity,"Не правильно логин/пароль.",Toast.LENGTH_SHORT).show()
                    }
                    llProgressBar.visibility  = GONE
                }

            })
    }


    private fun openMainPage(mod:AuthResultModel){
        val intent = Intent(this, MainActivity::class.java)
        saveData(mod)
        startActivity(intent)
        finish()
    }

    fun saveData(mod:AuthResultModel) {
//        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
//        with(sharedPref.edit()) {
//            putString(getString(R.string.secret_token),mod.token)
//            putInt(getString(R.string.depart_id),mod.dep_id)
//            commit()
//        }
        val setting = applicationContext.getSharedPreferences("Test", 0)
        val editor = setting.edit()
        editor.putString(getString(R.string.secret_token), mod.token)
        editor.putInt(getString(R.string.depart_id),mod.dep_id)
        editor.putString(getString(R.string.depart_name),mod.department)
// Apply the edits!
        editor.apply()

// Get from the SharedPreferences

    }
    private fun startAnimation() {
        app_icon.animate().apply {
            x(50f)
            y(100f)
            duration = 1000
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                afterAnimationLayout.visibility = VISIBLE
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })
    }

}
