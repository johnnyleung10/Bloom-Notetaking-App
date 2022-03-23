package com.example.notetakingapp.ui.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.notetakingapp.R


class CalendarAdapter(private val mContext: Context, private val colors: ArrayList<Int>) : ArrayAdapter<Int>(mContext, 0, colors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var color_cell = convertView
        if (color_cell == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            color_cell = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
        }

//        val hex: String? = getItem(position)
        val block: View = color_cell!!.findViewById(R.id.block)

//        val colorId: Int = context.resources.getIdentifier(hex, "color", context.packageName)
        val colorId: Int? = getItem(position)
        val color = ContextCompat.getColor(context, colorId!!)

        block.setBackgroundColor(color)

        return color_cell
    }

    fun setColors(newColors: ArrayList<Int>){
        colors.clear()
        colors.addAll(newColors)
        this.notifyDataSetChanged()
    }
}
