package com.base.sbc.config.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.base.sbc.config.utils.FilesUtils.logger;
import static org.eclipse.jetty.util.StringUtil.CRLF;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Values.BOUNDARY;

/**
 * @author 卞康
 * @date 2023/5/26 9:47:54
 * @mail 247967116@qq.com
 */
public class FtpUtils {

    // TODO: 2023/5/26 未完成，测试 

    private static String ip ="10.8.240.175";
    private static String port ="21";
    private static String password ="!qaz2wsx";
    private  static String username  ="spuser";

    private static String filepath ="/Image";



    public static void main(String[] args) throws IOException {
        //System.out.println(FtpUtils.upImgToRgent("Image/1180317228551", "/Image", "Image/1180317228551", "sourceType"));

        FTPClient ftpClient=new FTPClient();
        ftpClient.connect(ip, Integer.parseInt(port));
        boolean login = ftpClient.login(username, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.changeWorkingDirectory("/Image");
        FTPFile[] ftpFiles = ftpClient.listFiles();

        InputStream inputStream = new URL("http://10.8.240.251:9090/eifini-knowledge-base/pdm/2023-05-22/1684745169323.png").openConnection().getInputStream();
        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(outputStream);
        //byte[] byteArray = IOUtils.toByteArray(inputStream);

        boolean b1 = ftpClient.storeFile(new String("1684745169323.png".getBytes("UTF-8"), "iso-8859-1"), inputStream);
        boolean b = ftpClient.storeUniqueFile(inputStream);
        System.out.println(b1);
        System.out.println(b);
        ftpClient.disconnect();


    }
    public static String  upImgToRgent(String fileName, String filePath, String newName, String sourceType) throws IOException {
        FTPClient ftpClient = FtpUtils.ftps(ip, Integer.parseInt(port), username, password);

        logger.info("请求地址：ftp://" + ip + ":" + port + "/" + filePath);
        try {
            // add start 2021.6.30 新增路径编码，防治中文下找不到路径
            filePath = new String(filePath.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            // add end 2021.6.30 新增路径编码，防治中文下找不到路径
            ftpClient.changeWorkingDirectory(filePath);
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                System.out.println(ff.getName());
                //if (ff.getName().equals(fileName)) {
                    File localFile = new File(filepath + ff.getName());
                    OutputStream is = new URL(fileName).openConnection().getOutputStream();

                    //OutputStream is = new FileOutputStream(localFile);
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    // ftpClient.retrieveFile(new String(ff.getName().getBytes("GB2312"),"ISO-8859-1"), is);
                    boolean flag = ftpClient.retrieveFile(new String(ff.getName().getBytes("GB2312"), StandardCharsets.ISO_8859_1), is);
                    if (!flag) { // 此处编码问题导致未上传成功
                        flag = ftpClient.retrieveFile(new String(ff.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), is);
                    }
                    is.close();
                    String path = filepath + ff.getName(); // 本地图片的地址
                    Map<String, String> paraMap = new HashMap<String, String>();
                    paraMap.put("picname", newName); // 图片新的名称，以此名称为准
                    paraMap.put("pictype", "1");// 图片文件类型 0 jpg ；1 png
                    paraMap.put("folderName", sourceType); // 传递plm图片

                    Map<String, String> fileMap = new HashMap<String, String>();
                    fileMap.put("img", path);
                    String res = null;
                    String aim_server=ip+port;
                    res = FtpUtils.formUpload(aim_server, paraMap, fileMap);
                    if (res != null) {
                        System.out.println(res); // {"Sucess":true,"Msg":"成功","Time":"总耗时：4.4831保存耗时：3.3657"}
                        return res;
                    } else {
                        return "{'Sucess':false,'Msg':'失败;失败信息：请求上传图片失败，服务端未返回任何上传结果报文！'}";
                    }
                //} else {
                //    return "{'Sucess':false,'Msg':'失败;失败信息：未找到对应的图片，请核实;图片名称为" + fileName + "；新的名称为：" + newName + ";请求地址：+ftp://" + ip + ":" + port + "/" + filePath + "'}";
                //}
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "{'Sucess':false,'Msg':'失败;失败信息：" + e.toString() + "'}";
        }
        ftpClient.logout();
        return "{'Sucess':false,'Msg':'失败，程序异常出错！'}";
    }










    public static FTPClient ftps(String ip, int port, String username, String password) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(ip, port);
        ftpClient.login(username, password);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }


    public static String formUpload(String uploadUrl, Map<String, String> parameters, Map<String, String> fileMap) throws IOException {
        URL url = new URL(uploadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        OutputStream outputStream = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

        // 添加普通参数
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String paramName = entry.getKey();
                String paramValue = entry.getValue();
                writer.append("--").append(BOUNDARY).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=").append("UTF-8").append(CRLF);
                writer.append(CRLF);
                writer.append(paramValue).append(CRLF);
                writer.flush();
            }
        }

        // 添加文件参数
        if (fileMap != null && !fileMap.isEmpty()) {
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                String paramName = entry.getKey();
                String filePath = entry.getValue();
                File file = new File(filePath);
                if (file.exists()) {
                    writer.append("--").append(BOUNDARY).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"; filename=\"").append(file.getName()).append("\"").append(CRLF);
                    writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(file.getName())).append(CRLF);
                    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                    writer.append(CRLF);
                    writer.flush();

                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    fileInputStream.close();
                    writer.append(CRLF);
                    writer.flush();
                }
            }
        }

        writer.append("--").append(BOUNDARY).append("--").append(CRLF);
        writer.close();

        // 读取响应
        StringBuilder responseBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }
        reader.close();

        return responseBuilder.toString();
    }

}
