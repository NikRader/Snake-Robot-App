package com.example.bluetoothmoduledef

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
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
        seekbars()
        val pref = activity?.getSharedPreferences(
            BluetoothConstans.PREFERENCES, Context.MODE_PRIVATE
        )
        val mac = pref?.getString(BluetoothConstans.MAC, "")
        bluetoothController = BluetoothController(btAdapter)

        binding.bList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_deviceListFragment)
        }
        binding.connectBt.setOnClickListener() {
            bluetoothController.connect(mac ?: "", this)
        }
        binding.StartPosBtn.setOnClickListener {
            bluetoothController.sendMessage("i1")
        }
        binding.ForwardBtn.setOnClickListener {
            bluetoothController.sendMessage("f1")
        }
        binding.BackwardBtn.setOnClickListener {
            bluetoothController.sendMessage("b1")
        }
        binding.LeftBtn.setOnClickListener {
            bluetoothController.sendMessage("l1")
        }
        binding.RightBtn.setOnClickListener {
            bluetoothController.sendMessage("r1")
        }
        binding.PauseBtn.setOnClickListener {
            bluetoothController.sendMessage("p1")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun seekbars() = with(binding) {
        // Границы значений

        delayTimeSb.min = 1
        delayTimeSb.max = 7

        amplitudeSb.min = 30
        amplitudeSb.max = 60

        rightOffsetSb.min = -10
        rightOffsetSb.max = 10

        leftOffsetSb.min = -10
        leftOffsetSb.max = 10

        startPauseSb.min = 500
        startPauseSb.max = 5000

        frequencySb.min = (1/2)
        frequencySb.max = 1

        delayTimeSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                delayTimeValue.text = delayTimeSb.progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val textD ="D"+ delayTimeValue.text.toString()
                bluetoothController.sendMessage(textD)
            }
        })

        amplitudeSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                amplitudeValue.text = amplitudeSb.progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val textA ="A"+ amplitudeValue.text.toString()
                bluetoothController.sendMessage(textA)
            }
        })

        rightOffsetSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rightOffsetValue.text = rightOffsetSb.progress.toString()

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val textR ="R"+ rightOffsetValue.text.toString()
                bluetoothController.sendMessage(textR)
            }
        })

        leftOffsetSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                leftOffsetValue.text = leftOffsetSb.progress.toString()

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val textL ="L"+ leftOffsetValue.text.toString()
                bluetoothController.sendMessage(textL)
            }
        })

        startPauseSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                startPauseValue.text = startPauseSb.progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val textS ="S"+ startPauseValue.text.toString()
                bluetoothController.sendMessage(textS)
            }
        })

        frequencySb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                frequencyValue.text = frequencySb.progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val textF ="F"+ frequencyValue.text.toString()
                bluetoothController.sendMessage(textF)
            }
        })
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
