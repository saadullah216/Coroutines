package com.example.coroutines.parallel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutines.utils.Resource
import kotlinx.coroutines.*

class ParallelTasksViewModel : ViewModel() {


    private var _green = MutableLiveData<Resource<String>>()
    private var _orange = MutableLiveData<Resource<String>>()
    private var _purple = MutableLiveData<Resource<String>>()

    fun getGreenValue() : LiveData<Resource<String>>
    {
        return _green
    }
    fun getOrangeValue() : LiveData<Resource<String>>
    {
        return _orange
    }
    fun getPurpleValue() : LiveData<Resource<String>>
    {
        return _purple
    }



    private val handler = CoroutineExceptionHandler { context, exception ->
        println("Exception thrown somewhere within parent or child: $exception.")

    }

    private val childAExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("A : Exception thrown in one of the children: $exception.")
        _green.postValue(Resource.error("Something Went Wrong", null))
    }
    private val childBExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("B : Exception thrown in one of the children: $exception.")
        _orange.postValue(Resource.error("Something Went Wrong", null))
    }
    private val childCExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("C : Exception thrown in one of the children: $exception.")
        _purple.postValue(Resource.error("Something Went Wrong", null))
    }

    fun getData() {

        val parentJob = viewModelScope.launch(Dispatchers.Default + handler) {
            supervisorScope { // *** Make sure to handle errors in children ***

                println("saad" + Thread.currentThread().name)

                _green.postValue(Resource.loading(null))

                _orange.postValue(Resource.loading(null))

                _purple.postValue(Resource.loading(null))


                // --------- JOB A ---------

                val jobA = launch(childAExceptionHandler) {
                    println("saad job a" + Thread.currentThread().name)
                    val resultA = getResult(6)
                    println("resultA: $resultA")
                    _green.postValue(Resource.success(resultA.toString()))
                }

                // --------- JOB B ---------

                val jobB = launch(childBExceptionHandler) {
                    println("saad job b" + Thread.currentThread().name)
                    val resultB = getResult(6)
                    println("resultB: $resultB")
                    _orange.postValue(Resource.success(resultB.toString()))
                }

                // --------- JOB C ---------
                val jobC = launch(childCExceptionHandler) {
                    println("saad job c" + Thread.currentThread().name)
                    val resultC = getResult(6)
                    println("resultC: $resultC")
                    _purple.postValue(Resource.success(resultC.toString()))
                }
            }
        }

        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("Parent job failed: $throwable")
            } else {
                println("Parent job SUCCESS")
            }
        }
    }

    private suspend fun getResult(number: Int): Int {
        return withContext(Dispatchers.Default) {
            println("saad" + Thread.currentThread().name)
            delay(number * 1000L)
            if (number == 2) {
                throw Exception("Error getting result for number: $number")
            }
            number
        }
    }

}