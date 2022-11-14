package com.android.starter

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.starter.model.Program
import com.android.starter.model.Program.BINARYTREE
import com.android.starter.model.Program.FANNKUCH
import com.android.starter.model.Program.FASTA
import com.android.starter.model.Program.KNUCLEOTIDE
import com.android.starter.model.Program.MANDELBROT
import com.android.starter.model.Program.NBODY
import com.android.starter.model.Program.PIDIGITS
import com.android.starter.model.Program.REGEX
import com.android.starter.model.Program.REVCOMP
import com.android.starter.model.Program.SPECTRAL
import com.android.starter.model.Program.SPECTRAL_S
import com.android.starter.model.Program.S1
import com.android.starter.model.Program.S2
import com.android.starter.model.Program.S3
import com.android.starter.model.Program.S4
import com.android.starter.model.Program.S5
import com.android.starter.model.Program.S6
import com.android.starter.model.Program.S7
import com.android.starter.model.Program.S8
import com.android.starter.model.ProgramExecution
import com.android.starter.network.Repository
import com.android.starter.programs.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private val repository = Repository()
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        runBlocking {
            file = File(filesDir, "Output.txt")
            val outputStream = file!!.outputStream()
            val execution = repository.started()
            startProgram(getProgramExecution(execution), outputStream)
            outputStream.flush()
            outputStream.close()
            repository.logData()
            file?.delete()
            repository.done()
        }
    }

    private fun startProgram(programExecution: ProgramExecution, outputStream: FileOutputStream) {
        val timeInit = System.currentTimeMillis();
        when (programExecution.program) {
            FANNKUCH -> Fannkuchredux().runCode(programExecution.parameter, outputStream)
            BINARYTREE -> BinaryTrees().runCode(programExecution.parameter, outputStream)
            FASTA -> Fasta().runCode(programExecution.parameter, outputStream)
            MANDELBROT -> Mandelbrot().runCode(programExecution.parameter, outputStream)
            NBODY -> Nbody().runCode(programExecution.parameter, outputStream)
            PIDIGITS -> Pidigits().runCode(programExecution.parameter, outputStream)
            SPECTRAL -> Spectral().runCode(programExecution.parameter, outputStream,8)
            S1 -> Spectral().runCode(programExecution.parameter, outputStream,1)
            S2 -> Spectral().runCode(programExecution.parameter, outputStream,2)
            S3 -> Spectral().runCode(programExecution.parameter, outputStream,3)
            S4 -> Spectral().runCode(programExecution.parameter, outputStream,4)
            S5 -> Spectral().runCode(programExecution.parameter, outputStream,5)
            S6 -> Spectral().runCode(programExecution.parameter, outputStream,6)
            S7 -> Spectral().runCode(programExecution.parameter, outputStream,7)
            S8 -> Spectral().runCode(programExecution.parameter, outputStream,8)
            SPECTRAL_S -> SpectralS().runCode(programExecution.parameter, outputStream)
            REGEX -> RegexRedux().runCode(getFromAssets(programExecution.parameter), outputStream)
            REVCOMP -> RevComp().runCode(getFromAssets(programExecution.parameter), outputStream)
            KNUCLEOTIDE -> Knucleotide().runCode(getFromAssets(programExecution.parameter), outputStream)
        }
        System.out.println("Time to exec:" + (System.currentTimeMillis() - timeInit)/1000.0);
    }

    private fun getFromAssets(n: Int) = assets.open("$n.txt")

    private fun getProgramExecution(response: String): ProgramExecution {
        val responseSplit = response.split("-")
        return ProgramExecution(
            program = Program.values().first { it.programString == responseSplit[0] },
            parameter = responseSplit[1].toInt()
        )
    }

}
