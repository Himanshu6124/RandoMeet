package com.example.vibechat.ui.screens.friendsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibechat.ui.commoncomposables.TextComposable
import com.example.vibechat.ui.screens.chatscreen.TopSection
import com.example.vibechat.ui.screens.matchscreen.Conversation
import com.example.vibechat.utils.getBubbleTime
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreenUI(
    selectedTab: String,
    onBackPress: () -> Unit,
    onFriendClick: (Conversation) -> Unit,
    onTabChange: (String) -> Unit

){
    val viewModel : FriendsViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle side effects
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is FriendsSideEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Black,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .fillMaxWidth()
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopSection(
                    selected = selectedTab,
                    onSelect = onTabChange
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.handleEvent(FriendsEvent.OnFabClick) },
                containerColor = Color(0xFF007AFF),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Friend Requests"
                )
            }
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ){
            itemsIndexed(uiState.friends){index,friend->
                FriendItem(
                    friend = friend,
                    onFriendClick = onFriendClick
                )
                if(index != uiState.friends.lastIndex){
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        // Friend Request Dialog
        if (uiState.showRequestDialog) {
            FriendRequestDialog(
                friendRequests = uiState.friendRequests,
                isLoading = uiState.isLoading,
                onDismiss = { viewModel.handleEvent(FriendsEvent.OnDismissDialog) },
                onAccept = { username -> viewModel.handleEvent(FriendsEvent.OnAcceptRequest(username)) },
                onReject = { username -> viewModel.handleEvent(FriendsEvent.OnRejectRequest(username)) }
            )
        }
    }

}



@Composable
fun FriendItem(
    friend: Conversation,
    onFriendClick: (Conversation) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
            .clickable{onFriendClick(friend)}
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Profile Image
        AsyncImage(
            model = friend.photoUrl,
            contentDescription = "friend_image",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Name + Last Message
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                color = Color.Gray,
                text = friend.friendUserName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = friend.lastMessage.orEmpty(),
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Time
        TextComposable(
            text = getBubbleTime(friend.lastMessageTime.orEmpty()),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }

}