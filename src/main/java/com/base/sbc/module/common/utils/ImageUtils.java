package com.base.sbc.module.common.utils;


import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.StringUtils;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtils {

    /**
     * 图片按比例压缩
     * @param file
     * @return java.io.File
     */
    public static MultipartFile compressImage(MultipartFile file,Integer uploadImgMix) {

        //小于1M的文件不压缩
        if (!checkFileSize(file.getSize(),1,"M")){
            return file;
        }

        long time = System.currentTimeMillis();
        File targetFile = null;
        File newFile = null;
        try{
            String contentType = file.getContentType();
            String imageName = StringUtils.isNotBlank(file.getOriginalFilename()) ?file.getOriginalFilename() : UUID.randomUUID().toString();
            String path = UUID.randomUUID() +imageName;
            FileOutputStream outputStream = new FileOutputStream(path);
            targetFile = toFile(file);
            Thumbnails.of(targetFile)
                    .scale(0.7f) //图片大小（长宽）压缩 从0按照
                    .outputQuality(0.9f) //图片质量压缩比例 从0-1，越接近1质量越好
                    .toOutputStream(outputStream);
            outputStream.close();
            //file转MultipartFile
            newFile = new File(path);
            if (checkFileSize(newFile.length(),uploadImgMix,"M")){
                throw new OtherException("图片压缩后，大小超出最大限制："+uploadImgMix+"M");
            }
            FileInputStream inputStream = new FileInputStream(newFile);
            MultipartFile multipartFile = new MockMultipartFile(imageName, imageName, StringUtils.isNotBlank(contentType) ? contentType :
                    ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            log.info("图片压缩总耗时：" + (System.currentTimeMillis() - time)/1000);
            return multipartFile;
        }catch (Exception e){
            e.printStackTrace();
            throw new OtherException(e.getMessage());
        }finally{
            if (targetFile != null){
                targetFile.delete();
            }
            if (newFile != null){
                newFile.delete();
            }
        }

    }

    /**
     * 判断文件大小
     *
     * @param len
     *            文件长度
     * @param size
     *            限制大小
     * @param unit
     *            限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize < size) {
            return false;
        }
        return true;
    }


    /**
     * 验证文件是否是图片
     * @author huanghao
     * @date 2022/3/12 9:42
     * @param imgPath
     * @return boolean
     */
    public static boolean checkSuffix(String imgPath) {
        Boolean flag =false;
        //图片格式
        String[] FILETYPES = new String[]{
                ".jpg", ".jpeg", ".png",
                ".JPG", ".JPEG", ".PNG"
        };
        if(!StringUtils.isBlank(imgPath)){
            for (int i = 0; i < FILETYPES.length; i++) {
                String fileType = FILETYPES[i];
                if (imgPath.endsWith(fileType)) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    public static void delImage(String imageName) {
        File file = new File("/usr/local/src/image/"+imageName);
        //判断文件是否存在
        if (file.exists()){
            boolean flag = false;
            flag = file.delete();
            if (flag){
                System.out.println("成功删除图片"+file.getName());
            }else {
                System.out.println("删除失败");
            }
        }
    }
	
	public static File toFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        if (toFile != null) {
            log.info("MultipartFile转File成功:" + toFile.getAbsolutePath());
        }else {
            log.info("MultipartFile转File失败");
        }
        return toFile;
    }
	
	  //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
