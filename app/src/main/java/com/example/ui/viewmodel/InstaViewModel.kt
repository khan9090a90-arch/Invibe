package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CommentEntity
import com.example.data.model.MessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.StoryEntity
import com.example.data.model.UserProfile
import com.example.data.repository.InstaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME,
    EXPLORE,
    CREATE,
    REELS,
    PROFILE
}

data class FilterOption(
    val id: String,
    val name: String,
    val brightness: Float = 1.0f,
    val contrast: Float = 1.0f,
    val saturation: Float = 1.0f,
    val warmTint: Float = 0.0f
)

class InstaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = InstaRepository(database)

    // Navigation Tab
    private val _currentTab = MutableStateFlow(ScreenTab.HOME)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Data streams
    val posts: StateFlow<List<PostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPosts: StateFlow<List<PostEntity>> = repository.savedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stories: StateFlow<List<StoryEntity>> = repository.allStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.unreadNotificationsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // User Profile
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Story Viewer State
    private val _activeStory = MutableStateFlow<StoryEntity?>(null)
    val activeStory: StateFlow<StoryEntity?> = _activeStory.asStateFlow()

    private val _storyProgress = MutableStateFlow(0f)
    val storyProgress: StateFlow<Float> = _storyProgress.asStateFlow()

    private var storyTimerJob: Job? = null

    // Comments Sheet State
    private val _commentPost = MutableStateFlow<PostEntity?>(null)
    val commentPost: StateFlow<PostEntity?> = _commentPost.asStateFlow()

    private val _currentComments = MutableStateFlow<List<CommentEntity>>(emptyList())
    val currentComments: StateFlow<List<CommentEntity>> = _currentComments.asStateFlow()

    private val _commentInputText = MutableStateFlow("")
    val commentInputText: StateFlow<String> = _commentInputText.asStateFlow()

    // Direct Messages State
    private val _isDirectMessagesOpen = MutableStateFlow(false)
    val isDirectMessagesOpen: StateFlow<Boolean> = _isDirectMessagesOpen.asStateFlow()

    private val _activeChatPartner = MutableStateFlow<String?>("elena.creates")
    val activeChatPartner: StateFlow<String?> = _activeChatPartner.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val chatMessages: StateFlow<List<MessageEntity>> = _chatMessages.asStateFlow()

    private val _chatInputText = MutableStateFlow("")
    val chatInputText: StateFlow<String> = _chatInputText.asStateFlow()

    // Activity / Notifications Sheet State
    private val _isNotificationsOpen = MutableStateFlow(false)
    val isNotificationsOpen: StateFlow<Boolean> = _isNotificationsOpen.asStateFlow()

    // Explore / Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTag = MutableStateFlow("All")
    val selectedTag: StateFlow<String> = _selectedTag.asStateFlow()

    val exploreFilterTags = listOf("All", "Photography", "Aesthetic", "Music", "Style", "Travel", "Cyberpunk", "Fitness", "Architecture")

    // Explore Filtered Posts
    val filteredExplorePosts: StateFlow<List<PostEntity>> = combine(
        posts,
        _searchQuery,
        _selectedTag
    ) { postList, query, tag ->
        postList.filter { post ->
            val matchesQuery = query.isEmpty() ||
                    post.caption.contains(query, ignoreCase = true) ||
                    post.username.contains(query, ignoreCase = true) ||
                    post.location.contains(query, ignoreCase = true) ||
                    post.tags.contains(query, ignoreCase = true)

            val matchesTag = tag == "All" ||
                    post.tags.contains(tag, ignoreCase = true) ||
                    post.caption.contains(tag, ignoreCase = true)

            matchesQuery && matchesTag
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Create Post State
    val presetImages = listOf(
        "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=1080&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1080&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=1080&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=1080&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1080&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1519741497674-611481863552?w=1080&auto=format&fit=crop&q=80"
    )

    val filterPresets = listOf(
        FilterOption("normal", "Normal", 1f, 1f, 1f, 0f),
        FilterOption("clarendon", "Clarendon", 1.05f, 1.25f, 1.35f, 0.1f),
        FilterOption("juno", "Juno", 1.1f, 1.15f, 1.2f, 0.2f),
        FilterOption("ludwig", "Ludwig", 0.95f, 1.1f, 0.85f, -0.1f),
        FilterOption("valencia", "Valencia", 1.08f, 0.95f, 0.9f, 0.25f),
        FilterOption("moon", "Moon (B&W)", 1.0f, 1.2f, 0.0f, 0f)
    )

    private val _createPostSelectedImage = MutableStateFlow(presetImages[0])
    val createPostSelectedImage: StateFlow<String> = _createPostSelectedImage.asStateFlow()

    private val _createPostSelectedFilter = MutableStateFlow(filterPresets[0])
    val createPostSelectedFilter: StateFlow<FilterOption> = _createPostSelectedFilter.asStateFlow()

    private val _createPostCaption = MutableStateFlow("")
    val createPostCaption: StateFlow<String> = _createPostCaption.asStateFlow()

    private val _createPostLocation = MutableStateFlow("")
    val createPostLocation: StateFlow<String> = _createPostLocation.asStateFlow()

    private val _createPostTags = MutableStateFlow("#vibes #lifestyle")
    val createPostTags: StateFlow<String> = _createPostTags.asStateFlow()

    // Double tap heart animation trigger
    private val _doubleTapHeartPostId = MutableStateFlow<Long?>(null)
    val doubleTapHeartPostId: StateFlow<Long?> = _doubleTapHeartPostId.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun onDoubleTapPost(post: PostEntity) {
        viewModelScope.launch {
            _doubleTapHeartPostId.value = post.id
            if (!post.isLiked) {
                repository.toggleLike(post)
            }
            delay(800)
            if (_doubleTapHeartPostId.value == post.id) {
                _doubleTapHeartPostId.value = null
            }
        }
    }

    fun toggleLike(post: PostEntity) {
        viewModelScope.launch {
            repository.toggleLike(post)
        }
    }

    fun toggleSave(post: PostEntity) {
        viewModelScope.launch {
            repository.toggleSave(post)
        }
    }

    // Story Viewer Actions
    fun openStory(story: StoryEntity) {
        _activeStory.value = story
        _storyProgress.value = 0f
        startStoryTimer(story)
        viewModelScope.launch {
            repository.markStoryViewed(story.id)
        }
    }

    private fun startStoryTimer(story: StoryEntity) {
        storyTimerJob?.cancel()
        storyTimerJob = viewModelScope.launch {
            val totalSteps = 100
            for (i in 1..totalSteps) {
                delay(40) // ~4 seconds total per story
                _storyProgress.value = i / 100f
            }
            nextStory()
        }
    }

    fun nextStory() {
        val storyList = stories.value
        val currentIndex = storyList.indexOfFirst { it.id == _activeStory.value?.id }
        if (currentIndex in 0 until storyList.size - 1) {
            val next = storyList[currentIndex + 1]
            openStory(next)
        } else {
            closeStory()
        }
    }

    fun previousStory() {
        val storyList = stories.value
        val currentIndex = storyList.indexOfFirst { it.id == _activeStory.value?.id }
        if (currentIndex > 0) {
            val prev = storyList[currentIndex - 1]
            openStory(prev)
        } else {
            _storyProgress.value = 0f
            _activeStory.value?.let { startStoryTimer(it) }
        }
    }

    fun closeStory() {
        storyTimerJob?.cancel()
        _activeStory.value = null
        _storyProgress.value = 0f
    }

    // Comments Sheet Actions
    fun openComments(post: PostEntity) {
        _commentPost.value = post
        _commentInputText.value = ""
        viewModelScope.launch {
            repository.getCommentsForPost(post.id).collect { commentsList ->
                _currentComments.value = commentsList
            }
        }
    }

    fun closeComments() {
        _commentPost.value = null
        _currentComments.value = emptyList()
    }

    fun updateCommentInput(text: String) {
        _commentInputText.value = text
    }

    fun submitComment() {
        val post = _commentPost.value ?: return
        val text = _commentInputText.value.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            repository.addComment(
                postId = post.id,
                text = text,
                username = _userProfile.value.username,
                avatar = _userProfile.value.avatarUrl
            )
            _commentInputText.value = ""
        }
    }

    // Direct Messages Actions
    fun openDirectMessages() {
        _isDirectMessagesOpen.value = true
        loadActiveChat()
    }

    fun closeDirectMessages() {
        _isDirectMessagesOpen.value = false
    }

    fun selectChatPartner(partner: String) {
        _activeChatPartner.value = partner
        loadActiveChat()
    }

    private fun loadActiveChat() {
        val partner = _activeChatPartner.value ?: return
        viewModelScope.launch {
            repository.getMessagesForPartner(partner).collect { msgs ->
                _chatMessages.value = msgs
            }
        }
    }

    fun updateChatInput(text: String) {
        _chatInputText.value = text
    }

    fun sendChatMessage() {
        val partner = _activeChatPartner.value ?: return
        val text = _chatInputText.value.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            repository.sendMessage(
                partner = partner,
                partnerAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&auto=format&fit=crop&q=80",
                text = text
            )
            _chatInputText.value = ""
        }
    }

    // Notification Sheet Actions
    fun openNotifications() {
        _isNotificationsOpen.value = true
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }

    fun closeNotifications() {
        _isNotificationsOpen.value = false
    }

    // Explore Search Actions
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectExploreTag(tag: String) {
        _selectedTag.value = tag
    }

    // Create Post Actions
    fun setCreatePostImage(url: String) {
        _createPostSelectedImage.value = url
    }

    fun setCreatePostFilter(filter: FilterOption) {
        _createPostSelectedFilter.value = filter
    }

    fun setCreatePostCaption(caption: String) {
        _createPostCaption.value = caption
    }

    fun setCreatePostLocation(location: String) {
        _createPostLocation.value = location
    }

    fun setCreatePostTags(tags: String) {
        _createPostTags.value = tags
    }

    fun publishPost(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val caption = _createPostCaption.value.ifBlank { "New vibe ✨" }
            repository.createPost(
                imageUrl = _createPostSelectedImage.value,
                caption = caption,
                location = _createPostLocation.value,
                tags = _createPostTags.value
            )
            // Update profile post count
            _userProfile.value = _userProfile.value.copy(
                postsCount = _userProfile.value.postsCount + 1
            )
            // Reset form
            _createPostCaption.value = ""
            _createPostLocation.value = ""
            _currentTab.value = ScreenTab.HOME
            onSuccess()
        }
    }
}
