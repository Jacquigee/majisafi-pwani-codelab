package com.pwani.majisafi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwani.majisafi.domain.SalinityClassifier
import com.pwani.majisafi.domain.model.Borehole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReadingBottomSheet(
    borehole: Borehole,
    onDismiss: () -> Unit,
    onSubmit: (tdsPpm: Int, reporter: String, notes: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var tdsText by remember { mutableStateOf("") }
    var reporterText by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    val currentTdsInt = tdsText.toIntOrNull()
    val previewCategory = currentTdsInt?.let { SalinityClassifier.classify(it) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Log Salinity Reading",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${borehole.name} (${borehole.estate.displayName})",
                fontSize = 14.sp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = tdsText,
                onValueChange = {
                    tdsText = it
                    errorText = null
                },
                label = { Text("TDS Measurement (ppm)") },
                placeholder = { Text("e.g. 750") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = errorText != null
            )

            if (previewCategory != null) {
                Spacer(modifier = Modifier.height(8.dp))
                SalinityBadge(category = previewCategory)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = reporterText,
                onValueChange = { reporterText = it },
                label = { Text("Reporter Name / Org") },
                placeholder = { Text("e.g. Nyali Resident / Bowser Driver") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = notesText,
                onValueChange = { notesText = it },
                label = { Text("Notes (Optional)") },
                placeholder = { Text("e.g. Tested after heavy rains / pump repairs") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            if (errorText != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorText ?: "",
                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val tds = tdsText.toIntOrNull()
                    if (tds == null || tds < 0 || tds > 15000) {
                        errorText = "Please enter a valid TDS ppm value (0 - 15,000)"
                    } else if (reporterText.isBlank()) {
                        errorText = "Please specify a reporter name"
                    } else {
                        onSubmit(tds, reporterText, notesText)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Reading (Offline First)")
            }
        }
    }
}
