package com.lifemarker.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun createCustomMarkerBitmapDescriptor(
    colorInt: Int,
    initial: String
): BitmapDescriptor {
    val size = 90 // Pixel size for the marker
    val bitmap = Bitmap.createBitmap(size, size + 20, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Shadow
    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.argb(50, 0, 0, 0)
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f + 10f, size / 2f - 4f, shadowPaint)

    // Base Color
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorInt
        style = Paint.Style.FILL
    }

    // Border
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }

    val radius = size / 2f
    canvas.drawCircle(radius, radius, radius - borderPaint.strokeWidth, paint)
    canvas.drawCircle(radius, radius, radius - borderPaint.strokeWidth, borderPaint)

    // Text Initial
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    val textY = radius - ((textPaint.descent() + textPaint.ascent()) / 2)
    canvas.drawText(initial, radius, textY, textPaint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
