import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

Path currentPath = Paths.get(System.getProperty("user.dir"))
println "--- Inugami Post-Gen: Searching for POMs in ${currentPath} ---"

try {
    Files.walk(currentPath)
            .filter { Files.isRegularFile(it) && it.fileName.toString() == "pom.xml" }
            .forEach { path ->
                println "Processing: ${path}"
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8)
                def cleanedLines = lines.findAll { it && it.trim().length() > 0 }

                Files.write(path, cleanedLines, StandardCharsets.UTF_8)
                println "Successfully cleaned: ${path.getFileName()}"
            }
} catch (Exception e) {
    println "--- ERROR during Inugami Post-Gen: ${e.message} ---"
    e.printStackTrace()
}

println "--- Inugami Cleanup Finished ---"