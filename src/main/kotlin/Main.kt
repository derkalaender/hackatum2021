import googleapi.GoogleAPI
import kotlinx.coroutines.runBlocking

fun main() : Unit = runBlocking {
    val res = GoogleAPI.getDirections(48.186407, 11.606391, 48.083878, 11.677254)
    println(res)
}