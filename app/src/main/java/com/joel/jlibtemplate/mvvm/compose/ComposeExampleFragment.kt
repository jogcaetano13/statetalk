package com.joel.jlibtemplate.mvvm.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joel.communication.states.ResultState
import com.joel.jlibtemplate.extensions.composeTheme
import com.joel.jlibtemplate.models.Challenge
import com.joel.jlibtemplate.ui.theme.CommunicationComposeTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeExampleFragment : Fragment() {
    private val viewModel by viewModel<ComposeExampleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = requireContext().composeTheme {
        val state by viewModel.challengesState.collectAsStateWithLifecycle()
        ComposeExampleScreen(state = state)
    }
}

@Composable
private fun ComposeExampleScreen(
    state: ResultState<List<Challenge>>
) {
    val context = LocalContext.current

    when(state) {
        ResultState.Empty -> {}
        is ResultState.Error -> {
            if (!state.data.isNullOrEmpty()) {
                Toast.makeText(context, state.error.errorBody.orEmpty(), Toast.LENGTH_SHORT).show()
                BodyScreen(challenges = state.data!!)

            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.error.errorBody.orEmpty())
                }
            }
        }
        is ResultState.Loading -> {
            if (!state.data.isNullOrEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator()
                    BodyScreen(challenges = state.data!!)
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        is ResultState.Success -> {
            BodyScreen(challenges = state.data)
        }
    }
}

@Composable
private fun BodyScreen(
    modifier: Modifier = Modifier,
    challenges: List<Challenge>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(challenges) { item ->
            ListItem(item = item)
            Divider()
        }
    }
}

@Composable
private fun ListItem(
    item: Challenge
) {
    Text(
        modifier = Modifier.padding(10.dp),
        text = item.name.orEmpty()
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewComposeExampleScreen() {
    CommunicationComposeTheme {
        ComposeExampleScreen(
            state = ResultState.Empty
        )
    }
}