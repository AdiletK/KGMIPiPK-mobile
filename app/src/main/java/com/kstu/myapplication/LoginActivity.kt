package com.kstu.myapplication

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*

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
                app_name.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
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
            checkLogin()
        }
    }

    private fun checkLogin() {
        val name  = login.text
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("login",name)
        startActivity(intent)
        finish()
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
