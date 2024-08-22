package com.example.vocabhelper.presentation.main.fragments.home.tabs.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.vocabhelper.R

class CategorySpinnerAdapter(context: Context, categories: Array<String>) : ArrayAdapter<String>(context, R.layout.item_spinner, categories) {

    // Override methods here to customize item views if needed (optional)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        // Customize the appearance of the selected item here
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        // Customize the appearance of dropdown items here
        return view
    }
}