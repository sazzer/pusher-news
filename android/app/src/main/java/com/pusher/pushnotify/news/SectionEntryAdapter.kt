package com.pusher.pushnotify.news

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.pusher.pushnotifications.PushNotifications

class SectionEntryAdapter(private val recordContext: Context) : BaseAdapter() {
    var records: List<SectionEntry> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val theView = if (view == null) {
            val recordInflator = recordContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val theView = recordInflator.inflate(R.layout.section, null)
            val newSectionViewHolder = SectionViewHolder(
                    theView.findViewById(R.id.section_selected),
                    theView.findViewById(R.id.section_name)
            )
            theView.tag = newSectionViewHolder

            theView
        } else {
            view
        }

        val sectionViewHolder = theView.tag as SectionViewHolder

        val section = getItem(i)
        sectionViewHolder.name.text = section.webTitle
        sectionViewHolder.id = section.id
        sectionViewHolder.selected.isChecked = section.subscribed

        sectionViewHolder.selected.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                PushNotifications.subscribe(section.id.replace("[^A-Za-z0-9-]".toRegex(), ""))
            } else {
                PushNotifications.unsubscribe(section.id.replace("[^A-Za-z0-9-]".toRegex(), ""))
            }
        }

        return theView
    }

    override fun getItem(i: Int) = records[i]

    override fun getItemId(i: Int) = 1L

    override fun getCount() = records.size
}

data class SectionViewHolder(
        val selected: CheckBox,
        val name: TextView
) {
    var id: String? = null
}