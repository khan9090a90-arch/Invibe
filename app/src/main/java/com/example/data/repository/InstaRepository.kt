package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.CommentEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class InstaRepository(private val database: AppDatabase) {
    private val postDao = database.postDao()
    private val storyDao = database.storyDao()
    private val commentDao = database.commentDao()
    private val notificationDao = database.notificationDao()
    private val messageDao = database.messageDao()

    val allPosts: Flow<List<PostEntity>> = postDao.getAllPosts()
    val savedPosts: Flow<List<PostEntity>> = postDao.getSavedPosts()
    val allStories: Flow<List<StoryEntity>> = storyDao.getAllStories()
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    val unreadNotificationsCount: Flow<Int> = notificationDao.getUnreadCount()

    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> =
        commentDao.getCommentsForPost(postId)

    fun getMessagesForPartner(partner: String): Flow<List<MessageEntity>> =
        messageDao.getMessagesForPartner(partner)

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        if (postDao.getCount() == 0) {
            val initialPosts = listOf(
                PostEntity(
                    username = "elena.creates",
                    userHandle = "@elena.creates",
                    userAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
                    isVerified = true,
                    imageUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=1080&auto=format&fit=crop&q=80",
                    caption = "Midnight reflections in Shibuya 🌃 Neon lights never sleep. What city vibe inspires your soul the most?",
                    location = "Tokyo, Japan",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45, // 45m ago
                    likeCount = 3420,
                    commentCount = 89,
                    isLiked = true,
                    isSaved = false,
                    tags = "#tokyo,#cyberpunk,#neonvibes,#nightphotography,#wanderlust"
                ),
                PostEntity(
                    username = "marcus_beats",
                    userHandle = "@marcus_beats",
                    userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80",
                    isVerified = false,
                    imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=1080&auto=format&fit=crop&q=80",
                    caption = "Electric energy tonight! Dropped the unreleased remix and the crowd went absolutely ballistic 🔥🎧",
                    location = "Warehouse 23, Brooklyn",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 180, // 3h ago
                    likeCount = 1890,
                    commentCount = 142,
                    isLiked = false,
                    isSaved = true,
                    tags = "#musicfestival,#electronicvibes,#producerlife,#djset,#underground"
                ),
                PostEntity(
                    username = "chloe.style",
                    userHandle = "@chloe.style",
                    userAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&auto=format&fit=crop&q=80",
                    isVerified = true,
                    imageUrl = "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=1080&auto=format&fit=crop&q=80",
                    caption = "Effortless autumn minimalism 🍂 Oversized trench, vintage shades, and good coffee. Rate the fit 1-10! ✨",
                    location = "SoHo, New York",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 5, // 5h ago
                    likeCount = 5210,
                    commentCount = 312,
                    isLiked = false,
                    isSaved = false,
                    tags = "#streetwear,#ootd,#autumnfashion,#vintageaesthetic,#fashioninspo"
                ),
                PostEntity(
                    username = "kai_lens",
                    userHandle = "@kai_lens",
                    userAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&auto=format&fit=crop&q=80",
                    isVerified = false,
                    imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=1080&auto=format&fit=crop&q=80",
                    caption = "Golden hour tranquility at Yosemite. Disconnecting from screens to watch the granite glow. Breathtaking. 🏔️",
                    location = "Yosemite National Park, CA",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 12, // 12h ago
                    likeCount = 7890,
                    commentCount = 205,
                    isLiked = true,
                    isSaved = true,
                    tags = "#goldenhour,#yosemite,#naturelovers,#explorepage,#wilderness"
                ),
                PostEntity(
                    username = "brew_and_code",
                    userHandle = "@brew_and_code",
                    userAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=200&auto=format&fit=crop&q=80",
                    isVerified = false,
                    imageUrl = "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=1080&auto=format&fit=crop&q=80",
                    caption = "Aesthetic work setup on a rainy afternoon ☕ Cold brew + dark mode + chill lo-fi beats. Coding sprint is on!",
                    location = "Artisan Cafe, Seattle",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 22,
                    likeCount = 2450,
                    commentCount = 76,
                    isLiked = false,
                    isSaved = false,
                    tags = "#cafeculture,#setupinspo,#deskgoals,#cozyvibes,#developer"
                )
            )
            postDao.insertPosts(initialPosts)
        }

        if (storyDao.getCount() == 0) {
            val initialStories = listOf(
                StoryEntity(
                    username = "Your Story",
                    userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80",
                    imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=1080&auto=format&fit=crop&q=80",
                    caption = "Morning mood ☀️",
                    isViewed = false
                ),
                StoryEntity(
                    username = "elena.creates",
                    userAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
                    imageUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1080&auto=format&fit=crop&q=80",
                    caption = "Midnight ramen cravings 🍜",
                    isViewed = false
                ),
                StoryEntity(
                    username = "marcus_beats",
                    userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80",
                    imageUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=1080&auto=format&fit=crop&q=80",
                    caption = "Late night studio session 🎹",
                    isViewed = false
                ),
                StoryEntity(
                    username = "chloe.style",
                    userAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&auto=format&fit=crop&q=80",
                    imageUrl = "https://images.unsplash.com/photo-1529139574466-a303027c1d8b?w=1080&auto=format&fit=crop&q=80",
                    caption = "Fitting room secrets 👗",
                    isViewed = false
                ),
                StoryEntity(
                    username = "kai_lens",
                    userAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&auto=format&fit=crop&q=80",
                    imageUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=1080&auto=format&fit=crop&q=80",
                    caption = "Sunrise above the clouds ☁️",
                    isViewed = false
                ),
                StoryEntity(
                    username = "zack_skates",
                    userAvatar = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=200&auto=format&fit=crop&q=80",
                    imageUrl = "https://images.unsplash.com/photo-1520045892732-304bc3ac5d8e?w=1080&auto=format&fit=crop&q=80",
                    caption = "Kickflip down the 7-stair 🛹",
                    isViewed = false
                )
            )
            storyDao.insertStories(initialStories)
        }

        if (notificationDao.getCount() == 0) {
            val initialNotifications = listOf(
                NotificationEntity(
                    type = "like",
                    username = "elena.creates",
                    userAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
                    message = "liked your photo.",
                    targetPostImage = "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=200&auto=format&fit=crop&q=80",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 15,
                    isRead = false
                ),
                NotificationEntity(
                    type = "comment",
                    username = "marcus_beats",
                    userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80",
                    message = "commented: 'This track transition is unreal! 🔥'",
                    targetPostImage = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=200&auto=format&fit=crop&q=80",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 75,
                    isRead = false
                ),
                NotificationEntity(
                    type = "follow",
                    username = "chloe.style",
                    userAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&auto=format&fit=crop&q=80",
                    message = "started following you.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                    isRead = true
                ),
                NotificationEntity(
                    type = "like",
                    username = "kai_lens",
                    userAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&auto=format&fit=crop&q=80",
                    message = "liked your comment: 'Insane colors!'",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 6,
                    isRead = true
                )
            )
            notificationDao.insertNotifications(initialNotifications)
        }

        if (messageDao.getCount() == 0) {
            val initialMessages = listOf(
                MessageEntity(
                    conversationPartner = "elena.creates",
                    partnerAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
                    text = "Hey Alex! Loved your newest Tokyo edits!",
                    isMe = false,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 40
                ),
                MessageEntity(
                    conversationPartner = "elena.creates",
                    partnerAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
                    text = "Thank you Elena! Captured those right by the Shibuya crossing!",
                    isMe = true,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 35
                ),
                MessageEntity(
                    conversationPartner = "elena.creates",
                    partnerAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
                    text = "Are you going to the creator meetup this weekend?",
                    isMe = false,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 10
                ),
                MessageEntity(
                    conversationPartner = "marcus_beats",
                    partnerAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80",
                    text = "Yo bro, send me that visual loop for the DJ set!",
                    isMe = false,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 120
                )
            )
            messageDao.insertMessages(initialMessages)
        }
    }

    suspend fun toggleLike(post: PostEntity) = withContext(Dispatchers.IO) {
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likeCount + 1 else (post.likeCount - 1).coerceAtLeast(0)
        postDao.updateLike(post.id, newLiked, newCount)

        if (newLiked) {
            notificationDao.insertNotification(
                NotificationEntity(
                    type = "like",
                    username = "You",
                    userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80",
                    message = "liked ${post.username}'s post.",
                    targetPostImage = post.imageUrl,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun toggleSave(post: PostEntity) = withContext(Dispatchers.IO) {
        postDao.updateSaved(post.id, !post.isSaved)
    }

    suspend fun addComment(postId: Long, text: String, username: String, avatar: String) =
        withContext(Dispatchers.IO) {
            commentDao.insertComment(
                CommentEntity(
                    postId = postId,
                    username = username,
                    userAvatar = avatar,
                    text = text,
                    timestamp = System.currentTimeMillis()
                )
            )
            postDao.incrementCommentCount(postId)
        }

    suspend fun createPost(
        caption: String,
        imageUrl: String,
        location: String,
        tags: String
    ): Long = withContext(Dispatchers.IO) {
        postDao.insertPost(
            PostEntity(
                username = "alex_vibe",
                userHandle = "@alex_vibe",
                userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80",
                isVerified = true,
                imageUrl = imageUrl,
                caption = caption,
                location = location,
                timestamp = System.currentTimeMillis(),
                likeCount = 1,
                commentCount = 0,
                isLiked = true,
                isSaved = false,
                tags = tags
            )
        )
    }

    suspend fun markStoryViewed(storyId: Long) = withContext(Dispatchers.IO) {
        storyDao.markAsViewed(storyId)
    }

    suspend fun markAllNotificationsRead() = withContext(Dispatchers.IO) {
        notificationDao.markAllAsRead()
    }

    suspend fun sendMessage(partner: String, partnerAvatar: String, text: String) =
        withContext(Dispatchers.IO) {
            messageDao.insertMessage(
                MessageEntity(
                    conversationPartner = partner,
                    partnerAvatar = partnerAvatar,
                    text = text,
                    isMe = true,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
}
