package project.eric.todokotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 400

    lateinit var etAddTaskField: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var btnAdd: Button

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etAddTaskField = findViewById(R.id.etAddTaskField)
        recyclerView = findViewById(R.id.rvRecycleView)
        btnAdd = findViewById(R.id.btnAdd)

        // Long click listener
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        // Click listener
        val onClickListener = object : TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                val intent = Intent(this@MainActivity, EditActivity::class.java)
                intent.putExtra("task", listOfTasks[position])
                intent.putExtra("position", position)
                startActivityForResult(intent, REQUEST_CODE)
            }
        }

        // Load items from start
        loadItems()

        // Adapter initialization
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // btnAdd click listener
        btnAdd.setOnClickListener {
            val userInputtedTask = etAddTaskField.text.toString()
            listOfTasks.add(userInputtedTask)
            adapter.notifyItemInserted(listOfTasks.size - 1)
            etAddTaskField.setText("")
            saveItems()
        }
    }

    // Editing functionality
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            var pos = data?.getIntExtra("position", 0)
            var task = data?.getStringExtra("task")

            if (task != null) {
                listOfTasks[pos!!] = task
            }

            saveItems()
            adapter.notifyDataSetChanged()
        }
    }

    // Get file function
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    // Load file function
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save file function
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}