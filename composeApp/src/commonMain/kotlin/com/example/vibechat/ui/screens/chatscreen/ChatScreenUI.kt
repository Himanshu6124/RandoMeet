package com.example.vibechat.ui.screens.chatscreen


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import com.example.vibechat.core.utils.EMPTY
import com.example.vibechat.koin.showToast
import com.example.vibechat.ui.commoncomposables.HorizontalSpacer
import com.example.vibechat.ui.commoncomposables.TextComposable
import com.example.vibechat.ui.commoncomposables.whiteColor
import com.example.vibechat.ui.screens.chatscreen.components.Message
import com.example.vibechat.ui.screens.chatscreen.components.MessageCard
import com.example.vibechat.ui.screens.chatscreen.components.dummyMessages
import com.example.vibechat.ui.screens.matchscreen.Conversation
import com.example.vibechat.ui.screens.matchscreen.MessageStatus
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    isRandomMatch: Boolean = true,
    chat: Conversation? = Conversation(),
    navigateBack: () -> Unit = {},
    navigateToMatchScreen:() -> Unit,
    onTabChange: (String) -> Unit
) {
    val viewModel: ChatScreenViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsState().value
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    println("On start called")
                    viewModel.handleEvent(ChatEvent.InitState(chat))
                }
                Lifecycle.Event.ON_STOP -> {
                    viewModel.handleEvent(ChatEvent.DisconnectSocket)
                    println("On Stop called")
                }
                Lifecycle.Event.ON_DESTROY ->{
                    println("On Destroy called")
                    viewModel.disconnectSocketPermanently()
                }
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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

                is ChatSideEffect.NavigateToMatchScreen ->{
                    showToast("${it.disconnectedUserName} disconnected")
                    navigateToMatchScreen()
                }
            }
        }
    }


    Scaffold(
        containerColor = Color.Black,
        topBar = {
            if (chat != null) {
                ChatScreenTopBar(
                    chat = chat,
                    isOnline = uiState.isOnline,
                    onBackPress = navigateBack,
                    onAddFriend = {
                        viewModel.disconnectSocketPermanently()
                        navigateToMatchScreen()
//                        viewModel.sendFriendRequest(
//                            userId = userId.orEmpty(),
//                            friendId = chat.friendUserId
//                        )
                    },
                    onTabChange = onTabChange
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
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.White,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.White,
            ),

            modifier = Modifier.fillMaxWidth()
                .border(1.dp, color = Color.Gray),
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
    onAddFriend: () -> Unit,
    onTabChange: (String) -> Unit
) {

    var selectedTab by remember { mutableStateOf("Match") }
    Column(
        modifier = Modifier
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .greyGradient()
            .padding(8.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopSection(
            selected = selectedTab,
            onSelect = onTabChange
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = onBackPress),
                contentDescription = "Back",
                tint = whiteColor
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
                    color = whiteColor,
                    fontSize = 18.sp
                )
                TextComposable(
                    color = whiteColor,
                    text = if (isOnline) "online" else "offline",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.Outlined.Delete,
                modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = onAddFriend),
                contentDescription = "Add Friend",
                tint = whiteColor
            )
        }
    }
}
@Composable
fun prevChatTopBar(){
    ChatScreenTopBar(
        chat = Conversation(friendUserName = "Hp"),
        isOnline = true,
        onAddFriend = {},
        onBackPress = {},
        onTabChange = {}

    )
}

fun Modifier.greyGradient(): Modifier {
   return this.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF181A20), // deep dark
                Color(0xFF23242B)  // slightly lighter dark
            )
        )
    )
}


@Composable
fun TopSection(
    selected: String,
    onSelect: (String) -> Unit
) {
    val items = listOf("Match", "Friends")

    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(50)
            )
        ,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            TopItem(
                title = item,
                selected = selected == item,
                onClick = { onSelect(item) }
            )
        }
    }
}
@Composable
fun TopItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (selected)
            MaterialTheme.colorScheme.onSurfaceVariant
        else
            Color.Transparent,
        label = ""
    )

    val textColor by animateColorAsState(
        if (selected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        label = ""
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = false)
@Composable
fun TopSectionPreview() {
    var selected by remember { mutableStateOf("Match") }
    TopSection(
        selected = selected,
        onSelect = {
            selected = it
        }
    )
}

