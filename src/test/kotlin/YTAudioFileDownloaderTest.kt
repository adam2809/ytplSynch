import filedownload.FileDownloader
import filedownload.InvalidYTURLException
import filedownload.YTAudioFileDownloader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.TestUtils

class YTAudioFileDownloaderTest{

    companion object {
        const val YT_DL_FILE_NAME_FORMAT = "%s-%s.m4a"
    }

    private fun getYTDLFileNameFormatted(ytURL:String,ytId:String):String{
        return YT_DL_FILE_NAME_FORMAT.format(ytURL,ytId)
    }

    @BeforeEach
    fun setUp(){
        TestUtils.clearPath(TestUtils.testEmptyPath)
    }

    @Test
    fun downloadsValidURL(){
        val validTestURL = "https://www.youtube.com/watch?v=nsufd9Ckiko"
        val downloader: FileDownloader = YTAudioFileDownloader(validTestURL, TestUtils.testEmptyPath)

        downloader.download()

        val expectedTitle = "Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"
        val expectedVideoId = "nsufd9Ckiko"

        val filesInTestDest = TestUtils.getPathsInPath(TestUtils.testEmptyPath)
        assertEquals(1,filesInTestDest.size)
        assertEquals(getYTDLFileNameFormatted(expectedTitle,expectedVideoId),filesInTestDest[0].fileName.toString())

    }

    @Test
    fun correctExceptionThrownWhenURLInvalid(){
        val invalidTestURL = "marcin manganian koniukja"
        val downloader: FileDownloader = YTAudioFileDownloader(invalidTestURL, TestUtils.testEmptyPath)

        assertThrows<InvalidYTURLException> {
            downloader.download()
        }

        val filesInTestDest = TestUtils.getPathsInPath(TestUtils.testEmptyPath)
        assertTrue(filesInTestDest.isEmpty())
    }
}