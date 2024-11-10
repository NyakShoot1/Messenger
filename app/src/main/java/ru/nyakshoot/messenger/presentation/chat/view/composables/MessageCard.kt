package ru.nyakshoot.messenger.presentation.chat.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import ru.nyakshoot.messenger.domain.chat.Message
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MessageCard(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = if (isCurrentUser) Color(0xFF007AFF) else Color(0xFFEFEFEF),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .widthIn(max = 240.dp)
        ) {
            Text(
                text = message.text,
                color = if (isCurrentUser) Color.White else Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    ).format(message.ts!!.toDate()),
                    maxLines = 1,
                    fontSize = 12.sp,
                    color = if (isCurrentUser) Color.White.copy(alpha = 0.8f) else Color.Gray
                )

                if (isCurrentUser) {
                    Icon(
                        imageVector = if (message.isRead) Icons.Default.Done else Icons.Default.Done,
                        contentDescription = if (message.isRead) "Прочитано" else "Не прочитано",
                        tint = if (message.isRead) Color.Green else Color.Transparent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MessageCardPreview() {
    Column {
        MessageCard(Message("", "1", false, Timestamp.now(), "fdfafaffsafasaasfassafafagdggdgsddgssdgsgsdsf"), "2")
        MessageCard(Message("", "12", true, Timestamp.now(), "fdfafaffsafasaasfassafafagdggdgsddgssdgsgsdsf"), "12")
    }

}