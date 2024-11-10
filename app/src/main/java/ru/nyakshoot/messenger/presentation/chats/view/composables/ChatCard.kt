package ru.nyakshoot.messenger.presentation.chats.view.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import ru.nyakshoot.messenger.domain.chat.Message
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatCard(chatTitle: String, lastMessage: Message, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = chatTitle,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = lastMessage.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (lastMessage.ts != null) {
                        Text(
                            SimpleDateFormat(
                                "HH:mm",
                                Locale.getDefault()
                            ).format(lastMessage.ts!!.toDate()),
                            maxLines = 1
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .background(
                                if (!lastMessage.isRead) Color.Red else Color.Transparent,
                                CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatCardPreview() {
    ChatCard("Oleg", Message(isRead = true, text = "Hi!")) {}
}