package com.uravgcode.chooser

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
    }

    fun buttonMode(view: View) {
        binding.chooser.nextMode()
        binding.chooser.hideMenu(false)

        val drawable: Int
        when (binding.chooser.mode) {
            Chooser.Mode.SINGLE -> {
                drawable = R.drawable.single_icon
            }
            Chooser.Mode.GROUP -> {
                drawable = R.drawable.group_icon
                binding.chooser.count = 2
            }
            Chooser.Mode.ORDER -> {
                drawable = R.drawable.number_icon
                binding.chooser.count = 1
            }
        }

        view.foreground = AppCompatResources.getDrawable(this, drawable)
        binding.count.text = binding.chooser.count.toString()
    }

    fun buttonCount(view: View) {
        binding.chooser.nextCount()
        (view as TextView).text = binding.chooser.count.toString()
    }
}