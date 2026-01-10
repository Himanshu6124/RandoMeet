package com.example.vibechat.ui.screens.chatscreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibechat.core.utils.EMPTY
import com.example.vibechat.ui.commoncomposables.HorizontalSpacer
import com.example.vibechat.ui.commoncomposables.LoadNetworkImage
import com.example.vibechat.ui.commoncomposables.TextComposable
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.MessageCard
import com.example.vibechat.ui.screens.matchscreen.ChatCardData
import com.example.vibechat.ui.screens.matchscreen.MessageStatus
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import vibechat.composeapp.generated.resources.Res
import vibechat.composeapp.generated.resources.edit_24
import vibechat.composeapp.generated.resources.outline_arrow_forward_24
import vibechat.composeapp.generated.resources.user_profile

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    userId: String,
    isRandomMatch : Boolean,
    chat : ChatCardData?,
    navigateBack : ()-> Unit,
) {
    val viewModel : ChatScreenViewModel = koinViewModel()
    var inputText by remember { mutableStateOf("") }
//    val messages = viewModel.uiState.collectAsState().value.messages
    val isOnline = viewModel.isOnline.collectAsState()
    val isTyping = viewModel.isTyping.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val message =  viewModel.message.collectAsState()

    fun addMessage(newMessage: Message) {
        viewModel.addMessage(newMessage)
        inputText = ""
//        scope.launch {
//            val index = messages.size - 1
//            if (index != -1) {
//                listState.animateScrollToItem(messages.size - 1)
//            }
//        }
    }

    LaunchedEffect(message) {
        message.value?.let { addMessage(it) }
    }


//    LaunchedEffect(Unit) {
//        viewModel.getMessages(chat?.conversationId ?: "")
//        viewModel.connectToSocket(
//            friendUserId = chat?.friendUserId ?: "",
//            conversationId = chat?.conversationId ?: "",
//            senderId = userId.orEmpty()
//        )
//        viewModel.sendOnlineStatus(userId ?: "", chat?.conversationId ?: "")
//        viewModel.getUserStatus(
//            friendId = chat?.friendUserId.orEmpty(),
//            conversationId = chat?.conversationId.orEmpty()
//        )
//    }


    fun deleteMessage(message: Message) {
//        messages.remove(message)
    }

    Scaffold(
        topBar = {
            if (chat != null) {
                ChatScreenTopBar(
                    chat = chat,
                    isOnline = isOnline.value,
                    onBackPress = navigateBack,
                    onAddFriend = {
                        viewModel.sendFriendRequest(
                            userId = userId.orEmpty(),
                            friendId = chat.friendUserId
                        )
                    }
                )
            }
        },
    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .background(Color.Transparent)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState
            ) {
//                items(messages) { message ->
//                    MessageCard(userId, message) {
//                        deleteMessage(it)
//                    }
//                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                if (isTyping.value) {
                    Text(
                        "typing...",
                        modifier = Modifier.padding(
                            start = 20.dp,
                            bottom = 5.dp
                        )
                    )
                }
                SendMessageButton(
                    userId = userId ?: "",
                    conversationId = chat?.conversationId ?: "",
                    inputText = inputText,
                    onTextUpdate = {
                        inputText = it
                        viewModel.onUserTyping(
                            senderId = userId ?: "",
                            conversationId = chat?.conversationId.orEmpty(),
                            inputText = it
                        )
                    },
                    onSendMessage = {
                        viewModel.sendMessage(message = it, isRandom = isRandomMatch)
                        addMessage(it)
                    }
                )
            }

        }
    }
}

@Composable
fun SendMessageButton(
    userId : String,
    conversationId :String,
    inputText : String,
    onTextUpdate : (String)-> Unit,
    onSendMessage : (Message)-> Unit,
) {
    Row(
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputText,
            leadingIcon =  {
                Image(
                    painter = painterResource( Res.drawable.edit_24),
                    contentDescription = null
                )
            } ,
            trailingIcon = {
                Image(
                    painter = painterResource( Res.drawable.edit_24),
                    contentDescription = null,
                    modifier = Modifier.clickable {

                        if(inputText == "") return@clickable

                        val newMessage = Message(
                            message = inputText,
                            status = MessageStatus.SENT,
                            timeStamp = String.EMPTY,
                            senderId = userId,
                            conversationId = conversationId
                        )
                        onSendMessage(newMessage)
                    }
                )
                           },
            placeholder = {
                TextComposable(
                    text = "Type your message here ...",
                    fontWeight = FontWeight.Normal,
                ) },
            onValueChange = onTextUpdate
        )
    }
}

@Composable
fun ChatScreenTopBar(
    chat: ChatCardData,
    isOnline : Boolean,
    onBackPress: ()-> Unit,
    onAddFriend: ()-> Unit
) {

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Icon(
//                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
//                modifier = Modifier
//                    .size(40.dp)
//                    .clickable { onBackPress() },
//                contentDescription = "back"
//            )
//
            AsyncImage(
                model = chat.photoUrl,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            HorizontalSpacer(15.dp)

            Column(
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                TextComposable(
                    text = chat.friendUserName,
                    fontSize = 18.sp
                )
                TextComposable(
                    text = if(isOnline) "online" else "Offline",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.weight(1f))

//            Icon(
//                imageVector = Icons.Outlined.AddCircle,
//                tint = Color.Black,
//                modifier = Modifier
//                    .padding(end = 20.dp)
//                    .size(30.dp)
//                    .clickable {onAddFriend()  },
//                contentDescription = "Friends"
//            )

//            Icon(
//                imageVector = Icons.Outlined.Call,
//                tint = Color.Black,
//                modifier = Modifier
//                    .padding(end = 20.dp)
//                    .size(30.dp)
//                    .clickable {  },
//                contentDescription = "back"
//            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Preview
@Composable
fun ContactCard(){
    Row(
        modifier = Modifier
            .background(Color.Red)
            .padding(12.dp)

        ,
    ) {
        Image(
            painter = painterResource( Res.drawable.user_profile),
            contentDescription = null,
            modifier = Modifier.size(12.dp)
        )
        HorizontalSpacer(15.dp)
        Column(
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            TextComposable(
                text = "John Doe",
                fontSize = 18.sp
            )
    }
}
}
