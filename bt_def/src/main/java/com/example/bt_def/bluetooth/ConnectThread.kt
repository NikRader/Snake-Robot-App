package com.example.bt_def.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*
// Класс для передачи/получения данных по bluetooth на второстепенном потоке
class ConnectThread(device: BluetoothDevice, val listener: BluetoothController.Listener) :
    Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB" // идентификатор чтоб наладить связь
    private var mSocket: BluetoothSocket? = null

    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException) {

        } catch (se: SecurityException) {

        }
    }
    // Функция для запуска на второстепенном потоке
    override fun run() {
        try {
            mSocket?.connect()
            listener.onReceive(BluetoothController.BLUETOOTH_CONNECTED)
            readMessage()
        } catch (e: IOException) { // про подключение
            listener.onReceive(BluetoothController.BLUETOOTH_NO_CONNECTED)
        } catch (se: SecurityException) { // про безопасность

        }
    }
    // Функция для считывания полученного сообщения
    private fun readMessage() {
        // размер массива Байт
        val buffer = ByteArray(256)
        while (true) {
            try {
                val length = mSocket?.inputStream?.read(buffer)
                val message = String(buffer, 0, length ?: 0)
                listener.onReceive(message)
            } catch (e: IOException) {
                break
            }
        }
    }
    // Функция для отправки сообщения
    fun sendMessage(message: String) {
        try {
            mSocket?.outputStream?.write(message.toByteArray())
        } catch (e: IOException) {

        }
    }

    // Функция для прерывания подключения
    fun closeConnection() {
        try {
            mSocket?.close()
        } catch (e: IOException) {

        } catch (se: SecurityException) {
        }

    }

}