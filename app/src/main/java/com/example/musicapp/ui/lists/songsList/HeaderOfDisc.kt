package com.example.musicapp.ui.lists.songsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.musicapp.R

@Composable
fun HeaderOfDisc(){
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 0.dp)
    ){
        Text(
            text = "#",
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = "Title:",
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(8f)

        )
        Text(
            text = "Play Count:",
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(7f)
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_clock_24),
            contentDescription = "Time",
            tint = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .weight(1f)
        )
    }
}