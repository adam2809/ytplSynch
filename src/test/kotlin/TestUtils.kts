import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object TestDirs{
    val testYTPLDownloadedPath = Paths.get("/home/adam/code/ytplSynch/src/test/resources/PLStatusTestFiles")
    val testYTPLTransportedPath = Paths.get("/sdcard/YTPLDownloaded")
    val testDirOnDevice = Paths.get("/sdcard/ytplSynchTest")
    val testYTPL = "https://www.youtube.com/watch?list=PL1WyaSvUwdxcXb-V08h4rsRLjGCjKoUVd"
}

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