package com.example.primetestertest

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AchievementList(manager: AchievementManager, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top= 28.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // Einzelne Elemente müssen in einen 'item' Block
        item {
            Text(
                text = "Deine Achievements:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // Listen werden mit 'items' hinzugefügt
        items(manager.achievements) { achievement ->
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = if (achievement.unlocked)
                    "🏆 ${achievement.title}"
                else
                    "🔒 ${achievement.title}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
