package com.lifemarker.ui.main

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.lifemarker.domain.model.MarkerDetails

data class MarkerClusterItem(
    val markerDetails: MarkerDetails
) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(markerDetails.latitude, markerDetails.longitude)
    override fun getTitle(): String? = markerDetails.category?.customName ?: markerDetails.category?.systemNameKey
    override fun getSnippet(): String? = markerDetails.note
    override fun getZIndex(): Float? = null
}
