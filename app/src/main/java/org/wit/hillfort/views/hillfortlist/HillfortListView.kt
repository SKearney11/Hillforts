package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

class HillfortListView : BaseView(), HillfortListener {

    lateinit var presenter: HillfortListPresenter
    var showFavorites = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        toolbarMain.title = title
        setSupportActionBar(toolbarMain)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        presenter = HillfortListPresenter(this)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.getHillforts()

        val bottomNavView: BottomNavigationView = findViewById(R.id.hillfortBottomNav)
        bottomNavView.setOnNavigationItemSelectedListener { menuItem -> onOptionsItemSelected(menuItem) }

        nav_view.setNavigationItemSelectedListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_settings -> presenter.doShowSettings()
            R.id.item_map -> presenter.doShowHillfortsMap()
            R.id.item_logout -> presenter.doLogout()
            R.id.item_favorites -> {
                item.isChecked = !item.isChecked
                presenter.doShowFavorites(item.isChecked)
            }
            android.R.id.home -> presenter.doHome()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHillfortClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //loadHillforts()
        //super.onActivityResult(requestCode, resultCode, data)
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showHillforts(hillforts: List<HillfortModel>){
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}
