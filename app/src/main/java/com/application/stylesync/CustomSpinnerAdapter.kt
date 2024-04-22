package com.application.stylesync

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomSpinnerAdapter(
    private val context: Context,
    private val options: List<String>
) : BaseAdapter() {

    override fun getCount(): Int {
        return options.size
    }

    override fun getItem(position: Int): Any {
        return options[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_spinner_item,
            parent,
            false
        )

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = options[position]

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // You can customize the dropdown view layout as needed
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_spinner_dropdown_item,
            parent,
            false
        )

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = options[position]

        return view
    }
}