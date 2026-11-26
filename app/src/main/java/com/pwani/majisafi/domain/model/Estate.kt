package com.pwani.majisafi.domain.model

enum class Estate(val displayName: String) {
    ALL("All Estates"),
    NYALI("Nyali"),
    TUDOR("Tudor"),
    BAMBURI("Bamburi"),
    KISAUNI("Kisauni"),
    LIKONI("Likoni"),
    CHANGAMWE("Changamwe"),
    MOMBASA_ISLAND("Mombasa Island");

    companion object {
        fun fromString(value: String): Estate {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
                ?: ALL
        }
    }
}
