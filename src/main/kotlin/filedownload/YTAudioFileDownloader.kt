package filedownload

import java.io.File
import java.util.concurrent.TimeUnit

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

    fun Array<String>.runCommand(workingDir: File): Pair<String,String> {
        val proc = ProcessBuilder(*this)
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return Pair(proc.inputStream.bufferedReader().readText(), proc.errorStream.bufferedReader().readText())
    }
}