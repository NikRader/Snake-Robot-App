package com.example.bt_def

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bt_def.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar


class DeviceListFragment : Fragment(), ItemAdapter.Listener {
    private var preferences: SharedPreferences? = null
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var discoveryAdapter: ItemAdapter
    private var bAdapter: BluetoothAdapter? = null
    private lateinit var binding: FragmentListBinding
    private lateinit var btLauncher: ActivityResultLauncher<Intent>

    // Ниже написано второе разрешение
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>
    // Функция для создания разметки экрана
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }
    // Функция для работы с уже созданным экраном
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences =
            activity?.getSharedPreferences(BluetoothConstans.PREFERENCES, Context.MODE_PRIVATE)
        binding.bluetoothBt.setOnClickListener() {
            btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))

        }
        // Кнопка поиска bluetooth-устройств
        binding.searchBt.setOnClickListener() {

            try {
                if (bAdapter?.isEnabled == true) {  // если bluetooth включен
                    bAdapter?.startDiscovery()
                    it.visibility = View.GONE
                    binding.pbSearch.visibility = View.VISIBLE
                }
            } catch (e: SecurityException) {

            }
        }
        intentFilters()
        checkPermissions()
        initRcViews()
        registerBtLauncher()
        initBtAdapter()
        bluetoothState()
    }


    // Функция для заполения 2-х Recycler View
    private fun initRcViews() = with(binding) {
        rcViewPaired.layoutManager = LinearLayoutManager(requireContext())
        rcViewSearch.layoutManager = LinearLayoutManager(requireContext())
        itemAdapter = ItemAdapter(
            this@DeviceListFragment,
            false
        ) // указываем на сам фрагмент, а не на binding
        discoveryAdapter =
            ItemAdapter(this@DeviceListFragment, true) // указываем на сам фрагмент, а не на binding
        rcViewPaired.adapter = itemAdapter
        rcViewSearch.adapter = discoveryAdapter
    }

    // Функция получения списка сопряженных устройств
    private fun getPairedDevices() {
        try {
            val list = ArrayList<ListItem>()
            val deviceList = bAdapter?.bondedDevices as Set<BluetoothDevice>
            deviceList.forEach() {
                list.add(
                    ListItem(
                        it,
                        preferences?.getString(
                            BluetoothConstans.MAC,
                            ""
                        ) == it.address // Хитрое сравнение
                    )
                )
            }
            binding.emptyPaired.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            itemAdapter.submitList(list)

        } catch (e: SecurityException) {

        }
    }

    // Функция для инициализации bluetooth-адаптера
    private fun initBtAdapter() {
        val bManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }

    // Функция для отображения включения bluetooth на Android
    private fun bluetoothState() {
        if (bAdapter?.isEnabled == true) {
            changeButtonColor(binding.bluetoothBt, Color.GREEN)
            getPairedDevices()
        }
    }

    // Функция отображающая, включен(выключен) bluetooth
    private fun registerBtLauncher() {
        btLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            if (it.resultCode == Activity.RESULT_OK) {

                changeButtonColor(binding.bluetoothBt, Color.GREEN)
                getPairedDevices()
                Snackbar.make(binding.root, "Блютуз включен!", Snackbar.LENGTH_LONG).show()

            } else {
                Snackbar.make(binding.root, "Блютуз выключен!", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    // Функция для проверки разрешений
    private fun checkPermissions() {
        if (!checkBtPermissions()) {
            registerPermissionListener()
            launcherBtPermissions()
        }
    }

    // Функция для загрузки разрешений
    private fun launcherBtPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            pLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    // Функция для обработки получения разрешений
    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {

        }
    }

    // Функция для сохранения MAC-адреса выбранного устройства
    private fun saveMac(mac: String) {
        val editor = preferences?.edit()
        editor?.putString(BluetoothConstans.MAC, mac)
        editor?.apply()
    }

    // Функция сохраняющая Mac-адрес при нажатии
    override fun onClick(item: ListItem) {
        saveMac(item.device.address)
    }

    private val bReceiver = object : BroadcastReceiver() {
        // Функция для получения сообщений от системы
        override fun onReceive(p0: Context?, intent: Intent?) {
            if (intent?.action == BluetoothDevice.ACTION_FOUND) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val list = mutableListOf<ListItem>()
                list.addAll(discoveryAdapter.currentList)
                if (device != null) {
                    try {
                        if (device?.name != null) {
                            list.add(ListItem(device, false))
                        }

                    } catch (e: SecurityException) {

                    }
                }

                discoveryAdapter.submitList(list.toList())
                binding.emptySearch.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                try {
                    Log.d("MyLog", "Device: ${device?.name}")

                } catch (e: SecurityException) {

                }

            } else if (intent?.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                getPairedDevices()
            } else if (intent?.action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                binding.bluetoothBt.visibility = View.VISIBLE
                binding.pbSearch.visibility = View.GONE
            }
        }
    }

    // Функция для использования Интент-фильтров
    private fun intentFilters() {
        val f1 = IntentFilter(BluetoothDevice.ACTION_FOUND) // для ожидания найденных устройств
        val f2 =
            IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED) // для ожидания смены состояния сопряжения
        val f3 =
            IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) // для скрытия progress bar после завершения поиска
        // Регистрация Интент-фильтров в Activity
        activity?.registerReceiver(bReceiver, f1)
        activity?.registerReceiver(bReceiver, f2)
        activity?.registerReceiver(bReceiver, f3)
    }
}



