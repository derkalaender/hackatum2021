package data

class Person(
    val name: String
){
    companion object {
        private var ID = 0
    }

    val id = ID++
}