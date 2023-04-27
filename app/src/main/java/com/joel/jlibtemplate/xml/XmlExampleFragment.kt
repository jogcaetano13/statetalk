package com.joel.jlibtemplate.xml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.joel.jlibtemplate.adapters.ChallengeAdapterPaging
import com.joel.jlibtemplate.adapters.ChallengeLoadStateAdapter
import com.joel.jlibtemplate.databinding.FragmentXmlExampleBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class XmlExampleFragment : Fragment() {
    private lateinit var binding: FragmentXmlExampleBinding

    private val viewModel by viewModel<XmlExampleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentXmlExampleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChallengeAdapterPaging()
        val adapterWithFooter = adapter.withLoadStateFooter(ChallengeLoadStateAdapter())

        binding.itemsRv.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapterWithFooter
            it.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                if (it.refresh is LoadState.Error) {
                    Toast.makeText(
                        requireActivity(),
                        (it.refresh as LoadState.Error).error.message,
                        Toast.LENGTH_SHORT).show()
                }

                binding.loadingPb.isVisible = it.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launch {
            viewModel.pagingState.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}