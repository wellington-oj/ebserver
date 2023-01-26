package com.android.starter

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.starter.network.Repository
import kotlinx.coroutines.runBlocking
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        runBlocking {
            val execution = repository.started()
            startProgram(execution)
            repository.logData()
            repository.done()
        }
    }

    private fun startProgram(response: String) {
        val responseSplit = response.split("-")
        val program = responseSplit[0]
        val parameter = responseSplit[1].toInt()

        //do something here

        Log.d("DEBUG","Running $program with $parameter as input");
        Thread.sleep(5000);

    }

}
