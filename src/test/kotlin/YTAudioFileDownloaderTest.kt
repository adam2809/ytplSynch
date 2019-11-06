import filedownload.FileDownloader
import filedownload.InvalidYTURLException
import filedownload.YTAudioFileDownloader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

class YTAudioFileDownloaderTest{

    companion object {
        const val TEST_YT_FILE_DIR = "/home/adam/code/ytplSynch/src/test/resources/TestDestYTFileDownload"
        const val YT_DL_FILE_NAME_FORMAT = "%s-%s.m4a"
    }


    @Before
    fun clearTestDir(){
        getFilesInTestDir().forEach {
            Files.delete(it.toPath())
        }
    }

    private fun getFilesInTestDir():List<File>{
        return File(TEST_YT_FILE_DIR).walk().toList().drop(1)
    }

    private fun getYTDLFileNameFormatted(ytURL:String,ytId:String):String{
        return YT_DL_FILE_NAME_FORMAT.format(ytURL,ytId)
    }

    @Test
    fun downloadsValidURL(){
        val validTestURL = "https://www.youtube.com/watch?v=nsufd9Ckiko"
        val downloader: FileDownloader = YTAudioFileDownloader(validTestURL)

        downloader.download()

        val expectedTitle = "Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"
        val expectedVideoId = "nsufd9Ckiko"

        val filesInTestDest = getFilesInTestDir()
        assertEquals(1,filesInTestDest.size)
        println(filesInTestDest[0])
        assertEquals(getYTDLFileNameFormatted(expectedTitle,expectedVideoId),filesInTestDest[0])

    }

    @Test(expected = InvalidYTURLException::class)
    fun correctExceptionThrownWhenURLInvalid(){
        val invalidTestURL = "marcin manganian konikja"
        val downloader: FileDownloader = YTAudioFileDownloader(invalidTestURL)

        downloader.download()

        val filesInTestDest = getFilesInTestDir()
        assertTrue(filesInTestDest.isEmpty())
    }
}