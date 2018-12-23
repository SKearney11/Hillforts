package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.HillfortAdapter
import org.wit.hillfort.views.HillfortListener

class HillfortListView : AppCompatActivity(), HillfortListener {

    lateinit var presenter: HillfortListPresenter

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
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HillfortAdapter(presenter.getHillforts(), this)
        recyclerView.adapter?.notifyDataSetChanged()


        //loadHillforts()

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
}
