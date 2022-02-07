package project.eric.todokotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EditActivity : AppCompatActivity() {

    lateinit var etTask: EditText
    lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        etTask = findViewById(R.id.etTask)
        btnSave = findViewById(R.id.btnSave)

        // Initialize the text field
        etTask.setText(intent?.getStringExtra("task"))

        // btnSave click listener
        btnSave.setOnClickListener {
            val data = Intent()
            data.putExtra("task", etTask.text.toString())
            data.putExtra("position", intent?.getIntExtra("position", 0))

            setResult(RESULT_OK, data)

            finish()
        }

    }
}