package org.wit.hillfort.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.hillfortlist.HillfortListView

class LoginView: AppCompatActivity() {

    private lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        app = application as MainApp

        LoginButton.setOnClickListener {
            val username = LoginUsername.text.toString()
            val password = LoginPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()){
                toast("Enter username and password")
                return@setOnClickListener
            }

            val foundUser : UserModel? = app.users.findAll().find{ u -> u.username == username }
            if (foundUser != null && foundUser.password == password)
            {
                LoginUsername.setText("")
                LoginPassword.setText("")
                app.currentUser = foundUser
                startActivityForResult(intentFor<HillfortListView>(), 0)
            } else {
                toast("Incorrect username or Password")
            }
        }

        LoginSignup.setOnClickListener {
            startActivityForResult(intentFor<SignupView>(), 0)
        }

    }
}