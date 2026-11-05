package com.bryt.drama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

private data class Drama(
    val title: String,
    val language: String,
    val description: String,
    val videoUrl: String,
    val attribution: String
)

// Add only videos whose licenses explicitly allow redistribution or whose rights you own.
private val dramaCatalog = listOf(
    Drama(
        title = "Demo short — replace before public launch",
        language = "English",
        description = "Playback demo · replace with a rights-cleared title",
        videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        attribution = "Demo stream only. Confirm rights before public distribution."
    )
)

private val supportedLanguages = listOf(
    "All languages", "English", "Malayalam", "Hindi", "Tamil", "Telugu",
    "Kannada", "Bengali", "Marathi", "Punjabi", "Gujarati", "Urdu",
    "Arabic", "Chinese", "Japanese", "Korean", "Spanish", "French",
    "German", "Portuguese", "Russian", "Indonesian", "Thai", "Turkish",
    "Vietnamese", "Persian", "Italian", "Dutch", "Swahili", "Other"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DramaApp() }
    }
}

@Composable
private fun DramaApp() {
    var selectedDrama by remember { mutableStateOf<Drama?>(null) }
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFFF526B),
            background = Color(0xFF09090B),
            surface = Color(0xFF17171B)
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (selectedDrama == null) {
                HomeScreen(onDramaClick = { selectedDrama = it })
            } else {
                PlayerScreen(drama = selectedDrama!!, onBack = { selectedDrama = null })
            }
        }
    }
}

@Composable
private fun HomeScreen(onDramaClick: (Drama) -> Unit) {
    var selectedLanguage by remember { mutableStateOf("All languages") }
    val visibleDramas = dramaCatalog.filter {
        selectedLanguage == "All languages" || it.language == selectedLanguage
    }

    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Spacer(Modifier.height(28.dp))
        Text(
            "DRAMA",
            color = Color(0xFFFF526B),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )
        Text(
            "Stories without borders.",
            color = Color.White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Discover short dramas in languages you love.",
            color = Color.LightGray,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(18.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(supportedLanguages) { language ->
                FilterChip(
                    selected = selectedLanguage == language,
                    onClick = { selectedLanguage = language },
                    label = { Text(language) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFF526B),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        Spacer(Modifier.height(18.dp))
        Text(
            if (selectedLanguage == "All languages") "FEATURED" else selectedLanguage.uppercase(),
            color = Color.Gray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(10.dp))

        if (visibleDramas.isEmpty()) {
            Text(
                "No dramas in this language yet. New titles will appear here when added to the catalogue.",
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 18.dp)
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(visibleDramas) { drama ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1D1D23), RoundedCornerShape(18.dp))
                        .clickable { onDramaClick(drama) }
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(width = 76.dp, height = 104.dp)
                            .background(Color(0xFF34343B), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(drama.title, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(drama.language, color = Color(0xFFFF8B9B), fontSize = 12.sp)
                        Text(drama.description, color = Color.LightGray, fontSize = 12.sp)
                        Spacer(Modifier.height(9.dp))
                        Text("WATCH NOW  →", color = Color(0xFFFF7184), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Text(
            "Only add content you own or are licensed to distribute.",
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier.padding(bottom = 14.dp)
        )
    }
}

@Composable
private fun PlayerScreen(drama: Drama, onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val exoPlayer = remember(drama.videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(drama.videoUrl))
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { viewContext ->
                PlayerView(viewContext).apply {
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        Row(
            Modifier.align(Alignment.TopStart).padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(drama.title, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Text(
            drama.attribution,
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
        )
    }
}
