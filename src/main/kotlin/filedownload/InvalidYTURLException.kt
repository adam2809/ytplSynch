package filedownload

import java.lang.Exception

class InvalidYTURLException(invalidURL:String): Exception(MSG.format(invalidURL)) {
    companion object {
        const val MSG = "URL %s is invalid\n"
    }
}