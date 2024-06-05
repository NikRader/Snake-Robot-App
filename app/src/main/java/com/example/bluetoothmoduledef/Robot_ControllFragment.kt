package com.example.bluetoothmoduledef


import android.graphics.Color
import android.os.Bundle
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
        lineChart = binding.lineChart1
          graph()
    }

    // Метод для оповещения о подлючении устройства


    fun parse_string(str: String): MutableList<Int> {
        val myList = mutableListOf<Int>()
        var new = str.substringAfter(":")
        var new2 = new.dropLast(1)
        var parts = new2.split(";") // Разделение строки по запятым
        for (part in parts) {
            val new_part = part.substringAfter(",")
            myList.add(new_part.toInt())
        }
        return myList
    }

    fun create_coords(str_t: String, str_s: String): MutableList<Pair<Int, Int>> {
        val timelist = parse_string(str_t)
        val movelist = parse_string(str_s)
        val combinedList: MutableList<Pair<Int, Int>> = timelist.zip(movelist).toMutableList()
        return combinedList
    }

    fun graph() {

        lineChart = binding.lineChart1
         val dataPairs = create_coords(str_t, str_s)
        val entryList: MutableList<Entry> = dataPairs.map { pair ->
            Entry(pair.first.toFloat(), pair.second.toFloat())
        }.toMutableList()



        var lineDataSet = LineDataSet(entryList, "Углы поворота сервопривода")

        var lineData = LineData(lineDataSet)
        lineChart.data = lineData

        // Точки
        lineDataSet.valueTextColor = Color.BLUE
        lineDataSet.valueTextSize = 10f

        // Подписываем ось X


        // Подписываем ось Y

        //

        // Настройка оси X
        val xAxis = lineChart.xAxis
        xAxis.position =  XAxis.XAxisPosition.BOTTOM
            lineChart.xAxis.setAxisTitleText("Time")
        xAxis.textSize = 12f
        xAxis.textColor = Color.BLACK


        // Настройка оси Y
        val leftAxis = lineChart.axisLeft
        leftAxis.textSize = 12f
        leftAxis.textColor = Color.BLACK

    }
}

private fun XAxis.setAxisTitleText(s: String) {

}
