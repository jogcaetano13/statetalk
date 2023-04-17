package com.joel.jlibtemplate

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.joel.jlibtemplate.adapters.ChallengeAdapter
import com.joel.jlibtemplate.adapters.ChallengeLoadStateAdapter
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
        val adapterWithFooter = adapter.withLoadStateFooter(ChallengeLoadStateAdapter())

        binding.itemsRv.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapterWithFooter
            it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                if (it.refresh is LoadState.Error) {
                    Toast.makeText(
                        this@MainActivity,
                        (it.refresh as LoadState.Error).error.message,
                        Toast.LENGTH_SHORT).show()
                }

                binding.loadingPb.isVisible = it.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launch {
            viewModel.getChallengesPaginated().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}