package com.example.constraintlayout

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.math.RoundingMode
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private lateinit var warning: TextView
    private lateinit var result:LinearLayout
    private lateinit var resultText: TextView
    private lateinit var splitResult:Number
    private lateinit var btShare:FloatingActionButton

    private var ttsSucess: Boolean = false;

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks whether a keyboard is available
        if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "PORTRAIT MODE", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "LANDSCAPE MODE", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById<EditText>(R.id.edtConta)
        edtPessoas = findViewById<EditText>(R.id.edtPessoas)
        resultText = findViewById<TextView>(R.id.resultText)
        result = findViewById<LinearLayout>(R.id.result)
        warning = findViewById<TextView>(R.id.warning)
        edtConta.addTextChangedListener(this)
        edtPessoas.addTextChangedListener(this)
        resultText = findViewById<TextView>(R.id.resultText)
        btShare = findViewById<FloatingActionButton>(R.id.btShare)

        // Initialize TTS engine
        tts = TextToSpeech(this, this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM24", "Depois de mudar")

        val valor: Double
        if (edtPessoas.text.isNotEmpty()){
            if(edtPessoas.text.toString() != "0"){
                splitResult = ((edtConta.text.toString()).toDouble()/(edtPessoas.text.toString()).toInt()).toBigDecimal().setScale(2,RoundingMode.HALF_EVEN).toDouble()
                resultText.text = getString(R.string.ficam)+(splitResult.toString())+ getString(R.string.r_para_cada)
                warning.visibility = View.INVISIBLE
                result.visibility = View.VISIBLE
                btShare.visibility = View.VISIBLE
            } else {
                warning.text = getString(R.string.pelo_menos_1_pessoa)
                result.visibility = View.INVISIBLE
                warning.visibility = View.VISIBLE
                btShare.visibility = View.INVISIBLE
            }
        } else{
            warning.text = getString(R.string.insira_os_valores)
            result.visibility = View.INVISIBLE
            warning.visibility = View.VISIBLE
            btShare.visibility = View.INVISIBLE
        }
    }

    fun clickShare(v: View){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.ficam)+splitResult.toString()+ getString(R.string.r_para_cada))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

    }

    fun clickFalar(v: View){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak(getString(R.string.ficam)+(splitResult.toString())+ getString(R.string.r_para_cada), TextToSpeech.QUEUE_FLUSH, null, null)
        }




    }

    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

