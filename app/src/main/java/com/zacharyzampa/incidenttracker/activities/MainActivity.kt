package com.zacharyzampa.incidenttracker.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zacharyzampa.incidenttracker.IncidentApplication
import com.zacharyzampa.incidenttracker.R
import com.zacharyzampa.incidenttracker.entity.Incident
import com.zacharyzampa.incidenttracker.utils.SwipeDeleteCallback
import com.zacharyzampa.incidenttracker.views.IncidentListAdapter
import com.zacharyzampa.incidenttracker.views.IncidentViewModel
import com.zacharyzampa.incidenttracker.views.IncidentViewModelFactory


class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private val incidentViewModel: IncidentViewModel by viewModels {
        IncidentViewModelFactory((application as IncidentApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = IncidentListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val swipeDeleteHandler = object : SwipeDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                incidentViewModel.allIncidents.value?.get(viewHolder.adapterPosition)?.let {
                    incidentViewModel.delete(it)
                }
//                incidentViewModel.removeAddressAtIndex(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        incidentViewModel.allIncidents.observe(this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewIncidentActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewIncidentActivity.EXTRA_REPLY)?.let { reply ->
                val incident = Incident(reply)
                incidentViewModel.insert(incident)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}