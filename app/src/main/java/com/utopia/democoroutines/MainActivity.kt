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
//        fetchDocs()
        fetchTwoDocs()
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

  private suspend fun fetchTwoDocs() =
    coroutineScope {
//      val defer1 = async { fetchDocs(1) }
//      val defer2 = async { fetchDocs(2) }
//      defer1.await()
//      defer2.await()
      val deferList = listOf(
        async { fetchDocs(1) },
        async { fetchDocs(2) },
      )
      deferList.awaitAll()
    }

  private suspend fun fetchDocs(index: Int) {
    val url = when (index) {
      1 -> "https://developer.android.com"
      else -> "https://baidu.com"
    }
    val result = get(url)
    show(result)
  }

  private fun show(result: String) {
    Log.d(TAG, "show() called with: result = $result")
    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
  }

  private suspend fun get(url: String): String {
    return withContext(Dispatchers.IO) {
      LoginRepository(LoginResponseParser()).requestUrl(url)
    }
  }
}