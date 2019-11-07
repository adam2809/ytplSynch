import filedownload.FileDownloader
import filedownload.InvalidYTURLException
import filedownload.YTAudioFileDownloader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.nio.file.Files

class YTAudioFileDownloaderTest{

    companion object {
        const val TEST_YT_FILE_DIR = "/home/adam/code/ytplSynch/src/test/resources/TestDestYTFileDownload"
        const val YT_DL_FILE_NAME_FORMAT = "%s-%s.m4a"
    }


    @BeforeEach
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
        val downloader: FileDownloader = YTAudioFileDownloader(validTestURL, TEST_YT_FILE_DIR)

        downloader.download()

        val expectedTitle = "Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"
        val expectedVideoId = "nsufd9Ckiko"

        val filesInTestDest = getFilesInTestDir()
        assertEquals(1,filesInTestDest.size)
        assertEquals(getYTDLFileNameFormatted(expectedTitle,expectedVideoId),filesInTestDest[0].name)

    }

    @Test
    fun correctExceptionThrownWhenURLInvalid(){
        val invalidTestURL = "marcin manganian koniukja"
        val downloader: FileDownloader = YTAudioFileDownloader(invalidTestURL, TEST_YT_FILE_DIR)

        assertThrows<InvalidYTURLException> {
            downloader.download()
        }

        val filesInTestDest = getFilesInTestDir()
        assertTrue(filesInTestDest.isEmpty())
    }
}