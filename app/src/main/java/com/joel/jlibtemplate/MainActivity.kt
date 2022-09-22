package com.joel.jlibtemplate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.joel.communication.extensions.toModel
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.states.ResultState
import com.joel.jlibtemplate.adapters.ChallengeAdapter
import com.joel.jlibtemplate.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = ChallengeAdapter()

        CommunicationRequest.testRequest()

        binding.itemsRv.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }

        lifecycleScope.launch {
            viewModel.getChallenges().collectLatest {
                when(it) {
                    ResultState.Empty -> {}
                    is ResultState.Error -> {
                        binding.loadingPb.isVisible = false
                        val error = it.error.toModel<ErrorModel>()
                        print(error)
                    }
                    ResultState.Loading -> binding.loadingPb.isVisible = true
                    is ResultState.Success -> {
                        binding.loadingPb.isVisible = false
                        val data = it.data
                        println(data)
                    }
                }
            }
        }
    }
}