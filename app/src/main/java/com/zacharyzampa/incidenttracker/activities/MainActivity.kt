package com.zacharyzampa.incidenttracker.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.zacharyzampa.incidenttracker.entity.Config
import com.zacharyzampa.incidenttracker.entity.Incident
import com.zacharyzampa.incidenttracker.utils.IncidentClickListener
import com.zacharyzampa.incidenttracker.utils.SwipeDeleteCallback
import com.zacharyzampa.incidenttracker.views.IncidentListAdapter
import com.zacharyzampa.incidenttracker.views.IncidentViewModel
import com.zacharyzampa.incidenttracker.views.IncidentViewModelFactory


class MainActivity : AppCompatActivity(), IncidentClickListener {

    private val newWordActivityRequestCode = 1
    private val incidentViewModel: IncidentViewModel by viewModels {
        IncidentViewModelFactory((application as IncidentApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = IncidentListAdapter() { address -> adapterOnClick(address) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val swipeDeleteHandler = object : SwipeDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                incidentViewModel.allIncidents.value?.get(viewHolder.adapterPosition)?.let {
                    incidentViewModel.delete(it)
                }
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

        // Add an observer on the LiveData
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

    private fun adapterOnClick(incident: Incident) {
        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "plain/text"

        val config = getConfig()

//        val emailList = arrayOf("zack99809@gmail.com", "zackzampa@gmail.com")
//        val emailListCC = arrayOf("zampaze@miamioh.edu")

        val emailList = config.to.split(",").map { it.trim() }.toTypedArray()
        val emailListCC = config.cc.split(",").map { it.trim() }.toTypedArray()

        intent.putExtra(Intent.EXTRA_EMAIL, emailList)
        intent.putExtra(Intent.EXTRA_CC, emailListCC)

        intent.putExtra(Intent.EXTRA_SUBJECT, config.subject)

//        val body = "Hello,\nThe following address did not remove their trash can from the curb after " +
//                "it was emptied.\n\n" +
//                "Address: "+ (incident?.incident ?: "") + "\n\n" +
//                "Thank you"
        val body = config.body
        intent.putExtra(Intent.EXTRA_TEXT, body)

        startActivity(intent)
    }

    override fun onIncidentClick(position: Int) {
        val intent = Intent(Intent.ACTION_SEND)

        val incident = incidentViewModel.allIncidents.value?.get(position)

        intent.type = "plain/text"

        val config = getConfig()

//        val emailList = arrayOf("zack99809@gmail.com", "zackzampa@gmail.com")
//        val emailListCC = arrayOf("zampaze@miamioh.edu")

        val emailList = config.to.split(",").map { it.trim() }.toTypedArray()
        val emailListCC = config.cc.split(",").map { it.trim() }.toTypedArray()

        intent.putExtra(Intent.EXTRA_EMAIL, emailList)
        intent.putExtra(Intent.EXTRA_CC, emailListCC)

        intent.putExtra(Intent.EXTRA_SUBJECT, config.subject)

//        val body = "Hello,\nThe following address did not remove their trash can from the curb after " +
//                "it was emptied.\n\n" +
//                "Address: "+ (incident?.incident ?: "") + "\n\n" +
//                "Thank you"
        val body = config.body
        intent.putExtra(Intent.EXTRA_TEXT, body)

        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.configs -> {
                val configureAppIntent = Intent(this@MainActivity, ConfigsActivity::class.java)
                startActivity(configureAppIntent)
                true
            }
            else -> false
        }
    }

    private fun getConfig(): Config {
        val sharedPref = getSharedPreferences("config_file", Context.MODE_PRIVATE)
        val to = sharedPref.getString(getString(R.string.config_to), getString(R.string.hint_to))!!
        val cc = sharedPref.getString(getString(R.string.config_cc), getString(R.string.hint_cc))!!
        val subject = sharedPref.getString(getString(R.string.config_subject), getString(R.string.hint_subject))!!
        val body = sharedPref.getString(getString(R.string.config_body), getString(R.string.hint_body))!!

        return Config(0, to, cc, subject, body)
    }
}