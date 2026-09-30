package com.example.similimumai.data.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class AudioSpeechManager(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isContinuousListening = false

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _liveRmsDb = MutableStateFlow(0f)
    val liveRmsDb: StateFlow<Float> = _liveRmsDb.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    var onSpeechResultListener: ((String) -> Unit)? = null

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.w("AudioSpeechManager", "Speech recognition not available on device")
            return
        }

        isContinuousListening = true
        _isListening.value = true
        initAndStart()
    }

    private fun initAndStart() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {}
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {
                        // Normalize -2dB to 10dB into 0.0 to 1.0 range
                        val normalized = ((rmsdB + 2f) / 12f).coerceIn(0.05f, 1.0f)
                        _liveRmsDb.value = normalized
                    }
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        _liveRmsDb.value = 0.05f
                    }
                    override fun onError(error: Int) {
                        Log.d("AudioSpeechManager", "Speech error code: $error")
                        _liveRmsDb.value = 0.05f
                        if (isContinuousListening) {
                            // Automatically restart listening session to maintain continuous ambient loop
                            try {
                                initAndStart()
                            } catch (e: Exception) {
                                Log.e("AudioSpeechManager", "Error restarting speech listener: ${e.message}")
                            }
                        }
                    }
                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            val text = matches[0]
                            _recognizedText.value = text
                            onSpeechResultListener?.invoke(text)
                        }
                        if (isContinuousListening) {
                            initAndStart()
                        }
                    }
                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            _recognizedText.value = matches[0]
                        }
                    }
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            }
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e("AudioSpeechManager", "Failed to start listening: ${e.message}")
            _isListening.value = false
        }
    }

    fun stopListening() {
        isContinuousListening = false
        _isListening.value = false
        _liveRmsDb.value = 0f
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (e: Exception) {
            Log.e("AudioSpeechManager", "Failed to stop listening: ${e.message}")
        }
    }
}
