package com.notmyexample.hilt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.notmyexample.hilt.data.LoggerDataSource
import com.notmyexample.hilt.databinding.FragmentLogsBinding
import com.notmyexample.hilt.di.InMemoryDataSource
import com.notmyexample.hilt.di.LocalDataSource
import com.notmyexample.hilt.util.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LogsFragment : Fragment() {

    private val binding by lazy { FragmentLogsBinding.inflate(layoutInflater) }

    @LocalDataSource
    @Inject
    lateinit var loggerDataSource: LoggerDataSource

    @Inject
    lateinit var dateFormatter: DateFormatter

    private val logsViewAdapter by lazy { LogsViewAdapter(dateFormatter) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = logsViewAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loggerDataSource.getAllLogs().observe(viewLifecycleOwner) {
            logsViewAdapter.setDataSet(it)
        }
    }
}