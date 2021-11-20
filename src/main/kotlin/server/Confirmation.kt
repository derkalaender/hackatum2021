package server

data class Confirmation(
    val id: String,
    val selectMerge: Boolean,
    val canceled: Boolean
)