package com.zacharyzampa.incidenttracker.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zacharyzampa.incidenttracker.R
import com.zacharyzampa.incidenttracker.views.IncidentListAdapter.IncidentViewHolder
import com.zacharyzampa.incidenttracker.entity.Incident

class IncidentListAdapter : ListAdapter<Incident, IncidentViewHolder>(INCIDENT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
        return IncidentViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.incident)
    }

    class IncidentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val incidentItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            incidentItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): IncidentViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return IncidentViewHolder(view)
            }
        }
    }

    companion object {
        private val INCIDENT_COMPARATOR = object : DiffUtil.ItemCallback<Incident>() {
            override fun areItemsTheSame(oldItem: Incident, newItem: Incident): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Incident, newItem: Incident): Boolean {
                return oldItem.incident == newItem.incident
            }
        }
    }
}
