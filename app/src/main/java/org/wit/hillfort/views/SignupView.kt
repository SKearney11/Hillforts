package org.wit.hillfort.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class SignupView: AppCompatActivity() {

    private lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        app = application as MainApp

        SignupOk.setOnClickListener {
            val username = SignupUsername.text.toString()
            val password = SignupPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                toast("Please enter Username and Password")
                return@setOnClickListener
            }
            app.users.create(UserModel(username = username, password = password))
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

    }
}