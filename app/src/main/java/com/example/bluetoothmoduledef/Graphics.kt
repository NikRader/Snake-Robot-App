package com.example.bluetoothmoduledef

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import kotlin.math.cos
import kotlin.math.sin

class Graphics : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphics)

        val lineChartCos = findViewById<LineChart>(R.id.g1)
        val lineChartDerivative = findViewById<LineChart>(R.id.g2)

        val entriesCos = ArrayList<Entry>()
        val entriesDerivative = ArrayList<Entry>()

        for (x in -10..10) {
            val yCos = cos(x.toDouble())
            val yDerivative = -sin(x.toDouble())

            entriesCos.add(Entry(x.toFloat(), yCos.toFloat()))
            entriesDerivative.add(Entry(x.toFloat(), yDerivative.toFloat()))
        }

        val dataSetCos = LineDataSet(entriesCos, "cos(x)")
        dataSetCos.color = Color.BLUE

        val dataSetDerivative = LineDataSet(entriesDerivative, "derivative of cos(x)")
        dataSetDerivative.color = Color.RED

        val dataCos = LineData(dataSetCos)
        lineChartCos.data = dataCos
        lineChartCos.invalidate()

        val dataDerivative = LineData(dataSetDerivative)
        lineChartDerivative.data = dataDerivative
        lineChartDerivative.invalidate()
    }
}

