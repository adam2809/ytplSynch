import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

val testYTPLDownloadedPath = Paths.get("/home/adam/code/ytplSynch/src/test/resources/PLStatusTestFiles")
val testDirOnDevice = Paths.get("/sdcard/ytplSynchTest")

fun clearPath(path:Path){
    getPathsInPath(path).forEach {
        Files.delete(it)
    }
}

fun getPathsInPath(path: Path): List<Path> {
    val res = mutableListOf<Path>()
    Files.walk(path).forEach{
        res.add(it)
    }
    return res.drop(1)
}

clearPath(Paths.get("/home/adam/code/ytplSynch/src/test/resources/TestDestYTFileDownload"))
