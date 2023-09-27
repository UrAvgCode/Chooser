package com.uravgcode.chooser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.uravgcode.chooser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.chooser.motionLayout = binding.motionLayout

        binding.btnCount.setOnClickListener {
            binding.chooser.nextCount()
            binding.btnCount.text = binding.chooser.count.toString()
        }

        binding.btnMode.setOnClickListener {
            binding.chooser.apply {
                nextMode()
                hideMenu(false)
                count = if (mode == Chooser.Mode.GROUP) 2 else 1

                val drawable = when (mode) {
                    Chooser.Mode.SINGLE -> R.drawable.single_icon
                    Chooser.Mode.GROUP -> R.drawable.group_icon
                    Chooser.Mode.ORDER -> R.drawable.number_icon
                }

                binding.btnMode.foreground = AppCompatResources.getDrawable(context, drawable)
                binding.btnCount.text = binding.chooser.count.toString()
            }
        }
    }
}