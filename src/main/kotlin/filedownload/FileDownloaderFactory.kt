package filedownload

import java.nio.file.Path

interface FileDownloaderFactory{
    fun getInstance(source:String,dest: Path):FileDownloader
}