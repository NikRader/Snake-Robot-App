package com.example.bt_def.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class ConnectThread ( device: BluetoothDevice, val listener: BluetoothController.Listener) : Thread (){
    private val uuid =  "00001101-0000-1000-8000-00805F9B34FB" // идунтификатор чтоб наладить связь
    private var mSocket: BluetoothSocket? = null
    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException){


        } catch (se: SecurityException){


        }
    }

override fun run(){

    try {

        mSocket?.connect()
        listener.onReceive(BluetoothController.BLUETOOTH_CONNECTED)
    } catch (e: IOException){ // про подключение
        listener.onReceive(BluetoothController.BLUETOOTH_NO_CONNECTED)
    } catch (se: SecurityException){ // про безопасность

    }

}
    // Прерывание подключения
fun closeConnection(){
    try {
        mSocket?.close()
    } catch (e: IOException){


    } catch (se: SecurityException){


    }

}

}