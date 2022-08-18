package com.example.googlebooksapi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.googlebooksapi.ui.theme.GoogleBooksApiTheme
import com.example.googlebooksapi.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp {
                val viewModel: BookViewModel by viewModels()
                SearchScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainApp(content: @Composable ()->Unit) {
    GoogleBooksApiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ){
            content()
        }
    }
}

@Composable
fun SearchScreen(viewModel: BookViewModel) {

    var bookQuery by remember {
        mutableStateOf("")
    }
    var bookQuerySize by remember {
        mutableStateOf("")
    }
    var selectedPrintType by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 14.dp
    ) {
        Column {
            TextField(
                onValueChange = { bookQuery = it },
                value = bookQuery,
                label = { Text(text = "Search by book name") }
            )
            TextField(
                value = bookQuerySize,
                onValueChange = { bookQuerySize = it },
                label = { Text(text = "Search results") }
            )
            BookPrintType { printType ->
                selectedPrintType = printType
            }
            Button(onClick = {
                Toast.makeText(
                    context,
                    selectedPrintType,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.searchBook(bookQuery, bookQuerySize, selectedPrintType)
            }) {
                Text(text = "Search")
            }
        }
    }
}

@Composable
fun BookPrintType(selected: (String) -> Unit) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var selection by remember {
        mutableStateOf("")
    }
    val dropDownIcon = if (isExpanded) Icons.Filled.KeyboardArrowUp
    else Icons.Filled.KeyboardArrowDown
    Column(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(

                text = selection
            )
            Spacer(modifier =Modifier.width(30.dp))
            Icon(
                imageVector = dropDownIcon,
                contentDescription = "A dropdown icon",
                modifier =
                Modifier
                    .clickable { isExpanded = !isExpanded }
                    .fillMaxWidth())
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }) {
            val context = LocalContext.current
            context.resources.getStringArray(R.array.book_print_type).forEach { printType ->
                DropdownMenuItem(onClick = {
                    isExpanded = false
                    selected(printType)
                    selection = printType
                }) {
                    Text(text = printType)
                }
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
fun SearchScreenPreview() {
    val viewModel: BookViewModel = BookViewModel()
    MainApp {
        SearchScreen(viewModel)
    }
}