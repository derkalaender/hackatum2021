package googleapi.json

import kotlinx.serialization.Serializable

enum class MatrixStatus {
    OK,INVALID_REQUEST,MAX_ELEMENTS_EXCEEDED,MAX_DIMENSIONS_EXCEEDED,OVER_DAILY_LIMIT,OVER_QUERY_LIMIT,REQUEST_DENIED,UNKNOWN_ERROR;
    companion object {
        fun stringToStatus(string: String) : MatrixStatus {
            return values().find { it.name == string } ?: UNKNOWN_ERROR
        }
    }
}

data class Matrix(
    val destination_addresses: List<String> = listOf(),
    val origin_addresses: List<String> = listOf(),
    val rows: List<MatrixRow> = listOf(),
    val status: MatrixStatus = MatrixStatus.UNKNOWN_ERROR
) {
    fun toJson() : MatrixJson {
        return MatrixJson(
            destination_addresses,
            origin_addresses,
            rows,
            status.name
        )
    }
}

@Serializable
data class MatrixJson(
    val destination_addresses: List<String> = listOf(),
    val origin_addresses: List<String> = listOf(),
    val rows: List<MatrixRow> = listOf(),
    val status: String = ""
)

@Serializable
data class MatrixRow(
    val elements: List<MatrixElement> = listOf()
)

@Serializable
data class MatrixElement(
    val distance: MatrixIntEntry = MatrixIntEntry(),
    val duration: MatrixIntEntry = MatrixIntEntry(),
    val duration_in_traffic: MatrixIntEntry = MatrixIntEntry(),
    val status: String = ""
)

@Serializable
data class MatrixIntEntry(
    val text: String = "",
    val value: Int = 0
)