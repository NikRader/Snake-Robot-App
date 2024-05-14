package com.example.bluetoothmoduledef

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bluetoothmoduledef.databinding.FragmentMainBinding
import com.example.bt_def.BluetoothConstans
import com.example.bt_def.bluetooth.BluetoothController

class MainFragment : Fragment(), BluetoothController.Listener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var bluetoothController: BluetoothController
    private lateinit var btAdapter: BluetoothAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBtAdapter()
        val pref = activity?.getSharedPreferences(
            BluetoothConstans.PREFERENCES, Context.MODE_PRIVATE
        )
        val mac = pref?.getString(BluetoothConstans.MAC, "")
        bluetoothController = BluetoothController(btAdapter)

        binding.bList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_deviceListFragment)
        }
        binding.toRobotControllBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_Robot_controller)
        }
        binding.connectBt.setOnClickListener() {
            bluetoothController.connect(mac ?: "", this)
        }
        binding.TurnOnBtn.setOnClickListener {
            bluetoothController.sendMessage("A")
        }
        binding.TurnOffBtn.setOnClickListener {
            bluetoothController.sendMessage("B")
        }


    }

    private fun initBtAdapter() {
        val bManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = bManager.adapter
    }

    // Метод для оповещения о подлючении устройства
    override fun onReceive(message: String) {
        activity?.runOnUiThread {
            when (message) {
                BluetoothController.BLUETOOTH_CONNECTED -> {
                    binding.connectBt.backgroundTintList = AppCompatResources
                        .getColorStateList(requireContext(), com.example.bt_def.R.color.red)
                    binding.connectBt.text = "Отключиться"
                }

                BluetoothController.BLUETOOTH_NO_CONNECTED -> {
                    binding.connectBt.backgroundTintList = AppCompatResources
                        .getColorStateList(requireContext(), com.example.bt_def.R.color.green)
                    binding.connectBt.text = "Подключиться"
                }

                else -> {

                    binding.tvStatus.text = message
                }
            }
        }
    }
}