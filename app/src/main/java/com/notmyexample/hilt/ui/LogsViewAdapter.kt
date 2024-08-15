package com.notmyexample.hilt.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.notmyexample.hilt.R
import com.notmyexample.hilt.data.Log
import com.notmyexample.hilt.util.DateFormatter

class LogsViewAdapter(
    private val daterFormatter: DateFormatter
) : RecyclerView.Adapter<LogsViewAdapter.LogsViewHolder>() {

    private val logsDataSet = mutableListOf<Log>()

    class LogsViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        return LogsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_text_row, parent, false) as TextView
        )
    }

    override fun getItemCount(): Int {
        return logsDataSet.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val log = logsDataSet[position]
        holder.textView.text = "${log.msg}\n\t${daterFormatter.formatDate(log.timestamp)}"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSet(list: List<Log>) {
        logsDataSet.clear()
        list.forEach { logsDataSet.add(it) }
        notifyDataSetChanged()
    }
}