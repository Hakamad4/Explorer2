package com.hakamada.explorer2.reader

import javafx.scene.control.ListView
import lombok.Data
import java.io.File
import java.util.concurrent.Executors

@Data
class IReader {
    var home = System.getProperty("user.home").replace("\\", "/")
    var targetPath: String? = "$home/Documents/Sheets/"
    
    var rootPath: String? = null
        get() {
            if (field == null) {
                field = home
            }
            return field
        }

    public var unauthorizedPath: Array<String>? = null
        get() {
            if (field == null) {
                field = arrayOf("AndroidStudioProjects", "AppData", "git", "Videos", "Searches", "iCloudDrive", "workspace", "workspaceNac", "workDrone")
            }
            return field
        }
    public var unauthorizedSufix: Array<String>? = null
        get() {
            if (field == null) {
                field = arrayOf(".git", ".idea")
            }
            return field
        }

    fun canReadPath(file: File): Boolean {
        if (!file.isDirectory) return false //se não for um dir, retorna false
        if (unauthorizedPath!!.contains(file.name)) return false
        val listFiles = file.listFiles() ?: return false //se listFile for null retorna false
        val unauthorized = listFiles.filter { f -> unauthorizedSufix!!.contains(f.extension) }
        if (unauthorized.isNotEmpty()) return false //se for um projeto (se o dir conter pastas de git ou da IDEA) retorna false
        return true
    }

    @Throws(Exception::class)
    fun scanPath(pathname: String, listView: ListView<String>) {
        val file = File(pathname)
        if (file.absolutePath == (targetPath + file.name)) return
        if (canReadPath(file)) {
            val listFiles = file.listFiles()
            var coderCount = 0
            for (currentFile in listFiles) {
//            println("\t-- ${currentFile.name}")
                if (coderCount > 15) return
                if (currentFile.isDirectory) {
                    if (!currentFile.name.startsWith(".")) {
                        scanPath(currentFile.absolutePath, listView)
                    }
                } else {
                    var sufixList = arrayOf("xls", "csv", "xlsx")
                    if (sufixList.contains(currentFile.extension)) {
                        println("Scanning : $pathname")
                        println("Moved to : ${targetPath + currentFile.name}")
                        val executor = Executors.newSingleThreadExecutor()
                        try {
                            executor.submit {
                                listView.items.add("Scanning : $pathname")
                                listView.items.add("Moved to : ${targetPath + currentFile.name}")
                                listView.scrollTo(listView.items.size - 1)
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }finally {
                            if (executor.isTerminated){
                                System.err.println("terminated")
                                executor.shutdown()
                            }
                        }
                        currentFile.renameTo(File(targetPath + currentFile.name))
                    } else if (currentFile.extension == "html" || currentFile.extension == "xml" || currentFile.extension == "class" || currentFile.extension == "java") {
                        coderCount++
                    }
                }
            }
        }
    }

}
