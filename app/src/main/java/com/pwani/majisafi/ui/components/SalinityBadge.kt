package com.pwani.majisafi.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwani.majisafi.domain.model.SalinityCategory
import com.pwani.majisafi.ui.theme.BrackishAmber
import com.pwani.majisafi.ui.theme.BrackishContainer
import com.pwani.majisafi.ui.theme.FreshGreen
import com.pwani.majisafi.ui.theme.FreshGreenContainer
import com.pwani.majisafi.ui.theme.MildBrackishBlue
import com.pwani.majisafi.ui.theme.MildBrackishContainer
import com.pwani.majisafi.ui.theme.SalineContainer
import com.pwani.majisafi.ui.theme.SalineRed

@Composable
fun SalinityBadge(
    category: SalinityCategory,
    modifier: Modifier = Modifier
) {
    val (containerColor, contentColor) = when (category) {
        SalinityCategory.FRESH -> FreshGreenContainer to FreshGreen
        SalinityCategory.MILDLY_BRACKISH -> MildBrackishContainer to MildBrackishBlue
        SalinityCategory.BRACKISH -> BrackishContainer to BrackishAmber
        SalinityCategory.SALINE -> SalineContainer to SalineRed
    }

    Surface(
        modifier = modifier,
        color = containerColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = category.title,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
