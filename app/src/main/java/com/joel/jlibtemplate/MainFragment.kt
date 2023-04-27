package com.joel.jlibtemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.joel.jlibtemplate.extensions.composeTheme
import com.joel.jlibtemplate.ui.theme.CommunicationComposeTheme

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = requireContext().composeTheme {
        MainScreen(
            goToCompose = {
                findNavController().navigate(R.id.action_mainFragment_to_composeExampleFragment)
            },
            goToXml = {
                findNavController().navigate(R.id.action_mainFragment_to_xmlExampleFragment)
            }
        )
    }
}

@Composable
private fun MainScreen(
    goToCompose: () -> Unit,
    goToXml: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = goToCompose) {
            Text(text = "Go to compose example")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = goToXml) {
            Text(text = "Go to XML example")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CommunicationComposeTheme {
        MainScreen(
            goToCompose = {},
            goToXml = {}
        )
    }
}