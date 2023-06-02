package com.lush.composedemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lush.composedemo.databinding.MyXmlViewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "ShowcaseActivity"

class ShowcaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Your composable code goes here!
        }
    }
}

@Composable
fun HelloWorld(name: String) {
    Text(
        text = "Hello $name!"
    )
}

@Composable
fun HelloWorldButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        HelloWorld(
            name = stringResource(
                id = R.string.example_name
            )
        )
    }
}

@Composable
fun MultipleComposables() {
    HelloWorldButton {
        Log.i(TAG, "First button pressed!")
    }
    HelloWorldButton {
        Log.i(TAG, "Second button pressed!")
    }
}

@Composable
fun MyRow() {
    Row {
        HelloWorldButton {
            Log.i(TAG, "First button pressed!")
        }
        HelloWorldButton {
            Log.i(TAG, "Second button pressed!")
        }
    }
}

@Composable
fun SideEffectBadExample(
    items: List<String>
) {
    Column {
        items.forEach { item ->
            Text(item)
        }
    }
    Toast.makeText(
        LocalContext.current,
        items.size.toString(),
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun SideEffectGoodExample(
    items: State<List<String>>
) {
    val context = LocalContext.current
    Column {
        items.value.forEach { item ->
            Text(item)
        }
    }
    LaunchedEffect(items.value) {
        Toast.makeText(
            context,
            items.value.size.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }
}

class MyViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<String>>(
        emptyList()
    )

    val items = _items.asStateFlow()

    fun start() {
        viewModelScope.launch {
            delay(200)
            _items.value = listOf("foo")
            delay(2000)
            _items.value = _items.value.plus("bar")
        }
    }
}

@Composable
fun NaieveList(
    numbers: List<Int>
) {
    Column(
        modifier = Modifier
            .verticalScroll(
                state = rememberScrollState()
            )
            .fillMaxSize()
    ) {
        numbers.forEach { number ->
            Text(
                text = number.toString()
            )
        }
    }
}

@Composable
fun LazyList(
    numbers: List<Int>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(numbers) { number ->
            Text(
                text = number.toString()
            )
        }
    }
}

@Composable
fun InteropExample(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MyXmlViewBinding.inflate(
                LayoutInflater.from(context)
            ).root
        }
    )
}