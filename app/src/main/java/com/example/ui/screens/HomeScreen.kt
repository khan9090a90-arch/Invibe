package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.InstaViewModel

@Composable
fun HomeScreen(
    viewModel: InstaViewModel,
    modifier: Modifier = Modifier
) {
    val posts by viewModel.posts.collectAsState()
    val stories by viewModel.stories.collectAsState()
    val doubleTapHeartId by viewModel.doubleTapHeartPostId.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_feed_list")
    ) {
        // Stories Row
        item {
            StoriesSection(
                stories = stories,
                onStoryClick = { viewModel.openStory(it) }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 0.5.dp
            )
        }

        // Posts
        items(
            items = posts,
            key = { it.id }
        ) { post ->
            PostCard(
                post = post,
                isHeartBurstActive = doubleTapHeartId == post.id,
                onDoubleTap = { viewModel.onDoubleTapPost(post) },
                onLikeClick = { viewModel.toggleLike(post) },
                onCommentClick = { viewModel.openComments(post) },
                onSaveClick = { viewModel.toggleSave(post) }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "You're all caught up! ✨",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Check the Explore tab for trending creator content",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun StoriesSection(
    stories: List<StoryEntity>,
    onStoryClick: (StoryEntity) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag("stories_row"),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(stories, key = { it.id }) { story ->
            StoryAvatarItem(
                story = story,
                onClick = { onStoryClick(story) }
            )
        }
    }
}

@Composable
fun StoryAvatarItem(
    story: StoryEntity,
    onClick: () -> Unit
) {
    val ringBrush = if (story.isViewed) StorySeenGradient else StoryGradient

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("story_avatar_${story.id}")
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .border(width = 2.5.dp, brush = ringBrush, shape = CircleShape)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = story.userAvatar,
                contentDescription = "${story.username} story",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = story.username,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp,
            modifier = Modifier.widthIn(max = 74.dp)
        )
    }
}

@Composable
fun PostCard(
    post: PostEntity,
    isHeartBurstActive: Boolean,
    onDoubleTap: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}")
    ) {
        // Post Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.5.dp, StoryGradient, CircleShape)
                    .padding(2.dp)
            ) {
                AsyncImage(
                    model = post.userAvatar,
                    contentDescription = post.username,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = post.username,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (post.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified badge",
                            tint = InstaBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                if (post.location.isNotEmpty()) {
                    Text(
                        text = post.location,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(onClick = { /* options */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Post Media (with Double Tap to Like)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { onDoubleTap() }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = "Post image by ${post.username}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Double Tap Heart Burst Animation
            androidx.compose.animation.AnimatedVisibility(
                visible = isHeartBurstActive,
                enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.95f),
                    modifier = Modifier
                        .size(110.dp)
                )
            }
        }

        // Post Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onLikeClick,
                modifier = Modifier.testTag("like_button_${post.id}")
            ) {
                Icon(
                    imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (post.isLiked) "Unlike post" else "Like post",
                    tint = if (post.isLiked) InstaHeart else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(26.dp)
                )
            }

            IconButton(
                onClick = onCommentClick,
                modifier = Modifier.testTag("comment_button_${post.id}")
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = { /* share */ },
                modifier = Modifier.testTag("share_button_${post.id}")
            ) {
                Icon(
                    imageVector = Icons.Outlined.NearMe,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onSaveClick,
                modifier = Modifier.testTag("save_button_${post.id}")
            ) {
                Icon(
                    imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = if (post.isSaved) "Unsave post" else "Save post",
                    tint = if (post.isSaved) InstaBookmark else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Likes Count
        Column(modifier = Modifier.padding(horizontal = 14.dp)) {
            Text(
                text = "${post.likeCount} likes",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Caption
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = post.username + " ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = post.caption,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Tags
            if (post.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = post.tags.split(",").joinToString(" ") { it.trim() },
                    style = MaterialTheme.typography.bodySmall,
                    color = InstaBlue,
                    fontSize = 12.sp
                )
            }

            // View all comments
            if (post.commentCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "View all ${post.commentCount} comments",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable(onClick = onCommentClick)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
