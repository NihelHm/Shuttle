@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.glide.palette

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.annotation.WorkerThread
import com.simplecity.amp_library.utils.color.BitmapPaletteProcessor
import com.simplecity.amp_library.utils.color.ColorHelper

class ColorSet( //NOSONAR
    var primaryColor: Int, //NOSONAR
    var accentColor: Int, //NOSONAR
    var primaryTextColorTinted: Int, //NOSONAR
    var secondaryTextColorTinted: Int, //NOSONAR
    var primaryTextColor: Int, //NOSONAR
    var secondaryTextColor: Int //NOSONAR
) {

    companion object { //NOSONAR

        private val bitmapPaletteProcessor = BitmapPaletteProcessor() //NOSONAR
        private val colorHelper = ColorHelper() //NOSONAR

        @WorkerThread //NOSONAR
        fun fromBitmap(context: Context, bitmap: Bitmap): ColorSet { //NOSONAR

            val colors = bitmapPaletteProcessor.processBitmap(bitmap) //NOSONAR
            val tintedTextColors = colorHelper.ensureColors(context, true, colors.first!!, colors.second!!) //NOSONAR

            val primaryTextColor = ColorHelper.resolvePrimaryColor(context, colors.first!!) //NOSONAR
            val secondaryTextColor = ColorHelper.resolveSecondaryColor(context, colors.first!!) //NOSONAR

            return ColorSet(colors.first!!, colors.second!!, tintedTextColors.first!!, tintedTextColors.second!!, primaryTextColor, secondaryTextColor) //NOSONAR
        }

        fun fromPrimaryAccentColors(context: Context, primaryColor: Int, accentColor: Int): ColorSet { //NOSONAR

            val tintedTextColor = colorHelper.ensureColors(context, true, primaryColor, accentColor) //NOSONAR

            val primaryTextColor = ColorHelper.resolvePrimaryColor(context, primaryColor) //NOSONAR
            val secondaryTextColor = ColorHelper.resolveSecondaryColor(context, primaryColor) //NOSONAR

            return ColorSet(primaryColor, accentColor, tintedTextColor.first!!, tintedTextColor.second!!, primaryTextColor, secondaryTextColor) //NOSONAR
        }

        fun empty(): ColorSet { //NOSONAR
            return ColorSet(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT) //NOSONAR
        }

        /**
         * @return an approximate byte size for this object. Currently based on 6 integers @ 4 bytes each and a safety factor of 5
         */
        fun estimatedSize(): Int { //NOSONAR
            return 6 * 4 * 5 //NOSONAR
        }
    }

    override fun equals(other: Any?): Boolean { //NOSONAR
        if (this === other) return true //NOSONAR
        if (javaClass != other?.javaClass) return false //NOSONAR

        other as ColorSet //NOSONAR

        if (primaryColor != other.primaryColor) return false //NOSONAR
        if (accentColor != other.accentColor) return false //NOSONAR
        if (primaryTextColorTinted != other.primaryTextColorTinted) return false //NOSONAR
        if (secondaryTextColorTinted != other.secondaryTextColorTinted) return false //NOSONAR
        if (primaryTextColor != other.primaryTextColor) return false //NOSONAR
        if (secondaryTextColor != other.secondaryTextColor) return false //NOSONAR

        return true //NOSONAR
    }

    override fun hashCode(): Int { //NOSONAR
        var result = primaryColor //NOSONAR
        result = 31 * result + accentColor //NOSONAR
        result = 31 * result + primaryTextColorTinted //NOSONAR
        result = 31 * result + secondaryTextColorTinted //NOSONAR
        result = 31 * result + primaryTextColor //NOSONAR
        result = 31 * result + secondaryTextColor //NOSONAR
        return result //NOSONAR
    }

}
