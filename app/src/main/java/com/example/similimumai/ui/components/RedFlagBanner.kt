package com.example.similimumai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LiveRegionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.similimumai.data.model.RedFlagAlert
import com.example.similimumai.ui.theme.CrimsonContainer
import com.example.similimumai.ui.theme.CrimsonOnContainer
import com.example.similimumai.ui.theme.CrimsonRedFlag

@Composable
fun RedFlagBanner(
    alert: RedFlagAlert,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("red_flag_banner")
            // docs/design/accessibility.md §4: TalkBack must read emergency
            // alerts immediately (assertive live region)
            .semantics { liveRegion = LiveRegionMode.Assertive },
        colors = CardDefaults.cardColors(containerColor = CrimsonContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Emergency Alert",
                        tint = CrimsonRedFlag,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "EMERGENCY ALLOPATHIC TRIAGE",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = CrimsonRedFlag
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("dismiss_red_flag_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss Alert",
                        tint = CrimsonOnContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = alert.condition,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CrimsonOnContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "ACTION: ${alert.immediateAction}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = CrimsonRedFlag
            )
        }
    }
}
