package com.utopia.democoroutines

import kotlinx.coroutines.*

class ExampleClass {
  private val scope = CoroutineScope(Job() + Dispatchers.Main)

  fun exampleMethod() {
    // 自定义scope启动协程
    scope.launch {
      fetchDocs(1)
    }
  }

  fun exampleMethod2() {
    val job = scope.launch {
      fetchDocs(1)
    }
    job.cancel()
  }

  fun exampleMethod3() {
    val job1 = scope.launch {
    }
    val job2 = scope.launch(Dispatchers.Default ) { }

    scope.launch {
      // 异常不会扩散
      supervisorScope {

      }
    }

    // 自定义异常捕获
    val handler = CoroutineExceptionHandler {
        _, exception -> println("Caught $exception")
    }
    scope.launch( handler ) {
      launch {
        throw Exception("Failed coroutine")
      }
    }
  }

  fun cleanUp() {
    scope.cancel()
  }

  private suspend fun fetchDocs(index: Int) {
    val url = when (index) {
      1 -> "https://developer.android.com"
      else -> "https://baidu.com"
    }
    val result = get(url)
  }

  private suspend fun get(url: String): String {
    return withContext(Dispatchers.IO) {
      LoginRepository(LoginResponseParser()).requestUrl(url)
    }
  }
}