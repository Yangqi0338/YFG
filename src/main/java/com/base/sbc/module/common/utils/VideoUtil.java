package com.base.sbc.module.common.utils;

import com.base.sbc.config.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;

import java.io.*;
import java.math.BigDecimal;

/**
 * @author Eifini
 */
@Slf4j
public class VideoUtil {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        File file = new File("C:\\Users\\29117\\Desktop\\视频\\31.5.mp4");
        int i = readVideoTime(file);
        File tag = compressionVideo(file, "1080测试.mp4");
        System.err.println();
    }

    public static File compressionVideo(File source,String picName) {
        if(source == null){
            return null;
        }
        String newPath = source.getAbsolutePath().substring(0, source.getAbsolutePath().lastIndexOf(File.separator)).concat(File.separator).concat(picName);
        File target = new File(newPath);
        try {
            MultimediaObject object = new MultimediaObject(source);
            AudioInfo audioInfo = object.getInfo().getAudio();
            // 根据视频大小来判断是否需要进行压缩,
            int maxSize = 100;
            double mb = Math.ceil(source.length()/ 1048576);
            int second = (int)object.getInfo().getDuration()/1000;
            BigDecimal bd = new BigDecimal(String.format("%.4f", mb/second));
            log.info("开始压缩视频了--> 视频每秒平均 "+ bd +" MB ");
            // 视频 > 100MB, 或者每秒 > 0.5 MB 才做压缩， 不需要的话可以把判断去掉
//            boolean temp = mb > maxSize || bd.compareTo(new BigDecimal(0.5)) > 0;
            if(true){
                long time = System.currentTimeMillis();
                //TODO 视频属性设置
                int maxBitRate = 128000;
                int maxSamplingRate = 44100;
                int bitRate = 800000;
                int maxFrameRate = 25;
                int maxWidth = 1280;

                AudioAttributes audio = new AudioAttributes();
                // 设置通用编码格式
                audio.setCodec("aac");
                if (audioInfo != null){
                    // 设置最大值：比特率越高，清晰度/音质越好
                    // 设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 128000 = 182kb)
                    if(audioInfo.getBitRate() > maxBitRate){
                        audio.setBitRate(maxBitRate);
                    }

                    // 设置重新编码的音频流中使用的声道数（1 =单声道，2 = 双声道（立体声））。如果未设置任何声道值，则编码器将选择默认值 0。
                    audio.setChannels(audioInfo.getChannels());
                    // 采样率越高声音的还原度越好，文件越大
                    // 设置音频采样率，单位：赫兹 hz
                    // 设置编码时候的音量值，未设置为0,如果256，则音量值不会改变
                    // audio.setVolume(256);
                    if(audioInfo.getSamplingRate() > maxSamplingRate){
                        audio.setSamplingRate(maxSamplingRate);
                    }
                }


                //TODO 视频编码属性配置
                VideoInfo videoInfo = object.getInfo().getVideo();
                VideoAttributes video = new VideoAttributes();
                video.setCodec("h264");
                if (videoInfo != null){
                    //设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 800000 = 800kb)
                    if(videoInfo.getBitRate() > bitRate){
                        video.setBitRate(bitRate);
                    }

                    // 视频帧率：15 f / s  帧率越低，效果越差
                    // 设置视频帧率（帧率越低，视频会出现断层，越高让人感觉越连续），视频帧率（Frame rate）是用于测量显示帧数的量度。所谓的测量单位为每秒显示帧数(Frames per Second，简：FPS）或“赫兹”（Hz）。
                    if(videoInfo.getFrameRate() > maxFrameRate){
                        video.setFrameRate(maxFrameRate);
                    }

                }
                // 限制视频宽高
//                int width = videoInfo.getSize().getWidth();
//                int height = videoInfo.getSize().getHeight();
//                if(width > maxWidth){
//                    float rat = (float) width / maxWidth;
//                    video.setSize(new VideoSize(maxWidth,(int)(height/rat)));
//                }

                EncodingAttributes attr = new EncodingAttributes();
                attr.setOutputFormat("mp4");
                attr.setAudioAttributes(audio);
                attr.setVideoAttributes(video);

                // 速度最快的压缩方式， 压缩速度 从快到慢： ultrafast, superfast, veryfast, faster, fast, medium,  slow, slower, veryslow and placebo.
//                attr.setPreset(PresetUtil.VERYFAST);
//                attr.setCrf(27);
//                // 设置线程数
                attr.setEncodingThreads(Runtime.getRuntime().availableProcessors()/2);
                Encoder encoder = new Encoder();
                encoder.encode(new MultimediaObject(source), target, attr);
                log.info("压缩总耗时：" + (System.currentTimeMillis() - time)/1000);
                return target;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(target.length() > 0){
                source.delete();
            }
        }
        return source;
    }

    /**
     * MultipartFile 转 File
     * @param file
     * @throws Exception
     */
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

    /**
     *  File 转 MultipartFile
     * @param file
     * @throws Exception
     */
    public static MultipartFile toMultipartFile(File file,String contentType) throws Exception {
        InputStream inputStream = new FileInputStream(file);
        return new MockMultipartFile(file.getName(), file.getName(),
                StringUtils.isNotBlank(contentType)? contentType : "application/octet-stream", inputStream);
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

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    /** 前端上传视频之后，根据上传的视频文件获取视频的大小和时长 1、获取视频时长 */
    public static int readVideoTime(File source) {
        int vedioSecond = Integer.parseInt(parseDuration(source.getAbsolutePath()));
        return vedioSecond;
    }

    /**
     * 视频时长
     *
     * @param fileUrl
     * @return String[] 0=秒时长，1=展示时长（格式如 01:00:00）
     */
    public static String parseDuration(String fileUrl) {
        long ls = 0L;
        String[] length = new String[2];
        try {
            //
//            URL source = new URL(fileUrl);
            // 构造方法 接受URL对象
//            MultimediaObject instance = new MultimediaObject(source);
            // 构造方法 接受File对象
            MultimediaObject instance = new MultimediaObject(new File(fileUrl));
            MultimediaInfo result = instance.getInfo();
            ls = result.getDuration() / 1000;
            length[0] = String.valueOf(ls);
            Integer hour = (int) (ls / 3600);
            Integer minute = (int) (ls % 3600) / 60;
            Integer second = (int) (ls - hour * 3600 - minute * 60);
            String hr = hour.toString();
            String mi = minute.toString();
            String se = second.toString();
            if (hr.length() < 2) {
                hr = "0" + hr;
            }
            if (mi.length() < 2) {
                mi = "0" + mi;
            }
            if (se.length() < 2) {
                se = "0" + se;
            }

            String noHour = "00";
            if (noHour.equals(hr)) {
                length[1] = mi + ":" + se;
            } else {
                length[1] = hr + ":" + mi + ":" + se;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(length);//{"20","00:20"}
        return String.valueOf(ls);
    }


}

