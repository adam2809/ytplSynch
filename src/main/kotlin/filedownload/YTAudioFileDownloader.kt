package filedownload

import utils.runCommand
import java.io.File
import java.nio.file.Path

class YTAudioFileDownloader(private val ytURL:String, private val destPath: Path): FileDownloader{
    override var downloaded: Boolean = false
    var completeYTDLCommand = arrayOf("youtube-dl","-f","bestaudio[ext=m4a]",ytURL)

    override fun download() {
        val (_,error) = completeYTDLCommand.runCommand(destPath.toFile())
//              (Needed to distingush from warnings which youtube-dl also puts in error output)
//              |                                 \/                                          |
        if(error.isNotBlank() && error.contains("ERROR")){
            throw InvalidYTURLException(ytURL)
        }
        downloaded = true
    }
}