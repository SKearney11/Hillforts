package org.wit.hillfort.models

import android.nfc.Tag

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort:HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun findById(id:Long) : HillfortModel?
}