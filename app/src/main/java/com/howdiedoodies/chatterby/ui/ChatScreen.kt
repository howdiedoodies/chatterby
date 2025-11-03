// This file has been commented out because of a build error that could not be resolved.
// The error is related to the `ditchoom/websocket` library, which is not in a usable state.
// The `README.md` is outdated, and the library is missing the methods that are advertised in the documentation.
package com.howdiedoodies.chatterby.ui

//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.howdiedoodies.chatterby.viewmodel.ChatViewModel
//
//@Composable
//fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
//    val messages by viewModel.messages.collectAsState()
//    var text by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        LazyColumn(
//            modifier = Modifier.weight(1f)
//        ) {
//            items(messages) { message ->
//                Text(text = message)
//            }
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = text,
//                onValueChange = { text = it },
//                modifier = Modifier.weight(1f)
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Button(onClick = {
//                viewModel.sendMessage(text)
//                text = ""
//            }) {
//                Text(text = "Send")
//            }
//        }
//    }
//}
