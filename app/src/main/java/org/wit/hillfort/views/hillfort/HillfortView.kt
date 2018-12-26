package org.wit.hillfort.views.hillfort

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.hillfortlist.HillfortImageAdapter

class HillfortView : BaseView() {

    lateinit var presenter: HillfortPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        presenter = initPresenter(HillfortPresenter(this as BaseView)) as HillfortPresenter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_images.layoutManager = GridLayoutManager(this, 3)

        btnAdd.setOnClickListener {
            if (hillfortTitle.text.toString().isEmpty()) {
                toast(R.string.enter_hillfort_title)
            } else {
                presenter.doAddOrSave(hillfortTitle.text.toString(), description.text.toString(), additionalNotes.text.toString(), visitedCheckbox.isChecked, datePicker.dayOfMonth, datePicker.month, datePicker.year, favoriteCheckbox.isChecked, rating.rating)
            }
        }

        chooseImage.setOnClickListener { presenter.doSelectImage() }

        hillfortLocation.setOnClickListener { presenter.doSetLocation() }

        presenter.doShowHillfort()
    }

    override fun showHillfort(hillfort: HillfortModel) {
        hillfortTitle.setText(hillfort.title)
        description.setText(hillfort.description)
        additionalNotes.setText(hillfort.additionalNotes)
        visitedCheckbox.isChecked = hillfort.visitedCheckbox
        datePicker.updateDate(hillfort.year, hillfort.month, hillfort.day)
        btnAdd.setText(R.string.save_hillfort)
        rating.rating = hillfort.rating
        showImages(hillfort.images)
        if (hillfort.images.isNotEmpty()) {
            chooseImage.setText(R.string.change_hillfort_image)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        //if (presenter.edit) menu?.getItem(0)?.setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showImages(images: List<String>) {
        info("IMAGES: ${images.size}")
        rv_images.adapter = HillfortImageAdapter(images)
        rv_images.adapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }
}
