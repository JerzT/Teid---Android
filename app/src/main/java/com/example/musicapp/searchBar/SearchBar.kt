package com.example.musicapp.searchBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicapp.R
import com.example.musicapp.ui.theme.MusicAppTheme

@Preview(
    widthDp = 300,
    heightDp = 650,
)
@Composable
fun SearchBar() = Row(
    horizontalArrangement = Arrangement.End,
    modifier = Modifier
        .padding(10.dp, 10.dp, 0.dp, 10.dp)
){
    MusicAppTheme {
        SearchTextField(
            modifier = Modifier.weight(1f)
        )
        SearchIcon(
            onClick = { /*TODO*/ },
            painter = painterResource(id = R.drawable.baseline_settings_24),
            contentDescription = "filtr")
    }
}
