//package com.example.musicapp.ui.searchBar
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import com.example.musicapp.R
//import com.example.musicapp.ui.theme.MusicAppTheme
//
//@Composable
//fun SearchBar(
//    modifier: Modifier,
//    searchText: MutableState<String>
//) {
//    Row(
//        horizontalArrangement = Arrangement.End,
//        modifier = modifier
//            .padding(10.dp, 10.dp, 10.dp, 10.dp)
//            .fillMaxWidth()
//    ){
//        SearchTextField(
//            modifier = Modifier
//                .weight(1f)
//                .padding(0.dp, 0.dp, 10.dp, 0.dp),
//            searchText = searchText
//        )
//        SearchButton(
//            onClick = { /*TODO*/ },
//            painter = painterResource(id = R.drawable.baseline_filter_alt_24),
//            contentDescription = "filter")
//        SearchButton(
//            onClick = { /*TODO*/ },
//            painter = painterResource(id = R.drawable.baseline_swap_vert_24),
//            contentDescription = "filter")
//    }
//    SearchShadow()
//}
