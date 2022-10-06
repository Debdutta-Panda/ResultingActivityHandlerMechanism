package com.debduttapanda.resultingactivityhandlermechanism

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.debduttapanda.resultingactivityhandlermechanism.ui.theme.ResultingActivityHandlerMechanismTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResultingActivityHandlerMechanismTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text("Activity for result demo(Image picking)")
                        Divider()
                        PickImageWithMechanism()
                        Divider()
                        PickImage()
                    }
                }
            }
        }
    }
}

class PickImageViewModel: ViewModel(){
    val uri = mutableStateOf("")
    fun onResult(it: Uri?) {
        uri.value = it?.toString()?:"null"
    }
}

@Composable
fun PickImage(
    vm: PickImageViewModel = viewModel()
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            vm.onResult(it)
        }
    )
    Box(
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Without mechanism")
            Button(onClick = {
                imagePicker.launch("image/*")
            }) {
                Text("Pick")
            }
            Text(vm.uri.value)
        }
    }
}
////////////////////////////////////////////////////////////
class PickImageWithMechanismViewModel: ViewModel(){
    val uri = mutableStateOf("")
    fun pickImage() {
        viewModelScope.launch {
            val result = resultingActivityHandler.getContent("image/*")
            uri.value = result?.toString()?:"null"
        }
    }

    val resultingActivityHandler = ResultingActivityHandler()
}

@Composable
fun PickImageWithMechanism(
    vm: PickImageWithMechanismViewModel = viewModel()
) {
    vm.resultingActivityHandler.handle()
    Box(
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("With mechanism")
            Button(onClick = {
                vm.pickImage()
            }) {
                Text("Pick")
            }
            Text(vm.uri.value)
        }
    }
}