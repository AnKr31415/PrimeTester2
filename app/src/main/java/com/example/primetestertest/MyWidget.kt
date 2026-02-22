package com.example.primetestertest

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import android.annotation.SuppressLint


class MyWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Load birthday from DataStore, default to 2000-01-01 if not set
        val birthday = loadBirthday(context).first() ?: LocalDate.of(2000, 1, 1)
        val today = LocalDate.now()

        // Calculate days lived
        val daysLived = ChronoUnit.DAYS.between(birthday, today).toInt()

        // Find the next prime day
        var daysToWait = 1
        while (!isPrime(daysLived + daysToWait)) {
            daysToWait++
        }

        val nextPrimeDayTotal = daysLived + daysToWait
        val message = if (daysToWait == 1) {
            "Morgen ist Primzahl-Tag!\n(Tag $nextPrimeDayTotal)"
        } else {
            "Noch $daysToWait Tage\nbis Tag $nextPrimeDayTotal"
        }

        provideContent {
            WidgetContent(message, isPrime(daysLived))
        }
    }

    @Suppress("RestrictedAPI")
    @Composable
    private fun WidgetContent(message: String, isTodayPrime: Boolean) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.widget_background))
                .cornerRadius(16.dp)
                .padding(12.dp),
            verticalAlignment = Alignment.Vertical.Top,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Text(
                    text = if (isTodayPrime) "⭐" else "⏳",
                    style = TextStyle(fontSize = 18.sp)
                )
                Spacer(modifier = GlanceModifier.size(8.dp))
                Text(
                    text = "PrimeDays",
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            // Divider
            Column(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ColorProvider(Color.White.copy(alpha = 0.5f)))
            ) {}

            Spacer(modifier = GlanceModifier.height(16.dp))

            // Status message
            if (isTodayPrime) {
                Text(
                    text = "Heute ist ein Primzahl-Tag!",
                    style = TextStyle(
                        color = ColorProvider(Color.Cyan),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = GlanceModifier.fillMaxWidth()
                )
                Spacer(modifier = GlanceModifier.height(8.dp))
            }

            // Main Info
            Text(
                text = message,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                ),
                modifier = GlanceModifier.fillMaxWidth()
            )

            Spacer(modifier = GlanceModifier.height(12.dp))

            // Footer
            Text(
                text = "Bleib neugierig!",
                style = TextStyle(
                    color = ColorProvider(Color.LightGray),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = GlanceModifier.fillMaxWidth()
            )
        }
    }
}

class MyWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MyWidget()
}
