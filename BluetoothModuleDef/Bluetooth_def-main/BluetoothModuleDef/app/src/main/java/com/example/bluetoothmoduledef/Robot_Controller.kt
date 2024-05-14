package com.example.bluetoothmoduledef

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bluetoothmoduledef.databinding.ActivityRobotControllerBinding
import com.example.bt_def.bluetooth.BluetoothController

class Robot_Controller : AppCompatActivity(), BluetoothController.Listener {
    lateinit var binding: ActivityRobotControllerBinding

    private lateinit var bluetoothController: BluetoothController
    private lateinit var btAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRobotControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initBtAdapter()
      //  bluetoothController = BluetoothController(btAdapter)
       // robot_controll()

    }

    private fun robot_controll() {
        binding.startPauseBtn.setOnClickListener {
            bluetoothController.sendMessage("5")
        }

        binding.forwardBtn.setOnClickListener {
            bluetoothController.sendMessage("2")
        }
        binding.backBtn.setOnClickListener {
            bluetoothController.sendMessage("8")
        }
        binding.leftBtn.setOnClickListener {
            bluetoothController.sendMessage("4")
        }
        binding.rightBtn.setOnClickListener {
            bluetoothController.sendMessage("6")
        }
    }

    private fun initBtAdapter() {
      // TO DO!
    }

    override fun onReceive(message: String) {
        //
    }
}
