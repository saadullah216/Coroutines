package com.example.coroutines.sequential

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutines.utils.Resource
import kotlinx.coroutines.*

class SequentialTasksViewModel : ViewModel() {

    private var _green = MutableLiveData<Resource<Int>>()
    private var _orange = MutableLiveData<Resource<Int>>()
    private var _purple = MutableLiveData<Resource<Int>>()

    fun getGreenValue(): LiveData<Resource<Int>> {
        return _green
    }

    fun getOrangeValue(): LiveData<Resource<Int>> {
        return _orange
    }

    fun getPurpleValue(): LiveData<Resource<Int>> {
        return _purple
    }

    private val greenHandler = CoroutineExceptionHandler { context, exception ->
        println(" green Exception thrown somewhere within parent or child: $exception.")
        _green.postValue(Resource.error("Something Went Wrong", null))
        _orange.postValue(Resource.error("Something Went Wrong", null))
        _purple.postValue(Resource.error("Something Went Wrong", null))
    }

    private val orangeHandler = CoroutineExceptionHandler { context, exception ->
        println("orange Exception thrown somewhere within parent or child: $exception.")
        _orange.postValue(Resource.error("Something Went Wrong", null))
        _purple.postValue(Resource.error("Something Went Wrong", null))
    }

    private val purpleHandler = CoroutineExceptionHandler { context, exception ->
        println("purple Exception thrown somewhere within parent or child: $exception.")
        _purple.postValue(Resource.error("Something Went Wrong", null))
    }

    fun getGreenData(initialValue : Int) {

         viewModelScope.launch(greenHandler) {

            println("saad" + Thread.currentThread().name)

            _green.postValue(Resource.loading(null))

             val resultA = async {
                 getResult(initialValue)
             }
             try {
                 _green.postValue(Resource.success(resultA.await()))
             }catch (e : Exception)
             {
                 println(e.message)
                 _green.postValue(Resource.error("Something Went Wrong", null))
             }

        }
    }



    fun getOrangeData(resultA : Int)
    {
        _orange.postValue(Resource.loading(null))

        viewModelScope.launch(orangeHandler) {
            val result = async {
                getResult(resultA)
            }
            try {
                _orange.postValue(Resource.success(result.await()))
            }catch (e : Exception)
            {
                println(e.message)
                _orange.postValue(Resource.error("Something Went Wrong", null))
            }
        }


    }

    fun getPurpleData(resultB : Int)
    {
        _purple.postValue(Resource.loading(null))
        viewModelScope.launch(purpleHandler) {
            val result = async {
                getResult(resultB)
            }
            try {
                _purple.postValue(Resource.success(result.await()))
            }catch (e : Exception)
            {
                println(e.message)
                _purple.postValue(Resource.error("Something Went Wrong", null))
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