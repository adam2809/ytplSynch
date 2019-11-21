package utils

import filedownload.FileDownloader
import filedownload.FileDownloaderFactory
import java.io.File
import java.lang.Exception
import java.nio.file.Path

class TestFileDownloaderFactory :FileDownloaderFactory{
    override fun getInstance(source: String, dest: Path): FileDownloader {
        return TestFileDownloader(source,dest)
    }
}

class TestFileDownloader(private val source:String, private val dest:Path):FileDownloader{
    override var downloaded: Boolean = false

    val idToTitleMapping = mapOf(
        "nsufd9Ckiko" to "Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)",
        "oG0XcvGLoq0" to "Tiger & Woods - Balloon",
        "u7kaOntQbsw" to "William Onyeabor - Better Change Your Mind (Official)"
    )

    override fun download() {
        if(!idToTitleMapping.containsKey(source)){
            throw Exception()
        }
        File("${dest}/${idToTitleMapping[source]}-$source.m4a").createNewFile()
    }

}