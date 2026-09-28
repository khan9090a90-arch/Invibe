package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.VolumeOff
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ReelItem
import com.example.ui.theme.InstaBlue
import com.example.ui.theme.InstaHeart
import com.example.ui.viewmodel.InstaViewModel

@Composable
fun ReelsScreen(
    viewModel: InstaViewModel,
    modifier: Modifier = Modifier
) {
    val reelsList = remember {
        listOf(
            ReelItem(
                id = 1,
                username = "neon_rider",
                userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80",
                description = "Drifting through midnight Neo-Tokyo on neon wheels ✨ Speed, synth, and freedom.",
                audioTrack = "Kavinsky - Nightcall (Synthwave Remix)",
                imageUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1080&auto=format&fit=crop&q=80",
                likesCount = 28400,
                commentsCount = 890
            ),
            ReelItem(
                id = 2,
                username = "dj_pulse",
                userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80",
                description = "When the bass drops at 3 AM and nobody wants to go home 🔊🤯 Drop your favorite track below!",
                audioTrack = "Original Audio - DJ Pulse Live",
                imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=1080&auto=format&fit=crop&q=80",
                likesCount = 45900,
                commentsCount = 1320
            ),
            ReelItem(
                id = 3,
                username = "fit_clara",
                userAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&auto=format&fit=crop&q=80",
                description = "Morning 6 AM discipline. Consistency beats motivation every single day. 💪🔥",
                audioTrack = "Trending Workout Beats - Trap Mix",
                imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=1080&auto=format&fit=crop&q=80",
                likesCount = 31200,
                commentsCount = 540
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { reelsList.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("reels_screen")
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ReelPageItem(reel = reelsList[page])
        }

        // Top Reels Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 40.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reels",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = { /* Camera action */ }) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Create Reel",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun ReelPageItem(reel: ReelItem) {
    var isLiked by remember { mutableStateOf(reel.isLiked) }
    var likesCount by remember { mutableIntStateOf(reel.likesCount) }
    var isMuted by remember { mutableStateOf(false) }

    // Vinyl spinning animation
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Reel Media (Vertical Full Screen)
        AsyncImage(
            model = reel.imageUrl,
            contentDescription = reel.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Bottom Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                )
        )

        // Creator details & Audio info (Bottom Left)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = reel.userAvatar,
                    contentDescription = reel.username,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = reel.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                    modifier = Modifier.clickable { /* follow */ }
                ) {
                    Text(
                        text = "Follow",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = reel.description,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Audio Marquee row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = reel.audioTrack,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Side Action Bar (Bottom Right)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = {
                        isLiked = !isLiked
                        likesCount = if (isLiked) likesCount + 1 else likesCount - 1
                    }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like reel",
                        tint = if (isLiked) InstaHeart else Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = "$likesCount",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Comments
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { /* open comments */ }) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "${reel.commentsCount}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Share
            IconButton(onClick = { /* share */ }) {
                Icon(
                    imageVector = Icons.Outlined.NearMe,
                    contentDescription = "Share reel",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Mute / Unmute
            IconButton(onClick = { isMuted = !isMuted }) {
                Icon(
                    imageVector = if (isMuted) Icons.Outlined.VolumeOff else Icons.Outlined.VolumeUp,
                    contentDescription = "Toggle sound",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Spinning Vinyl Audio Disc
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .rotate(rotation)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = InstaBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
