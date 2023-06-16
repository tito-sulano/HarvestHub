package com.example.capstoneharvesthub

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.capstoneharvesthub.navigation.Routes


data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val screen: Routes,
)
