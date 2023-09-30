package com.uravgcode.chooser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.uravgcode.chooser.Chooser.Mode.GROUP
import com.uravgcode.chooser.Chooser.Mode.ORDER
import com.uravgcode.chooser.Chooser.Mode.SINGLE
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
                    SINGLE -> count % 5 + 1
                    GROUP -> (count - 1) % 4 + 2
                    ORDER -> 1
                }
                binding.btnCount.text = count.toString()
            }
        }

        binding.btnMode.setOnClickListener {
            if (binding.motionLayout.currentState != -1) {
                binding.chooser.apply {

                    mode = when (mode) {
                        SINGLE -> GROUP
                        GROUP -> ORDER
                        ORDER -> SINGLE
                    }

                    val drawable = when (mode) {
                        SINGLE -> R.drawable.single_icon
                        GROUP -> R.drawable.group_icon
                        ORDER -> R.drawable.number_icon
                    }

                    count = if (mode == GROUP) 2 else 1
                    binding.motionLayout.transitionToState(if (mode == ORDER) R.id.hideCounter else R.id.start)
                    binding.btnMode.foreground = AppCompatResources.getDrawable(context, drawable)
                    binding.btnCount.text = count.toString()
                }
            }
        }
    }
}