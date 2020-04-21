package com.example.goout.Activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.goout.R
import com.example.goout.ViewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var animatorSet: AnimatorSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)

        actionBar?.hide()
        viewModel = LoginViewModel()

        Handler().postDelayed({
            animateLoadingValue()
            viewModel.init()
            viewModel.fieldsPresent.value?.let {
                groupFields.visibility = it
            }

            runOnUiThread {
                loadingAnimation.pauseAnimation()
            }
        }, 4000)

        bindButtons()

    }

    private fun bindButtons() {
        btnConfirm.setOnClickListener {
            //            if(viewModel.validateFields(txtUsername.text,txtPassword.tex)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
//            }
//            else {
//                // RETURN ERRO
//            }
        }
    }

    private fun animateLoadingValue() {
        animatorSet = AnimatorSet().apply {
            val animations = mutableListOf<Animator>()

            animations.add(getStarterValueAnimator())

            playTogether(animations)
        }

        animatorSet.start()
    }

    private fun getStarterValueAnimator() =
        ValueAnimator.ofFloat(0f, resources.getDimension(R.dimen.pu_value_sold_animation))
            .apply {
                duration = 500L
                interpolator = LinearInterpolator()

                addUpdateListener {
                    loadingAnimation.y = it.animatedValue as Float
                }
            }

    override fun onDestroy() {
        super.onDestroy()
        if (::animatorSet.isInitialized && animatorSet.isStarted) {
            animatorSet.cancel()
        }
    }
}
