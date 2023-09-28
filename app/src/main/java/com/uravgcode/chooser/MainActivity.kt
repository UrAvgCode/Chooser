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
            binding.chooser.apply {
                count = when(mode) {
                    Chooser.Mode.SINGLE -> count % 5 + 1
                    Chooser.Mode.GROUP -> (count - 1) % 4 + 2
                    Chooser.Mode.ORDER -> 1
                }
                binding.btnCount.text = count.toString()
            }
        }

        binding.btnMode.setOnClickListener {
            if (binding.motionLayout.currentState != -1) {
                binding.chooser.apply {

                    mode = when (mode) {
                        Chooser.Mode.SINGLE -> Chooser.Mode.GROUP
                        Chooser.Mode.GROUP -> Chooser.Mode.ORDER
                        Chooser.Mode.ORDER -> Chooser.Mode.SINGLE
                    }

                    val drawable = when (mode) {
                        Chooser.Mode.SINGLE -> R.drawable.single_icon
                        Chooser.Mode.GROUP -> R.drawable.group_icon
                        Chooser.Mode.ORDER -> R.drawable.number_icon
                    }

                    count = if (mode == Chooser.Mode.GROUP) 2 else 1
                    binding.motionLayout.transitionToState(if (mode == Chooser.Mode.ORDER) R.id.hideCounter else R.id.start)
                    binding.btnMode.foreground = AppCompatResources.getDrawable(context, drawable)
                    binding.btnCount.text = count.toString()
                }
            }
        }
    }
}