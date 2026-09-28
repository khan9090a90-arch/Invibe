package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val userHandle: String,
    val userAvatar: String,
    val isVerified: Boolean = false,
    val imageUrl: String,
    val caption: String,
    val location: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val tags: String = "" // comma separated
)

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val userAvatar: String,
    val imageUrl: String,
    val caption: String = "",
    val isViewed: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val postId: Long,
    val username: String,
    val userAvatar: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likeCount: Int = 0,
    val isLiked: Boolean = false
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "like", "comment", "follow"
    val username: String,
    val userAvatar: String,
    val message: String,
    val targetPostImage: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val conversationPartner: String,
    val partnerAvatar: String,
    val text: String,
    val isMe: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class UserProfile(
    val username: String = "alex_vibe",
    val fullName: String = "Alex Rivera",
    val bio: String = "Capturing moments in urban neon & twilight 🌆 | Visual storyteller & tech designer | NYC ✈️ Tokyo",
    val avatarUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
    val postsCount: Int = 42,
    val followersCount: Int = 14200,
    val followingCount: Int = 485,
    val isVerified: Boolean = true
)

data class ReelItem(
    val id: Long,
    val username: String,
    val userAvatar: String,
    val description: String,
    val audioTrack: String,
    val imageUrl: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false
)
