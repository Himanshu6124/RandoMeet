package com.example.vibechat.ui.screens.friendsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.example.vibechat.data.model.User
import com.example.vibechat.ui.commoncomposables.whiteColor

@Composable
fun FriendRequestDialog(
    friendRequests: List<User>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C1C1E)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Friend Requests",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = whiteColor
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = whiteColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Loading indicator
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = whiteColor)
                    }
                } else if (friendRequests.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No pending friend requests",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    // List of requests
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        items(friendRequests) { request ->
                            FriendRequestItem(
                                request = request,
                                onAccept = { request.username?.let { onAccept(it) } },
                                onReject = { request.username?.let { onReject(it) } }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FriendRequestItem(
    request: User,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            AsyncImage(
                model = request.photoUrl,
                contentDescription = "profile_image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // User Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = request.name ?: request.username ?: "Unknown",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = whiteColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (request.username != null && request.name != null) {
                    Text(
                        text = "@${request.username}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Reject Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFFFF3B30),
                            shape = CircleShape
                        )
                        .clickable { onReject() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Reject",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Accept Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFF34C759),
                            shape = CircleShape
                        )
                        .clickable { onAccept() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Accept",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
