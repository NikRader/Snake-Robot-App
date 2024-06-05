package com.example.bluetoothmoduledef


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bluetoothmoduledef.databinding.FragmentRobotControllBinding
import com.example.bt_def.R

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.Entry

class Robot_ControllFragment : Fragment() {

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
    val str2 =
        "I2:1)164,52;2)164,65;3)165,88;4)165,107;5)166,126;6)167,134;7)167,128;8)168,110;9)169,85;10)169,61;"
    val str3 =
        "I3:1)268,52;2)269,64;3)269,87;4)270,106;5)272,126;6)272,134;7)273,129;8)274,110;9)274,85;10)275,62;"
    val str4 =
        "I4:1)374,52;2)374,64;3)375,86;4)376,105;5)376,126;6)377,134;7)378,129;8)378,111;9)379,86;10)379,62;"
    val str5 =
        "I5:1)478,51;2)479,63;3)479,85;4)480,105;5)481,125;6)481,134;7)482,129;8)483,112;9)483,87;10)485,63;"

    val str6 =
        "I6:1)584,51;2)584,63;3)585,84;4)586,104;5)586,125;6)587,134;7)588,130;8)588,112;9)589,88;10)590,64;"
    val str7 =
        "I7:1)688,51;2)689,62;3)690,84;4)690,103;5)691,124;6)692,134;7)692,130;8)693,113;9)694,89;10)694,64;"
    val str8 =
        "I8:1)794,51;2)795,62;3)795,83;4)796,102;5)797,124;6)797,134;7)798,130;8)799,114;9)799,89;10)800,65;"
    val str9 =
        "I9:1)899,51;2)899,61;3)900,82;4)900,102;5)901,123;6)902,134;7)902,131;8)903,114;9)904,90;10)904,66;"
    val str10 =
        "I10:1)1004,51;2)1005,61;3)1005,81;4)1006,101;5)1007,123;6)1007,134;7)1008,131;8)1009,115;9)1009,91;10)1010,66;"


    val str_t = "t_1:1,10;2,20;3,30;4,40;5,50;6,60;7,70;8,80;9,90;10,100."
    val str_s = "s_1:1,0;2,15;3,30;4,45;5,60;6,75;7,90;8,105;9,120;10,135."
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRobotControllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("MyString", "Вывод координат графиков, где x - Время(мс), y - Положение угла(°)")
        graph_test(str1)
        graph_test(str2)
        graph_test(str3)
        graph_test(str4)
        graph_test(str6)
        graph_test(str7)
        graph_test(str8)
        graph_test(str9)
        graph_test(str10)


        draw_graph()
    }

    // Метод для оповещения о подлючении устройства


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

    private fun give_points_to_graphs() {

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

        print("Координаты серво 1:")
        println(graph1)
        print("Координаты серво 2:")
        println(graph2)
        print("Координаты серво 3:")
        println(graph3)
        println("******")
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
        print("Координаты серво 9:")
        println(graph9)
        print("Координаты серво10:")
        println(graph10)

    }

    fun draw_graph() {
        // График 1-го Серво
        val servo1 = binding.moveGraph1
        var dataSet1 = LineDataSet(graph1, "Углы поворота сервопривода 1")

        dataSet1.color = Color.BLACK
        dataSet1.lineWidth = 3f
        val lineData1 = LineData(dataSet1)
        servo1.data = lineData1

        // Точки
        dataSet1.valueTextColor = Color.BLUE
        dataSet1.valueTextSize = 10f

        val xAxis1 = servo1.xAxis
        xAxis1.position = XAxis.XAxisPosition.BOTTOM

        // График 2-го Серво
        val servo2 = binding.moveGraph2
        var dataSet2 = LineDataSet(graph2, "Углы поворота сервопривода 2")
        dataSet2.color = Color.BLACK
        dataSet2.lineWidth = 3f

        val lineData2 = LineData(dataSet2)
        servo2.data = lineData2

        // Точки
        dataSet2.valueTextColor = Color.BLUE
        dataSet2.valueTextSize = 10f
        val xAxis2 = servo2.xAxis
        xAxis2.position = XAxis.XAxisPosition.BOTTOM

        // График 3-го Серво
        val servo3 = binding.moveGraph3
        var dataSet3 = LineDataSet(graph3, "Углы поворота сервопривода 3")
        dataSet3.color = Color.BLACK
        dataSet3.lineWidth = 3f
        val lineData3 = LineData(dataSet3)
        servo3.data = lineData3

        // Точки
        dataSet3.valueTextColor = Color.BLUE
        dataSet3.valueTextSize = 10f

        val xAxis3 = servo3.xAxis
        xAxis3.position = XAxis.XAxisPosition.BOTTOM
    }

    fun graph() {

//        lineChart = binding.moveGraph1
//        val dataPairs = create_coords(str_t, str_s)
//
//
//
//        var lineDataSet = LineDataSet(entryList, "Углы поворота сервопривода 1")
//
//        var lineData = LineData(lineDataSet)
//        lineChart.data = lineData
//
//        // Точки
//        lineDataSet.valueTextColor = Color.BLUE
//        lineDataSet.valueTextSize = 10f
//
//
//        // Настройка оси X
//        val xAxis = lineChart.xAxis
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        lineChart.xAxis.setAxisTitleText("Time")
//        xAxis.textSize = 12f
//        xAxis.textColor = Color.BLACK
//
//
//        // Настройка оси Y
//        val leftAxis = lineChart.axisLeft
//        leftAxis.textSize = 12f
//        leftAxis.textColor = Color.BLACK
//
    }
}

private fun XAxis.setAxisTitleText(s: String) {

}
