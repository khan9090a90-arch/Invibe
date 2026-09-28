package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.InstaViewModel
import com.example.ui.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstaVibeTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    viewModel: InstaViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val activeStory by viewModel.activeStory.collectAsState()
    val storyProgress by viewModel.storyProgress.collectAsState()
    val commentPost by viewModel.commentPost.collectAsState()
    val currentComments by viewModel.currentComments.collectAsState()
    val commentInputText by viewModel.commentInputText.collectAsState()
    val isDirectMessagesOpen by viewModel.isDirectMessagesOpen.collectAsState()
    val isNotificationsOpen by viewModel.isNotificationsOpen.collectAsState()
    val unreadNotifsCount by viewModel.unreadNotificationsCount.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    // Handle Back Press navigation
    BackHandler(enabled = isDirectMessagesOpen || activeStory != null || isNotificationsOpen || commentPost != null || currentTab != ScreenTab.HOME) {
        when {
            activeStory != null -> viewModel.closeStory()
            isDirectMessagesOpen -> viewModel.closeDirectMessages()
            isNotificationsOpen -> viewModel.closeNotifications()
            commentPost != null -> viewModel.closeComments()
            currentTab != ScreenTab.HOME -> viewModel.selectTab(ScreenTab.HOME)
        }
    }

    if (isDirectMessagesOpen) {
        DirectMessagesScreen(
            viewModel = viewModel,
            onBack = { viewModel.closeDirectMessages() }
        )
    } else {
        Scaffold(
            topBar = {
                if (currentTab == ScreenTab.HOME) {
                    TopAppBar(
                        title = {
                            Text(
                                text = "InVibe",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    brush = StoryGradient
                                ),
                                modifier = Modifier.testTag("app_brand_logo")
                            )
                        },
                        actions = {
                            // Notifications Heart Button with badge
                            BadgedBox(
                                badge = {
                                    if (unreadNotifsCount > 0) {
                                        Badge(
                                            containerColor = InstaHeart,
                                            contentColor = Color.White
                                        ) {
                                            Text(text = "$unreadNotifsCount", fontSize = 10.sp)
                                        }
                                    }
                                }
                            ) {
                                IconButton(
                                    onClick = { viewModel.openNotifications() },
                                    modifier = Modifier.testTag("notifications_icon_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Notifications",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Direct Messages Button
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = InstaBlue,
                                        contentColor = Color.White
                                    ) {
                                        Text(text = "2", fontSize = 10.sp)
                                    }
                                }
                            ) {
                                IconButton(
                                    onClick = { viewModel.openDirectMessages() },
                                    modifier = Modifier.testTag("direct_messages_icon_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.NearMe,
                                        contentDescription = "Direct Messages",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = if (currentTab == ScreenTab.REELS) Color.Black else MaterialTheme.colorScheme.surface,
                    contentColor = if (currentTab == ScreenTab.REELS) Color.White else MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 4.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    val isReels = currentTab == ScreenTab.REELS
                    val defaultIconTint = if (isReels) Color.White else MaterialTheme.colorScheme.onSurface

                    // 1. HOME TAB
                    NavigationBarItem(
                        selected = currentTab == ScreenTab.HOME,
                        onClick = { viewModel.selectTab(ScreenTab.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home Feed",
                                tint = defaultIconTint,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )

                    // 2. EXPLORE TAB
                    NavigationBarItem(
                        selected = currentTab == ScreenTab.EXPLORE,
                        onClick = { viewModel.selectTab(ScreenTab.EXPLORE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.EXPLORE) Icons.Filled.Search else Icons.Outlined.Search,
                                contentDescription = "Explore",
                                tint = defaultIconTint,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )

                    // 3. CREATE TAB
                    NavigationBarItem(
                        selected = currentTab == ScreenTab.CREATE,
                        onClick = { viewModel.selectTab(ScreenTab.CREATE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.CREATE) Icons.Filled.AddBox else Icons.Outlined.AddBox,
                                contentDescription = "Create Post",
                                tint = defaultIconTint,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )

                    // 4. REELS TAB
                    NavigationBarItem(
                        selected = currentTab == ScreenTab.REELS,
                        onClick = { viewModel.selectTab(ScreenTab.REELS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.REELS) Icons.Filled.MovieCreation else Icons.Outlined.MovieCreation,
                                contentDescription = "Reels",
                                tint = defaultIconTint,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )

                    // 5. PROFILE TAB
                    NavigationBarItem(
                        selected = currentTab == ScreenTab.PROFILE,
                        onClick = { viewModel.selectTab(ScreenTab.PROFILE) },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .border(
                                        width = if (currentTab == ScreenTab.PROFILE) 2.dp else 0.dp,
                                        color = if (currentTab == ScreenTab.PROFILE) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .padding(if (currentTab == ScreenTab.PROFILE) 2.dp else 0.dp)
                            ) {
                                AsyncImage(
                                    model = userProfile.avatarUrl,
                                    contentDescription = "Profile",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        ) { paddingValues ->
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) { tab ->
                when (tab) {
                    ScreenTab.HOME -> HomeScreen(viewModel = viewModel)
                    ScreenTab.EXPLORE -> ExploreScreen(viewModel = viewModel)
                    ScreenTab.CREATE -> CreatePostScreen(viewModel = viewModel)
                    ScreenTab.REELS -> ReelsScreen(viewModel = viewModel)
                    ScreenTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Story Fullscreen Viewer Dialog
    activeStory?.let { story ->
        StoryViewerDialog(
            story = story,
            progress = storyProgress,
            onClose = { viewModel.closeStory() },
            onNext = { viewModel.nextStory() },
            onPrevious = { viewModel.previousStory() }
        )
    }

    // Comments Sheet
    commentPost?.let { post ->
        CommentsBottomSheet(
            post = post,
            comments = currentComments,
            inputText = commentInputText,
            onInputChange = { viewModel.updateCommentInput(it) },
            onSubmit = { viewModel.submitComment() },
            onDismiss = { viewModel.closeComments() }
        )
    }

    // Notifications / Activity Sheet
    if (isNotificationsOpen) {
        NotificationsBottomSheet(
            notifications = notifications,
            onDismiss = { viewModel.closeNotifications() }
        )
    }
}
