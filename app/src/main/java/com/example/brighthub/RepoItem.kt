package com.example.brighthub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brighthub.ui.theme.BrightHubTheme

@Composable
fun RepoItem(owner: String, name: String, topContributor: String? = null) {
    Card(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Column {
            Text("$owner/$name", style = MaterialTheme.typography.bodyLarge)
            Row(Modifier.padding(start = 32.dp)) {
                Text("top contributor: ")
                Text(
                    topContributor ?: "...",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoItemPreview() {
    BrightHubTheme {
        RepoItem("freeCodeCamp", "freeCodeCamp")
    }
}