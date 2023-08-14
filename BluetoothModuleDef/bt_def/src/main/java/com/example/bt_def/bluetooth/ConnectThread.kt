package com.example.bt_def.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class ConnectThread ( device: BluetoothDevice) : Thread (){
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
        Log.d( "My Log", "Connecting..." )
        mSocket?.connect()
        Log.d( "My Log", "Connected" )
    } catch (e: IOException){ // про подключение
        Log.d( "My Log", "Not connected!" )
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