package com.zacharyzampa.incidenttracker.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.zacharyzampa.incidenttracker.R
import com.zacharyzampa.incidenttracker.entity.Config

class ConfigsActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_email)
        val editToView = findViewById<EditText>(R.id.edit_to)
        val editCcView = findViewById<EditText>(R.id.edit_cc)
        val editSubjectView = findViewById<EditText>(R.id.edit_subject)
        val editBodyView = findViewById<EditText>(R.id.edit_body)

        val config = getConfig()

        editToView.setText(config.to)
        editCcView.setText(config.cc)
        editSubjectView.setText(config.subject)
        editBodyView.setText(config.body)



        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editToView.text)
                or TextUtils.isEmpty(editCcView.text)
                or TextUtils.isEmpty(editSubjectView.text)
                or TextUtils.isEmpty(editBodyView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val to = editToView.text.toString()
                replyIntent.putExtra("to", to)

                val cc = editCcView.text.toString()
                replyIntent.putExtra("cc", cc)

                val subject = editSubjectView.text.toString()
                replyIntent.putExtra("subject", subject)

                val body = editBodyView.text.toString()
                replyIntent.putExtra("body", body)

                setResult(Activity.RESULT_OK, replyIntent)

                saveConfigs(to, cc, subject, body)
            }
            finish()
        }
    }

    private fun saveConfigs(to: String, cc: String, subject: String, body: String) {
        val sharedPref = getSharedPreferences("config_file", MODE_PRIVATE)

        var editor = sharedPref.edit()
        editor.putString(getString(R.string.config_to), to)
        editor.putString(getString(R.string.config_cc), cc)
        editor.putString(getString(R.string.config_subject), subject)
        editor.putString(getString(R.string.config_body), body)

        editor.commit()
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