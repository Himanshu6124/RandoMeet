package com.example.vibechat.utils

import com.example.vibechat.data.model.User
import com.example.vibechat.ui.screens.matchscreen.Conversation


val sampleConversations = listOf(
    Conversation(
        conversationId = "1",
        friendUserName = "Alice Johnson",
        friendUserId = "user1",
        photoUrl = "https://i.pravatar.cc/150?img=1",
        lastMessage = "Hey! How are you?",
        lastMessageTime = "10:30 AM",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.READ
    ),
    Conversation(
        conversationId = "2",
        friendUserName = "Bob Smith",
        friendUserId = "user2",
        photoUrl = "https://i.pravatar.cc/150?img=2",
        lastMessage = "See you tomorrow!",
        lastMessageTime = "9:15 AM",
        isByYou = true,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.DELIVERED
    ),
    Conversation(
        conversationId = "3",
        friendUserName = "Charlie Brown",
        friendUserId = "user3",
        photoUrl = "https://i.pravatar.cc/150?img=3",
        lastMessage = "That sounds great 😊",
        lastMessageTime = "Yesterday",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.READ
    ),
    Conversation(
        conversationId = "4",
        friendUserName = "Diana Prince",
        friendUserId = "user4",
        photoUrl = "https://i.pravatar.cc/150?img=4",
        lastMessage = "Can we meet at 5?",
        lastMessageTime = "Yesterday",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.SENT
    ),
    Conversation(
        conversationId = "5",
        friendUserName = "Ethan Hunt",
        friendUserId = "user5",
        photoUrl = "https://i.pravatar.cc/150?img=5",
        lastMessage = "Thanks for your help!",
        lastMessageTime = "2 days ago",
        isByYou = true,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.READ
    ),
    Conversation(
        conversationId = "6",
        friendUserName = "Fiona Gallagher",
        friendUserId = "user6",
        photoUrl = "https://i.pravatar.cc/150?img=6",
        lastMessage = "I'll be there in 10 minutes",
        lastMessageTime = "3 days ago",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.READ
    ),
    Conversation(
        conversationId = "7",
        friendUserName = "George Martin",
        friendUserId = "user7",
        photoUrl = "https://i.pravatar.cc/150?img=7",
        lastMessage = "Good morning!",
        lastMessageTime = "1 week ago",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.DELIVERED
    ),
    Conversation(
        conversationId = "1",
        friendUserName = "Alice Johnson",
        friendUserId = "user1",
        photoUrl = "https://i.pravatar.cc/150?img=1",
        lastMessage = "Hey! How are you?",
        lastMessageTime = "10:30 AM",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.READ
    ),
    Conversation(
        conversationId = "2",
        friendUserName = "Bob Smith",
        friendUserId = "user2",
        photoUrl = "https://i.pravatar.cc/150?img=2",
        lastMessage = "See you tomorrow!",
        lastMessageTime = "9:15 AM",
        isByYou = true,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.DELIVERED
    ),
    Conversation(
        conversationId = "3",
        friendUserName = "Charlie Brown",
        friendUserId = "user3",
        photoUrl = "https://i.pravatar.cc/150?img=3",
        lastMessage = "That sounds great 😊",
        lastMessageTime = "Yesterday",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.READ
    ),
    Conversation(
        conversationId = "4",
        friendUserName = "Diana Prince",
        friendUserId = "user4",
        photoUrl = "https://i.pravatar.cc/150?img=4",
        lastMessage = "Can we meet at 5?",
        lastMessageTime = "Yesterday",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.SENT
    ),
    Conversation(
        conversationId = "5",
        friendUserName = "Ethan Hunt",
        friendUserId = "user5",
        photoUrl = "https://i.pravatar.cc/150?img=5",
        lastMessage = "Thanks for your help!",
        lastMessageTime = "2 days ago",
        isByYou = true,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.READ
    ),
    Conversation(
        conversationId = "6",
        friendUserName = "Fiona Gallagher",
        friendUserId = "user6",
        photoUrl = "https://i.pravatar.cc/150?img=6",
        lastMessage = "I'll be there in 10 minutes",
        lastMessageTime = "3 days ago",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.READ
    ),
    Conversation(
        conversationId = "7",
        friendUserName = "George Martin",
        friendUserId = "user7",
        photoUrl = "https://i.pravatar.cc/150?img=7",
        lastMessage = "Good morning!",
        lastMessageTime = "1 week ago",
        isByYou = false,
        messageStatus = com.example.vibechat.ui.screens.matchscreen.MessageStatus.DELIVERED
    )
)

val sampleRequests = listOf(
    User(
        username = "john_doe",
        name = "John Doe",
        photoUrl = "https://i.pravatar.cc/150?img=1",
        bio = "Software Developer"
    ),
    User(
        username = "jane_smith",
        name = "Jane Smith",
        photoUrl = "https://i.pravatar.cc/150?img=2",
        bio = "Designer"
    ),
    User(
        username = "mike_wilson",
        name = "Mike Wilson",
        photoUrl = "https://i.pravatar.cc/150?img=3",
        bio = "Photographer"
    )
)