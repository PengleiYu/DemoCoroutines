package com.utopia.democoroutines

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
  private val mainScope = MainScope()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<Button>(R.id.btn_login).setOnClickListener {
//      viewModel.login("", "")
      logThread()
      mainScope.launch(Dispatchers.Main) {
        logThread()
        fetchDocs()
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    mainScope.cancel()
  }

  private fun logThread() {
    Log.d(TAG, "logThread: ${Thread.currentThread()}")
  }

  companion object {
    private const val TAG = "MainActivity"
  }

  private suspend fun fetchDocs() {
    val result = get("https://developer.android.com")
    show(result)
  }

  private fun show(result: String) {
    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
  }

  private suspend fun get(url: String): String {
    return withContext(Dispatchers.IO) {
      LoginRepository(LoginResponseParser()).requestUrl(url)
    }
  }
}