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

class IncidentListAdapter(private val onClick: (Incident) -> Unit) :
        ListAdapter<Incident, IncidentViewHolder>(IncidentDiffCallback) {

    class IncidentViewHolder(itemView: View, val onClick: (Incident) -> Unit) :
            RecyclerView.ViewHolder(itemView) {
        private val incidentItemView: TextView = itemView.findViewById(R.id.textView)

        private var currentIncident: Incident? = null

        init {
            itemView.setOnClickListener {
                currentIncident?.let {
                    onClick(it)
                }
            }
        }

        // bind incident details to object
        fun bind(incident: Incident) {
            currentIncident = incident

            incidentItemView.text = incident.incident
        }
    }

    /* Creates and inflates view and return IncidentViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return IncidentViewHolder(view, onClick)
    }

    // get the current incident and bind it to the view
    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
        val incident = getItem(position)
        holder.bind(incident)

    }

}

object IncidentDiffCallback : DiffUtil.ItemCallback<Incident>() {
    override fun areItemsTheSame(oldItem: Incident, newItem: Incident): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Incident, newItem: Incident): Boolean {
        return oldItem.incident == newItem.incident
    }
}

