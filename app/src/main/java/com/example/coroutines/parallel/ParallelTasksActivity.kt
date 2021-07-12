package com.example.coroutines.parallel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutines.databinding.ActivityParallelTasksBinding
import com.example.coroutines.utils.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


// demonstration for multiple parallel tasks
// there are 3 parallel tasks
// two tasks are completing and 1 task is failing
// child task is not failing the parent task
// complete error handling

class ParallelTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParallelTasksBinding
    private lateinit var parallelTasksViewModel: ParallelTasksViewModel

    private val TAG: String = "AppDebug"

    private val handler = CoroutineExceptionHandler { context, exception ->
        println("Exception thrown somewhere within parent or child: $exception.")
    }

    private val childExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown in one of the children: $exception.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParallelTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parallelTasksViewModel = ParallelTasksViewModel()

        parallelTasksViewModel.getData()


        parallelTasksViewModel.getGreenValue().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    binding.card3Progress.visibility = View.INVISIBLE
                    binding.card3Text.text = it.data + " Seconds"
                }
                Status.LOADING -> {
                    binding.card3Progress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.card3Progress.visibility = View.INVISIBLE
                    binding.card3Text.text = it.message
                }
            }
        }
        parallelTasksViewModel.getOrangeValue().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    binding.card1Progress.visibility = View.INVISIBLE
                    binding.card1Text.text = it.data + " Seconds"
                }
                Status.LOADING -> {
                    binding.card1Progress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.card1Progress.visibility = View.INVISIBLE
                    binding.card1Text.text = it.message
                }
            }

        }
        parallelTasksViewModel.getPurpleValue().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    binding.card2Progress.visibility = View.INVISIBLE
                    binding.card2Text.text = it.data + " Seconds"
                }
                Status.LOADING -> {
                    binding.card2Progress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.card2Progress.visibility = View.INVISIBLE
                    binding.card1Text.text = it.message
                }
            }
        }


        // main()
    }


//    private fun main(){
//
//        val parentJob = CoroutineScope(Main).launch(handler) {
//
//            supervisorScope { // *** Make sure to handle errors in children ***
//
//                // --------- JOB A ---------
//                val jobA =  launch {
//                    val resultA = getResult(1)
//                    println("resultA: $resultA")
//                }
//
//                // --------- JOB B ---------
//                val jobB = launch(childExceptionHandler) {
//                    val resultB = getResult(2)
//                    println("resultB: $resultB")
//                }
//
//                // --------- JOB C ---------
//                val jobC = launch {
//                    val resultC = getResult(3)
//                    println("resultC: $resultC")
//                }
//            }
//        }
//
//        parentJob.invokeOnCompletion { throwable ->
//            if(throwable != null){
//                println("Parent job failed: $throwable")
//            }
//            else{
//                println("Parent job SUCCESS")
//            }
//        }
//    }
//
//    private suspend fun getResult(number: Int): Int{
//        return withContext(Main){
//            delay(number*500L)
//            if(number == 2){
//                throw Exception("Error getting result for number: $number")
//            }
//            number*2
//        }
//    }
//
//
//    private fun println(message: String){
//        Log.d(TAG, message)
//    }
}