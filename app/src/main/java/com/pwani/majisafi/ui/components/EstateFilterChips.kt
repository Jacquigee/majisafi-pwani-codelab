package com.pwani.majisafi.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pwani.majisafi.domain.model.Estate

@Composable
fun EstateFilterChips(
    selectedEstate: Estate,
    onEstateSelected: (Estate) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Estate.entries.forEach { estate ->
            FilterChip(
                selected = selectedEstate == estate,
                onClick = { onEstateSelected(estate) },
                label = { Text(estate.displayName) }
            )
        }
    }
}
