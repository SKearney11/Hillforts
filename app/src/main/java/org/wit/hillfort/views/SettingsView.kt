package org.wit.hillfort.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
                    v = 0
                    app.currentUser.password = newPassword.text.toString()
                    app.users.update(app.currentUser.copy())
                    setResult(RESULT_OK)
                    finish()
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        v = 0
    }
}