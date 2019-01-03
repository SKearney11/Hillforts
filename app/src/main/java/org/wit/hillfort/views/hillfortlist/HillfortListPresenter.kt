package org.wit.hillfort.views.hillfortlist

import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.views.SettingsView
import org.wit.hillfort.views.hillfort.HillfortView
import kotlinx.coroutines.experimental.async
import org.wit.hillfort.views.VIEW

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {

    init{
        app = view.application as MainApp
    }

    fun getHillforts(){
            async(UI){
                view!!.showHillforts(app.hillforts.findAll())
            }
    }

    fun getHillforts(searchString: String){
        async(UI){
            view!!.showHillforts(app.hillforts.findAll().filter { it.title.contains(searchString) })
        }
    }

    fun doAddHillfort(){
        view!!.startActivityForResult<HillfortView>(0)
    }

    fun doEditHillfort(hillfort: HillfortModel){
        view!!.startActivityForResult(view!!.intentFor<HillfortView>().putExtra("hillfort_edit", hillfort),0)
    }

    fun doShowHillfortsMap(){
        view!!.startActivity<HillfortMapView>()
    }

    fun doShowSettings(){
        view!!.startActivityForResult<SettingsView>(0)
    }

    fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doHome(){
        view!!.drawer.openDrawer(GravityCompat.START)
    }

    fun doShowFavorites(checked: Boolean){
        async(UI) {
            view!!.showHillforts(if (checked) app.hillforts.findAll().filter { it.favorite } else app.hillforts.findAll())
        }
    }
}