package com.base.sbc.config.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.SocketException;

/**
 * @author 卞康
 * @date 2023/7/23 14:43:20
 * @mail 247967116@qq.com
 */
@Slf4j
public class OpenSmpFtpUtils {

    //private static BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    public static InputStream download(String url) {
        String ftpHost = "10.8.240.175";
        String ftpUserName = "plmuser";
        String ftpPassword = "plmuser";
        Integer ftpPort = 21;
        return downloadFtpFileToStream(ftpHost, ftpUserName, ftpPassword, ftpPort, url);
    }


    public static InputStream downloadFtpFileToStream(String ftpHost, String ftpUserName,
                                                 String ftpPassword, int ftpPort, String ftpPath) {

        FTPClient ftpClient = null;
        InputStream in = null;
        try {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            ftpClient.setBufferSize(1024);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory("/");

            log.info("-----start--retrieveFileStream----patch:" + ftpPath);
            in = ftpClient.retrieveFileStream(ftpPath);


            if (in == null) {
                log.info("------retrieveFileStream---in-is-null-patch:" + ftpPath);
            }

            if (in != null) {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int num;
                while ((num = in.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, num);
                }
                byteArrayOutputStream.flush();
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                in.close();
                ftpClient.logout();
                return new ByteArrayInputStream(byteArray);
            }
            //return mockMultipartFile;
            //return pdfBase64;
        } catch (FileNotFoundException e) {
            log.error(",download--NOT FOUND--FILE," + ftpPath + "没有找到文件");
            log.error(e.getMessage(), e);
        } catch (SocketException e) {
            log.error("download--,ftp-con-error--连接FTP失败.");
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error("download--read-error---文件读取错误。");
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error("download--read-error---文件下载错误。");
            log.error(e.getMessage(), e);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (ftpClient != null) {
                    ftpClient.logout();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static FTPClient getFTPClient(String ftpHost, String ftpUserName,
                                          String ftpPassword, int ftpPort) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = new FTPClient();
            //ftpClient.setControlEncoding("GBK"); // 中文支持
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setDefaultTimeout(20 * 1000);
            ftpClient.setDataTimeout(20 * 1000);
            //ftpClient.setSoTimeout(20*1000);
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                log.info("UNCONNECT,PASSWORD ERROR 未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                log.info("FTP - CONN - SUCCESS连接成功。");
            }
        } catch (SocketException e) {
            log.error("FTP ,IP-ERROR--的IP地址可能错误，请正确配置。");
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error("FTP,,PORT-ERROR--的端口错误,请正确配置。");
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error("FTP,,ERROR。");
            log.error(e.getMessage(), e);
        }
        return ftpClient;
    }
}
