package com.base.sbc.config.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * ftp图片获取下载到本地
 * 图片上传到远程服务地址
 * @author yangshanshan
 *
 */
@Slf4j
public class ImgUtils {

	/** 本地字符编码 */
	private static String LOCAL_CHARSET = "GBK";
	// FTP协议里面，规定文件名编码为iso-8859-1
	private static String SERVER_CHARSET = "ISO-8859-1";
	/*
	 * Description: 从FTP服务器下载文件
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param remotePath FTP服务器上的相对路径
	 * @param fileName 要下载的文件名
	 * @param localPath 下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, int port, String username,String password, String remotePath, String fileName,String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ignored) {
				}
			}
		}
		return success;
	}
	
	
	
	 
	/**
	 *取ftp上的图片到本地
	 *@param ftpClient  ftp上图片的名称
	 *@param fileName   ftp服务器  
	 *@param filePath  FTP服务器上的相对路径   如 a/b/c
	 *@param flocalFilePath 存储到本地的文件目录
	 *@return
	 *@throws IOException
	 */
	public static  boolean downloadFile(FTPClient ftpClient, String fileName,String filePath,String flocalFilePath) throws IOException {
		try {
			ftpClient.changeWorkingDirectory(filePath);
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(flocalFilePath + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpClient.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ftpClient.logout();
		boolean success = true;
		return success;

	}

	/**
	 * 获取FTPClient
	 * @param url 远程ftp地址
	 * @param port 远程服务端口
	 * @param name 登录名
	 * @param pwd 密码
	 * @return FTPClient
	 */
	public static FTPClient ftps(String url, int port, String name, String pwd) {
		int reply;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器


			if (ftp.login(name, pwd)) { // 登录
				if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
						LOCAL_CHARSET = "UTF-8";
				}
				ftp.setControlEncoding(LOCAL_CHARSET);
				ftp.enterLocalPassiveMode();// 设置被动模式
				ftp.setFileType(FTP.BINARY_FILE_TYPE);// 设置传输的模式
			} else {
					log.info	("Connet ftpServer error! Please check user or password");
				}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ftp;
	}



	 /**
	 * 上传图片
	 * @param urlStr 远程接收图片服务地址
	 * @param textMap  接送参数
	 * @param fileMap  文件参数 （传入的本地文件的路径）
	 * @return
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuilder strBuf = new StringBuilder();
				/*if (strBuf.length() == 0) {
					return null;
				}*/
                for (Map.Entry<String, String> entry : textMap.entrySet()) {
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
                for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                    String inputName = entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();

                    if (filename.endsWith("bmp")) {
                        String newPath = covertBmpToJpg(inputValue);
                        file = new File(newPath);
                    }

					String contentType = FileUtil.getMimeType(file.toPath());

					String strBuf = "\r\n" + "--" + BOUNDARY + "\r\n" +
							"Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n" +
							"Content-Type:" + contentType + "\r\n\r\n";

                    out.write(strBuf.getBytes());
                    DataInputStream in = new DataInputStream(Files.newInputStream(file.toPath()));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuilder strBuf = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			log.error("调用丽晶图片上传服务异常：" + e);
			e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    public static String uploadDesignPic(String url, Map<String, Object> form) {
        String post = HttpUtil.post(url, form);
        return post;
    }

    private static String covertBmpToJpg(String path) {
        String newPath = "";
        FileOutputStream out = null;
        try {
            File file = new File(path);
            Image img = ImageIO.read(file);
            BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 创建JPG图像
            BufferedImage jpgImage = new BufferedImage(tag.getWidth(), tag.getHeight(), BufferedImage.TYPE_INT_RGB);
			jpgImage.createGraphics().drawImage(tag, 0, 0, Color.WHITE, null);
			// 将JPG图像保存为文件
			ImageIO.write(jpgImage, "jpg", new File(newPath));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("bmp格式图片转jpg异常：" + e.toString());
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return newPath;
	}
}
