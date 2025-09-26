import com.bojanludajic.searchForTextOccurrences
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path

class ScannerTest {

    lateinit var path: Path

    @BeforeEach
    fun setup() {
        path = Path.of("/Users/bojanludajic/IdeaProjects/DirectoryScanner/src/test/resources")
    }

    @Test
    fun noResults() = runBlocking {
        val occurrences = searchForTextOccurrences("Python", path).toList()
        assertEquals(0, occurrences.size)
    }

    @Test
    fun singleResult() = runBlocking {
        val occurrences = searchForTextOccurrences("Java", path).toList()
        assertEquals(1, occurrences.size)
        assertEquals(3, occurrences.first().line)
        assertEquals(0, occurrences.first().offset)
    }

    @Test
    fun multipleResults() = runBlocking {
        val occurrences = searchForTextOccurrences("Kotlin", path).toList()
        assertEquals(2, occurrences.size)
        assertEquals(2, occurrences.first().line)
        assertEquals(0, occurrences.first().offset)

        assertEquals(4, occurrences.last().line)
        assertEquals(6, occurrences.last().offset)
    }

}