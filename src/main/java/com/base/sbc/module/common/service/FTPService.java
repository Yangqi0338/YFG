package com.base.sbc.module.common.service;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FTPService {

    private final FTPClient ftpClient;

    public FTPService(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public boolean uploadFile(String remotePath, String fileName, InputStream inputStream) {
        boolean success = false;
        try {
            ftpClient.changeWorkingDirectory(remotePath);
            success = ftpClient.storeFile(fileName, inputStream);
        } catch (IOException e) {
            // 处理上传异常
            e.printStackTrace();
        }
        return success;
    }

    public void disconnect() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                // 处理断开连接异常
                e.printStackTrace();
            }
        }
    }
}
