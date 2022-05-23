package com.oqoqbobo.web.service.fileHandle;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileHandleService {
    String getFileContent(String fileName,String filePath) throws Exception;
    void setFileContent(String content,String fileName,String filePath) throws Exception;
    void handleContent(String filePath,String setContent,Boolean isAppened) throws IOException;
    void handleFile(String filePath,String setContent,Boolean isAppened) throws IOException;

    void downLoadOutText(HttpServletResponse response) throws IOException;

    void resetOutText(String filePath) throws IOException;

    void addFileContentForDict(String fileAllPath) throws IOException;
}
