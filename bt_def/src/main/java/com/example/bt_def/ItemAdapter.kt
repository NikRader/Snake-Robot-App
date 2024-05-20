package com.example.bt_def

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bt_def.databinding.ListItemBinding

class ItemAdapter(private val listener: Listener, val adapterType: Boolean) :
    ListAdapter<ListItem, ItemAdapter.MyHolder>(Comparator()) {
    private var oldCheckBox: CheckBox? = null

    class MyHolder
        (
        view: View, private val adapter: ItemAdapter,
        private val listener: Listener,
        val adapterType: Boolean
    ) : RecyclerView.ViewHolder(view) {
        private val b = ListItemBinding.bind(view)
        private var item1: ListItem? = null

        // для слушателя нажатия на checkbox либо весь itemview
        init {
            b.checkBox.setOnClickListener() {
                item1?.let { it1 -> listener.onClick(it1) }
                adapter.selectCheckbox(it as CheckBox)
            }
            // Для слушателя не только галочки
            itemView.setOnClickListener() {
                if (adapterType) {
                    try {
                        item1?.device?.createBond()
                    } catch (e: SecurityException) {

                    }

                } else {

                    item1?.let { it1 -> listener.onClick(it1) }
                    adapter.selectCheckbox(b.checkBox)
                }
            }
        }

        // Функция для заполения данных внутри каждого itemview
        fun bind(item: ListItem) = with(b) {
            checkBox.visibility = if (adapterType) View.GONE else View.VISIBLE
            item1 = item
            try {
                name.text = item.device.name

            } catch (e: SecurityException) {

            }

            mac.text = item.device.address
            if (item.isChecked) {
                adapter.selectCheckbox(checkBox)
            }
        }
    }
    // Класс для обновления данных внутри списков
    class Comparator :
        DiffUtil.ItemCallback<ListItem>() { // Передали, что будем сравнивать (ListItem)
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem == newItem
        }
    }
    // Функция для создания разметки списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyHolder(view, this, listener, adapterType)
    }
    // Функция для заполнения списка
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(getItem(position))
    }
    // Функция для отметки выбора устройства пользователем
    fun selectCheckbox(checkBox: CheckBox) {
        oldCheckBox?.text = " "
        oldCheckBox?.isChecked = false

        oldCheckBox = checkBox
        oldCheckBox?.isChecked = true
        oldCheckBox?.text = "выбрано"
    }
    // Интерфейс для обработки нажатия на itemview
    interface Listener {
        fun onClick(device: ListItem)
    }

}