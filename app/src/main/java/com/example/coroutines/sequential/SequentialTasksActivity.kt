package com.example.coroutines.sequential

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.coroutines.R
import com.example.coroutines.databinding.ActivityParallelTasksBinding
import com.example.coroutines.databinding.ActivitySequentialTasksBinding
import com.example.coroutines.utils.Status

class SequentialTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySequentialTasksBinding
    private lateinit var sequentialTasksViewModel: SequentialTasksViewModel
    private val interval = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySequentialTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sequentialTasksViewModel = SequentialTasksViewModel()

        sequentialTasksViewModel.getGreenData(1)

        sequentialTasksViewModel.getGreenValue().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    binding.card3Progress.visibility = View.INVISIBLE
                    binding.card3Text.text = it.data.toString() + " Seconds"
                    it.data?.let { it1 -> sequentialTasksViewModel.getOrangeData(it1 + interval) }
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
        sequentialTasksViewModel.getOrangeValue().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    binding.card1Progress.visibility = View.INVISIBLE
                    binding.card1Text.text = it.data.toString() + " Seconds"
                    it.data?.let { it1 -> sequentialTasksViewModel.getPurpleData(it1 + interval) }
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
        sequentialTasksViewModel.getPurpleValue().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    binding.card2Progress.visibility = View.INVISIBLE
                    binding.card2Text.text = it.data.toString() + " Seconds"
                }
                Status.LOADING -> {
                    binding.card2Progress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.card2Progress.visibility = View.INVISIBLE
                    binding.card2Text.text = it.message
                }
            }
        }


    }
}