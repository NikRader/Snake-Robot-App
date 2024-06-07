package com.example.bluetoothmoduledef


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bluetoothmoduledef.databinding.FragmentRobotControllBinding
import com.example.bt_def.bluetooth.BluetoothController

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry

class Robot_ControllFragment : Fragment(), BluetoothController.Listener {

    private lateinit var binding: FragmentRobotControllBinding
    private lateinit var lineChart: LineChart

    var graph1: MutableList<Entry> = mutableListOf()
    var graph2: MutableList<Entry> = mutableListOf()
    var graph3: MutableList<Entry> = mutableListOf()
    var graph4: MutableList<Entry> = mutableListOf()
    var graph5: MutableList<Entry> = mutableListOf()
    var graph6: MutableList<Entry> = mutableListOf()
    var graph7: MutableList<Entry> = mutableListOf()
    var graph8: MutableList<Entry> = mutableListOf()
    var graph9: MutableList<Entry> = mutableListOf()
    var graph10: MutableList<Entry> = mutableListOf()

    val str1 =
        "I1:1)69,52;2)70,65;3)70,88;4)71,107;5)72,127;6)72,134;7)73,128;8)73,109;9)74,84;10)75,61;"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRobotControllBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.messageBtn.setOnClickListener {
            val text = arguments?.getString("MyArg")
            val new_text = text?.dropLast(1)
            // Получим строки для парсинга
            val work_str = new_text?.split('A')
            for(work in work_str!!){
                try{
                    graph_test(work)
                } catch (e:NumberFormatException){
                    println("null pointer!")
                }
                draw_graph()
            }
        }
    }

    fun parse_string(string: String): MutableList<Entry> {
        val myList = mutableListOf<Int>()
        val timeList = mutableListOf<Int>()
        val moveList = mutableListOf<Int>()

        var iter_string = string.substringBefore(":")
        val iter_num = iter_string.substring(1)
        Log.d("MyString", "Номер итерации: $iter_num")
//        println(string)
        var new = string.substringAfter(":")
        var new2 = new.dropLast(1)
        var parts = new2.split(";") // Разделение строки по запятым
        for (part in parts) {

            val new_part = part.substringAfter(")")
            val part_t = new_part.substringBefore(",")
            val part_s = new_part.substringAfter(",")

            timeList.add(part_t.toInt())
            moveList.add(part_s.toInt())
        }
//        Log.d("MyString", "Выведем Координаты(<время, угол>) c 1-го по 10-ый сервопривод за $iter_num-ую итерацию:")
        val combinedList: MutableList<Pair<Int, Int>> = timeList.zip(moveList).toMutableList()

        val entryList: MutableList<Entry> = combinedList.map { pair ->
            Entry(pair.first.toFloat(), pair.second.toFloat())
        }.toMutableList()

        return entryList
    }


    fun graph_test(str: String) {
        val test = parse_string(str)

        graph1.add(test[0])
        graph2.add(test[1])
        graph3.add(test[2])
        graph4.add(test[3])
        graph5.add(test[4])
        graph6.add(test[5])
        graph7.add(test[6])
        graph8.add(test[7])
        graph9.add(test[8])
        graph10.add(test[9])

//        print("Координаты серво 1:")
//        println(graph1)
//        print("Координаты серво 2:")
//        println(graph2)
//        print("Координаты серво 3:")
//        println(graph3)
//        println("******")
//        print("Координаты серво 4:")
//        println(graph4)
//        print("Координаты серво 5:")
//        println(graph5)
//        print("Координаты серво 6:")
//        println(graph6)
//        print("Координаты серво 7:")
//        println(graph7)
//        print("Координаты серво 8:")
//        println(graph8)
//        print("Координаты серво 9:")
//        println(graph9)
//        print("Координаты серво10:")
//        println(graph10)

    }

    fun draw_graph() {
        // График 1-го Серво
        val servo1 = binding.moveGraph1
        var dataSet1 = LineDataSet(graph7, "Углы поворота сервопривода 7")

        dataSet1.color = Color.BLACK
        dataSet1.lineWidth = 3f
        // Точки
       // dataSet1.valueTextColor = Color.BLUE
      //  dataSet1.valueTextSize = 10f

        val xAxis1 = servo1.xAxis
        xAxis1.position = XAxis.XAxisPosition.BOTTOM
        val lineData1 = LineData(dataSet1)
        servo1.data = lineData1
        servo1.invalidate()


        // График 2-го Серво
        val servo2 = binding.moveGraph2
        var dataSet2 = LineDataSet(graph8, "Углы поворота сервопривода 8")
        dataSet2.color = Color.BLACK
        dataSet2.lineWidth = 3f
        // Точки
      //  dataSet2.valueTextColor = Color.BLUE
      //  dataSet2.valueTextSize = 10f
        val xAxis2 = servo2.xAxis
        xAxis2.position = XAxis.XAxisPosition.BOTTOM
        val lineData2 = LineData(dataSet2)
        servo2.data = lineData2
        servo2.invalidate()

        // График 3-го Серво
        val servo3 = binding.moveGraph3
        var dataSet3 = LineDataSet(graph9, "Углы поворота сервопривода 9")
        dataSet3.color = Color.BLACK
        dataSet3.lineWidth = 3f

        // Точки
      //  dataSet3.valueTextColor = Color.BLUE
       // dataSet3.valueTextSize = 10f

        val xAxis3 = servo3.xAxis
        xAxis3.position = XAxis.XAxisPosition.BOTTOM
        val lineData3 = LineData(dataSet3)
        servo3.data = lineData3
        servo3.invalidate()

    }

    override fun onReceive(message: String) {
        activity?.runOnUiThread {
            if (message.isNotEmpty()) {
                val a = message[0]
                if (a == 'I') {
                    Log.d("MySting", "$message")
                }
            }
        }
    }

    private fun XAxis.setAxisTitleText(s: String) {

    }
}
