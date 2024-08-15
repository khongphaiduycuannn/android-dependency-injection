package com.notmyexample.hilt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.notmyexample.hilt.R
import com.notmyexample.hilt.data.LoggerDataSource
import com.notmyexample.hilt.databinding.FragmentButtonsBinding
import com.notmyexample.hilt.di.InMemoryDataSource
import com.notmyexample.hilt.di.LocalDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ButtonsFragment : Fragment() {

    @LocalDataSource
    @Inject
    lateinit var loggerDataSource: LoggerDataSource

    private val binding by lazy { FragmentButtonsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button1.setOnClickListener {
            loggerDataSource.addLog("Interaction with 'Button 1'")
        }

        binding.button2.setOnClickListener {
            loggerDataSource.addLog("Interaction with 'Button 2'")
        }

        binding.button3.setOnClickListener {
            loggerDataSource.addLog("Interaction with 'Button 3'")
        }

        binding.deleteLogs.setOnClickListener {
            loggerDataSource.removeLogs()
        }

        binding.allLogs.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, LogsFragment())
                .addToBackStack(requireActivity().packageName)
                .commit()
        }
    }
}