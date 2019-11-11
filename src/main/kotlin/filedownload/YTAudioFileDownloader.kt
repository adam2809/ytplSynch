package filedownload

import utils.runCommand
import java.io.File

class YTAudioFileDownloader(private val ytURL:String, private val destDir:String): FileDownloader {
    override var downloaded: Boolean = false
    var completeYTDLCommand = arrayOf("youtube-dl","-f","bestaudio[ext=m4a]",ytURL)

    override fun download() {
        val (output,error) = completeYTDLCommand.runCommand(File(destDir))
//              (Needed to distingush from warnings which youtube-dl also puts in error output)
//              |                                 \/                                          |
        if(error.isNotBlank() && error.contains("ERROR")){
            throw InvalidYTURLException(ytURL)
        }
        downloaded = true
    }
}