package org.wit.hillfort.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp

class SettingsView: AppCompatActivity() {

    private lateinit var app: MainApp
    var v = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        app = application as MainApp
        async(UI) {
            var s = app.hillforts.findAll().size
            for(i in app.hillforts.findAll()){
                if (i.visitedCheckbox == true){
                    v++
                }
            }

            numOfHillforts.setText("Number of entries: " + s)
            numVisited.setText("Amount visited: " + v)

            saveButton2.setOnClickListener {
                if (newPassword.text.isNotEmpty()){
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.updatePassword(newPassword.text.toString())?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(app.applicationContext, "User Password Updated", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(app.applicationContext, "Please Enter a Valid Password", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        v = 0
    }
}