package filedownload

import java.nio.file.Path

class YTAudioFileDownloaderFactory:FileDownloaderFactory{
    override fun getInstance(source: String, dest: Path): FileDownloader {
        return YTAudioFileDownloader(source,dest)
    }
}