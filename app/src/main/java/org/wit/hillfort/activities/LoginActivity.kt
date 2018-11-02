package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class LoginActivity: AppCompatActivity() {

    private lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        app = application as MainApp

        LoginButton.setOnClickListener {
            val username = LoginUsername.text.toString()
            val password = LoginPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) return@setOnClickListener

            val foundUser : UserModel? = app.users.findAll().find{ u -> u.username == username }

            if (foundUser != null && foundUser.password == password) startActivityForResult(intentFor<HillfortListActivity>(), 0)
        }

        LoginSignup.setOnClickListener {
            startActivityForResult(intentFor<SignupActivity>(), 0)
        }

    }
}