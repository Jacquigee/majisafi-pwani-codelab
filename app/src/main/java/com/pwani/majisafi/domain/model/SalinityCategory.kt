package com.pwani.majisafi.domain.model

enum class SalinityCategory(
    val title: String,
    val rangeDescription: String,
    val recommendation: String
) {
    FRESH(
        title = "Fresh Water",
        rangeDescription = "< 500 ppm",
        recommendation = "Safe for drinking and cooking without restriction."
    ),
    MILDLY_BRACKISH(
        title = "Mildly Brackish",
        rangeDescription = "500 - 1,500 ppm",
        recommendation = "Safe for cooking and bathing; noticeable mineral taste."
    ),
    BRACKISH(
        title = "Brackish Water",
        rangeDescription = "1,501 - 3,000 ppm",
        recommendation = "Laundry, cleaning, and flushing only. Corrodes pipes and kettles."
    ),
    SALINE(
        title = "Saline (Sea Intrusion)",
        rangeDescription = "> 3,000 ppm",
        recommendation = "Non-potable! Reverse Osmosis (RO) filtration mandatory before use."
    )
}
