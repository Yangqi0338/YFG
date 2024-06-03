package com.base.sbc.module.common.utils;


import com.base.sbc.config.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

@Slf4j
public class ImageUtils {

    /**
     * 图片按比例压缩
     * @author huanghao
     * @date 2022/3/12 8:58
     * @param file
     * @return java.io.File
     */
    public static MultipartFile compressImage(MultipartFile file) {
        long time = System.currentTimeMillis();
        File targetFile = null;
        File newFile = null;
        try{
            String contentType = file.getContentType();
            String imageName = file.getOriginalFilename();
            String path = UUID.randomUUID() +imageName;
            FileOutputStream outputStream = new FileOutputStream(path);
            targetFile = VideoUtil.toFile(file);
            Thumbnails.of(targetFile)
                    .scale(0.7f) //图片大小（长宽）压缩 从0按照
                    .outputQuality(1f) //图片质量压缩比例 从0-1，越接近1质量越好
                    .toOutputStream(outputStream);
            outputStream.close();
            //file转MultipartFile
            newFile = new File(path);
            FileInputStream inputStream = new FileInputStream(newFile);
            MultipartFile multipartFile = new MockMultipartFile(imageName, imageName, StringUtils.isNotBlank(contentType) ? contentType :
                    ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            log.info("图片压缩总耗时：" + (System.currentTimeMillis() - time)/1000);
            return multipartFile;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if (targetFile != null){
                targetFile.delete();
            }
            if (newFile != null){
                newFile.delete();
            }
        }
        return file;
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

}
