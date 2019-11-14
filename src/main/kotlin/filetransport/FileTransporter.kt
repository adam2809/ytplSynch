package filetransport

import java.nio.file.Path

interface FileTransporter {
    fun transport(source:Path,dest: Path)
}