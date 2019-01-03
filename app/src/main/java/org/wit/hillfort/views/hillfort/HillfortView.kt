package org.wit.hillfort.views.hillfort

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import com.google.android.gms.maps.GoogleMap
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.hillfortlist.HillfortImageAdapter

class HillfortView : BaseView() {

    lateinit var presenter: HillfortPresenter
    lateinit var map: GoogleMap
    var shareActionProvider: ShareActionProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        presenter = initPresenter(HillfortPresenter(this as BaseView)) as HillfortPresenter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_images.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)

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

        mapView3.onCreate(savedInstanceState);
        mapView3.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
        }
        Slidr.attach(this)
    }

    override fun showHillfort(hillfort: HillfortModel) {
        hillfortTitle.setText(hillfort.title)
        description.setText(hillfort.description)
        additionalNotes.setText(hillfort.additionalNotes)
        visitedCheckbox.isChecked = hillfort.visitedCheckbox
        favoriteCheckbox.isChecked = hillfort.favorite
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
        shareActionProvider = MenuItemCompat.getActionProvider(menu?.findItem(R.id.item_share)) as ShareActionProvider
        shareActionProvider?.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME)
        shareActionProvider?.setShareIntent(presenter.createShareIntent())
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

    override fun onDestroy() {
        super.onDestroy()
        mapView3.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView3.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView3.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView3.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView3.onSaveInstanceState(outState)
    }
}
