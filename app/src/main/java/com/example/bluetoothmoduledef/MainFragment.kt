package com.example.bluetoothmoduledef

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
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
//        Log.d("MyString","Графики")
//        val list = create_coords(str_t, str_s)
//        for(i in 0..list.size-1){
//            println(list[i])
//        }
        initBtAdapter()
        seekbars()
        set_start_param()
        start_bluetooth()
        all_buttons()

    }

    private fun start_bluetooth() {
        val pref = activity?.getSharedPreferences(
            BluetoothConstans.PREFERENCES, Context.MODE_PRIVATE
        )
        val mac = pref?.getString(BluetoothConstans.MAC, "")
        bluetoothController = BluetoothController(btAdapter)

        binding.connectBt.setOnClickListener() {
            bluetoothController.connect(mac ?: "", this)
        }
    }

    private fun set_start_param() {
        binding.delayTimeSb.progress = 4
        binding.delayTimeValue.text = binding.delayTimeSb.progress.toString()
        binding.amplitudeSb.progress = 45
        binding.amplitudeValue.text = binding.amplitudeSb.progress.toString()
    }

    private fun all_buttons() {
        binding.toGraphBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_robot_ControllFragment)
        }

        binding.bList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_deviceListFragment)
        }

        binding.StartPosBtn.setOnClickListener {
            start_pos()
        }
        binding.ForwardBtn.setOnClickListener {
            forward_move()
        }
        binding.BackwardBtn.setOnClickListener {
            backward_move()
        }
        binding.LeftBtn.setOnClickListener {
            left_move()
        }
        binding.RightBtn.setOnClickListener {
            right_move()
        }
    }

    private fun start_pos() {
        bluetoothController.sendMessage("i1")
        println("start_pos")
    }

    private fun forward_move() {
        bluetoothController.sendMessage("f1")
        println("forward_move")
    }

    private fun backward_move() {
        bluetoothController.sendMessage("b1")
        println("back_move")
    }

    private fun left_move() {
        bluetoothController.sendMessage("l1")
        println("left_move")
    }

    private fun right_move() {
        bluetoothController.sendMessage("r1")
        println("right_move")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun seekbars() = with(binding) {


        // Новый цвет, например, красный
        val color1 =ContextCompat.getColor(requireContext(), R.color.dark_red)
        val color2 = ContextCompat.getColor(requireContext(), R.color.dark_red)
        val color3 = ContextCompat.getColor(requireContext(), R.color.dark_red)
        val color4 = ContextCompat.getColor(requireContext(), R.color.dark_red)
// Устанавливаем цвет полосы прогресса Времени
        delayTimeSb.getProgressDrawable()
            .setColorFilter(PorterDuffColorFilter(color1, PorterDuff.Mode.SRC_IN))
// Устанавливаем цвет ползунка
        delayTimeSb.getThumb().setColorFilter(color1, PorterDuff.Mode.SRC_IN)

        // Устанавливаем цвет полосы прогресса Амплитуды
        amplitudeSb.getProgressDrawable()
            .setColorFilter(PorterDuffColorFilter(color2, PorterDuff.Mode.SRC_IN))
// Устанавливаем цвет ползунка
        amplitudeSb.getThumb().setColorFilter(color2, PorterDuff.Mode.SRC_IN)

// Устанавливаем цвет полосы прогресса Сдвига вправо
        rightOffsetSb.getProgressDrawable()
            .setColorFilter(PorterDuffColorFilter(color3, PorterDuff.Mode.SRC_IN))
// Устанавливаем цвет ползунка
        rightOffsetSb.getThumb().setColorFilter(color3, PorterDuff.Mode.SRC_IN)

        // Устанавливаем цвет полосы прогресса Сдвига влево
        leftOffsetSb.getProgressDrawable()
            .setColorFilter(PorterDuffColorFilter(color4, PorterDuff.Mode.SRC_IN))
// Устанавливаем цвет ползунка
        leftOffsetSb.getThumb().setColorFilter(color4, PorterDuff.Mode.SRC_IN)
        // Границы значений

        delayTimeSb.min = 1
        delayTimeSb.max = 7

        amplitudeSb.min = 30
        amplitudeSb.max = 60

        rightOffsetSb.min = -10
        rightOffsetSb.max = 10

        leftOffsetSb.min = -10
        leftOffsetSb.max = 10

//        startPauseSb.min = 500
//        startPauseSb.max = 5000
//
//        offsetSb.min = 1
//        offsetSb.max = 10

        delayTimeSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                delayTimeValue.text = delayTimeSb.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val textD = "D" + delayTimeValue.text.toString()
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
                val textA = "A" + amplitudeValue.text.toString()
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
                val textR = "R" + rightOffsetValue.text.toString()
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
                val textL = "L" + leftOffsetValue.text.toString()
                bluetoothController.sendMessage(textL)
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
                    var textBat = message
                    // Регулярное выражение для поиска целых чисел
                    val pattern = Regex("\\d")

                    if (pattern.containsMatchIn(textBat)) {
                        try {
                            var bat = textBat.toIntOrNull()
                            if (bat != null) {
                                Log.d("", "Вольт до: <$bat>")
                                var volts_old = bat.toFloat()
                                val a = 100
                                var volts = (bat.toDouble() / a.toDouble())
                                Log.d("", "Вольт после: <$volts>")
                                var str_volts = volts.toString()
                                var str_show = "Напряжение: $str_volts вольт"
                                binding.voltsShowBtn.text = str_show
                                var new_bat = (bat - 700) / (100 * 0.014)
                                val roundedbut = Math.round(new_bat).toInt()
                                if (roundedbut < 20) {
                                    binding.voltsBatTv.setTextColor(Color.parseColor("#F44336"))
                                }
                                if (roundedbut >= 20 && roundedbut < 60) {
                                    binding.voltsBatTv.setTextColor(Color.parseColor("#DDD160"))
                                }
                                if (roundedbut > 60) {
                                    binding.voltsBatTv.setTextColor(Color.parseColor("#36B63B"))
                                }
                                val str_roundbut = roundedbut.toString()
                                val my_str = "Заряд аккумулятора: $str_roundbut %"
                                binding.voltsBatTv.text = my_str
                                Log.d("", "Заряд аккумулятора: <$str_roundbut>%")
                            }


                        } catch (e: NumberFormatException) {
                            println("Произошла ошибка: ${e.message}")
                            // Обработка ошибки, например, уведомление пользователя
                        }

                    } else {
                        Log.d("", "Строка не содержит целых чисел")
                    }
                }
            }
        }
    }


}
