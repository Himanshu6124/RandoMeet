package com.example.vibechat.ui.screens.chatscreen


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.AddCircle
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibechat.core.utils.EMPTY
import com.example.vibechat.ui.commoncomposables.HorizontalSpacer
import com.example.vibechat.ui.commoncomposables.TextComposable
import com.example.vibechat.ui.commoncomposables.VerticalSpacer
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.MessageCard
import com.example.vibechat.ui.screens.chatscreen.components.dummyMessages
import com.example.vibechat.ui.screens.matchscreen.Conversation
import com.example.vibechat.ui.screens.matchscreen.MessageStatus
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    isRandomMatch: Boolean = true,
    chat: Conversation?,
    navigateBack: () -> Unit = {},
) {
    val viewModel: ChatScreenViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsState().value
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.iniState(
            conversation = chat
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is ChatSideEffect.ScrollToBottom -> {
                    if (it.index != -1) {
                        listState.animateScrollToItem(it.index)
                    }
                }

                is ChatSideEffect.ShowSnackBar -> {

                }

                is ChatSideEffect.ShowToast -> {

                }
            }
        }
    }


    Scaffold(
        topBar = {
            if (chat != null) {
                ChatScreenTopBar(
                    chat = chat,
                    isOnline = uiState.isOnline,
                    onBackPress = navigateBack,
                    onAddFriend = {
//                        viewModel.sendFriendRequest(
//                            userId = userId.orEmpty(),
//                            friendId = chat.friendUserId
//                        )
                    }
                )
            }
        },

        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (uiState.isTyping) {
                    Text(
                        "typing...",
                        modifier = Modifier.padding(
                            start = 20.dp,
                            bottom = 5.dp
                        )
                    )
                }
                SendMessageButton(
                    userId = uiState.userId.orEmpty(),
                    conversationId = chat?.conversationId ?: "",
                    inputText = inputText,
                    onTextUpdate = {
                        inputText = it
                        viewModel.onUserTyping(
                            senderId = uiState.userId.orEmpty(),
                            conversationId = chat?.conversationId.orEmpty(),
                            inputText = it
                        )
                    },
                    onSendMessage = {
                        viewModel.handleEvent(
                            ChatEvent.SendMessage(
                                message = it,
                                isForRandomMatching = isRandomMatch
                            )
                        )
                    }
                )
            }
        }

    ) { padding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            state = listState
        ) {
            items(uiState.messages) { message ->
                MessageCard(uiState.userId, message) {
//                        deleteMessage(it)
                }
            }

        }
    }
}

@Composable
fun SendMessageButton(
    userId: String = "",
    conversationId: String = "",
    inputText: String = "",
    onTextUpdate: (String) -> Unit = {},
    onSendMessage: (Message) -> Unit = {},
) {
    Row(
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputText,
            trailingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier.clickable {

                        if (inputText == "") return@clickable

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
                )
            },
            onValueChange = onTextUpdate
        )
    }
}


@Composable
fun ChatScreenTopBar(
    chat: Conversation,
    isOnline: Boolean,
    onBackPress: () -> Unit,
    onAddFriend: () -> Unit
) {

    Column {
        Row(
            modifier = Modifier
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .fillMaxWidth()
                .background(Color.White)
                .padding(15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = onBackPress),
                contentDescription = "Back"
            )

            AsyncImage(
                model = chat.photoUrl,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            HorizontalSpacer(15.dp)

            Column {
                TextComposable(
                    text = chat.friendUserName,
                    fontSize = 18.sp
                )
                TextComposable(
                    text = if (isOnline) "online" else "offline",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.Outlined.AddCircle,
                modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = onAddFriend),
                contentDescription = "Add Friend"
            )
        }
        HorizontalDivider()
    }
}
