package com.otgs.customerapp.activities

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.otgs.customerapp.R
import com.otgs.customerapp.utils.Utils.setImageToImageView
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initDataBindings()

    }


    private fun initDataBindings() {
        setImageToImageView(this, s2bgImageView, R.drawable.star_bg)
    }

    fun changeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //Here you can get the size!

        if (!isRunning) {
            isRunning = true

            iconImageView.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(0)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {

                    }

                    override fun onAnimationEnd(animator: Animator) {

                        iconImageView.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(1500)
                            .setListener(object : Animator.AnimatorListener {
                                override fun onAnimationStart(animator: Animator) {

                                }

                                override fun onAnimationEnd(animator: Animator) {
                                    changeActivity()
                                }

                                override fun onAnimationCancel(animator: Animator) {

                                }

                                override fun onAnimationRepeat(animator: Animator) {

                                }
                            }).start()

                    }

                    override fun onAnimationCancel(animator: Animator) {

                    }

                    override fun onAnimationRepeat(animator: Animator) {

                    }
                }).start()

        }
    }


}

