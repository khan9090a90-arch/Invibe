package com.example.data.local

import androidx.room.*
import com.example.data.model.CommentEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isSaved = 1 ORDER BY timestamp DESC")
    fun getSavedPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: Long): PostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("UPDATE posts SET isLiked = :isLiked, likeCount = :likeCount WHERE id = :id")
    suspend fun updateLike(id: Long, isLiked: Boolean, likeCount: Int)

    @Query("UPDATE posts SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSaved(id: Long, isSaved: Boolean)

    @Query("UPDATE posts SET commentCount = commentCount + 1 WHERE id = :id")
    suspend fun incrementCommentCount(id: Long)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePost(id: Long)

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getCount(): Int
}

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY isViewed ASC, timestamp DESC")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Query("UPDATE stories SET isViewed = 1 WHERE id = :id")
    suspend fun markAsViewed(id: Long)

    @Query("SELECT COUNT(*) FROM stories")
    suspend fun getCount(): Int
}

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity): Long

    @Query("UPDATE comments SET isLiked = :isLiked, likeCount = :likeCount WHERE id = :id")
    suspend fun updateLike(id: Long, isLiked: Boolean, likeCount: Int)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getCount(): Int
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE conversationPartner = :partner ORDER BY timestamp ASC")
    fun getMessagesForPartner(partner: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("SELECT COUNT(*) FROM messages")
    suspend fun getCount(): Int
}
