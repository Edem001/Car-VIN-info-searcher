import java.io.File
import java.lang.StringBuilder
import java.util.*

fun main() {

    val file = File("API_result.txt")
    var text: List<String> = LinkedList<String>()

    file.forEachLine(charset = Charsets.UTF_8) { str -> (text as LinkedList<String>).add(str) }

    text = text.filterNot { line -> line.contains("\"\"") || line.contains("\"Not Applicable\"") }

    text.forEach { println(it) }

}