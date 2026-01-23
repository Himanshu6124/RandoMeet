package com.example.vibechat.ui.screens.friendsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vibechat.ui.commoncomposables.TextComposable
import com.example.vibechat.ui.commoncomposables.whiteColor
import com.example.vibechat.ui.screens.chatscreen.TopSection
import com.example.vibechat.ui.screens.chatscreen.greyGradient
import com.example.vibechat.ui.screens.matchscreen.Conversation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import vibechat.composeapp.generated.resources.Res
import vibechat.composeapp.generated.resources.user_profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreenUI(
    onBackPress: () -> Unit,
    onFriendClick: (Conversation) -> Unit,
    onTabChange: (String) -> Unit

){
    val viewModel : FriendsViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsState().value


    Scaffold(
        containerColor = Color.Black,
        topBar = {
            var selectedTab by remember { mutableStateOf("Friends") }
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
            }

//            TopAppBar(
//                colors = Top,
//                title = { Text("Friends") },
//                actions = {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clickable(onClick = onBackPress),
//                        contentDescription = "Back",
//                        tint = whiteColor
//                    )
//                }
//            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ){
            items(uiState.friends){friend->
                FriendItem(
                    friend = friend,
                    onFriendClick = onFriendClick
                )
            }
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
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable{onFriendClick(friend)}
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Profile Image
        AsyncImage(
            model = friend.photoUrl,
            contentDescription = "friend_image",
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Name + Last Message
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = friend.friendUserName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = friend.lastMessage.orEmpty(),
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Time
        TextComposable(
            text = friend.lastMessageTime.orEmpty(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}


@Preview
@Composable
fun PrevFriend(){

    FriendItem(
        Conversation(
        friendUserName = "abhishek",
        lastMessage = "how are you ?",
        lastMessageTime = "12:34",
    ),
        onFriendClick = {}
    )
}