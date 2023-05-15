package com.base.sbc.config.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.base.sbc.config.common.ApiResult;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.constant.BaseConstant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class FilesUtils {

    protected static final Logger logger = LoggerFactory.getLogger(FilesUtils.class);

    @Value("${oss.aliyun.endpoint}")
    private String endpoint;

    @Value("${oss.aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.aliyun.bucketName}")
    private String bucketName;

    /**
     * 产品
     */
    public static final String PRODUCT = "/product/";
    /**
     * 产品
     */
    public static final String PRODUCT_EXCEL = "/product/excel/";


    /**
     * https
     */
    public static final String HTTPS = "https://";
    /**
     * http
     */
    public static final String HTTP = "http://";

    public static final String SUCCESS = "SUCCESS";

    /**
     * 上传文件
     *
     * @param file    页面传递的文件
     * @param folder  保存的文件夹("/userHead")  ApiResult内有公开属性
     * @param request 请求，获取头部信息
     * @return 保存成功的地址
     * @throws IOException
     * @throws ClientException
     * @throws OSSException
     */
    public ApiResult upload(MultipartFile file, String folder, HttpServletRequest request) throws OSSException, ClientException, IOException {
        String applyCompany = request.getHeader(BaseConstant.APPLY_COMPANY);
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
        //公司id - 分类 - 月  - 日  - 文件
        folder = folder + DateUtils.getMonth() + BaseGlobal.G + DateUtils.getDay() + BaseGlobal.G;
        ossClient.putObject(bucketName, applyCompany + folder + fileName, file.getInputStream());//对象的内容
        ossClient.shutdown();// 关闭OSSClient。
        String fileURL = HTTPS + bucketName + endpoint.replace(HTTP, BaseGlobal.DI) + BaseGlobal.G + applyCompany + folder + fileName;//获取图片返回的地址
        return ApiResult.success(SUCCESS, fileURL);
    }

    /***
     * excel导入时上传图片 传入一个map 返回一个map
     * @param file 参数形式为 (行_列，文件) 即(1_2,File) 第一行第二列的文件
     * @param folder 文件夹名称
     * @param ext 后缀地址名称 参数形式为 (行_列，后缀) 即(1_2,excName) 第一行第二列的文件后缀名称
     * @param request
     * @return 返回一个 (行_列,地址List) 即(1_2,[http1,http2])
     * @throws OSSException
     * @throws ClientException
     * @throws IOException
     */
    public ApiResult upload(Map<String, List<File>> file, String folder, Map<String, List<String>> ext, HttpServletRequest request)
            throws OSSException, ClientException, IOException {
        String applyCompany = request.getHeader(BaseConstant.APPLY_COMPANY);
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
        // 公司id - 分类 - 月 - 日 - 文件
        folder = folder + DateUtils.getMonth() + BaseGlobal.G + DateUtils.getDay() + BaseGlobal.G;
        Map<String, List<String>> MapFile = Maps.newHashMap();
        List<String> urlList = Lists.newArrayList();
        String fileName = "";
        for (Map.Entry<String, List<File>> file1 : file.entrySet()) {
            List<File> fileList = file1.getValue();
            List<String> excList = ext.get(file1.getKey());//后缀名称
            int i = 0;
            for (File file2 : fileList) {
                fileName = UUID.randomUUID().toString();
                if ("jpeg".equals(excList.get(i))) {
                    fileName += ".jpg";
                }
                if ("png".equals(excList.get(i))) {
                    fileName += ".png";
                }
                i++;
                ossClient.putObject(bucketName, applyCompany + folder + fileName, file2);// 对象的内容
                String fileURL = HTTPS + bucketName + endpoint.replace(HTTP, BaseGlobal.DI) + BaseGlobal.G + applyCompany
                        + folder + fileName;// 获取图片返回的地址
                if (MapFile.get(file1.getKey()) != null && MapFile.get(file1.getKey()).size() > 0) {
                    urlList = MapFile.get(file1.getKey());
                    urlList.add(fileURL);
                    MapFile.put(file1.getKey(), urlList);
                } else {
                    urlList = new ArrayList<>();
                    urlList.add(fileURL);
                    MapFile.put(file1.getKey(), urlList);
                }
            }
        }
        ossClient.shutdown();// 关闭OSSClient。
        return ApiResult.success("success", MapFile);
    }

    /**
     * 上传文件,只拿到返回地址
     *
     * @param file   页面传递的文件
     * @param folder 保存的文件夹("/userHead")  ApiResult内有公开属性
     * @return 保存成功的地址
     * @throws IOException
     * @throws ClientException
     * @throws OSSException
     */
    public String upload(MultipartFile file, String folder, String applyCompany) throws OSSException, ClientException, IOException {
        String fileName = file.getOriginalFilename();
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件。<yourLocalFile>由本地文件路径加文件名包括后缀组成，例如/users/local/myfile.txt。
        //公司id - 分类 - 月  - 日  - 文件
        folder = folder + DateUtils.getMonth() + BaseGlobal.G + DateUtils.getDay() + BaseGlobal.G;
        ossClient.putObject(bucketName, applyCompany + folder + fileName, file.getInputStream());//对象的内容
        ossClient.shutdown();// 关闭OSSClient。
        String fileURL = HTTPS + bucketName + endpoint.replace(HTTP, BaseGlobal.DI) + BaseGlobal.G + applyCompany + folder + fileName;//获取图片返回的地址
        return fileURL;
    }

    /***
     * 分片上传
     * @param file
     * @param folder
     * @param request
     * @return
     * @throws Throwable
     */
    public ApiResult uploadBigData(MultipartFile file, String folder, HttpServletRequest request) throws Throwable {
        String applyCompany = request.getHeader(BaseConstant.USER_COMPANY);
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        /* 步骤1：初始化一个分片上传事件。
         */
        InitiateMultipartUploadRequest request1 = new InitiateMultipartUploadRequest(bucketName, applyCompany + folder + fileName);
        InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request1);
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
        String uploadId = result.getUploadId();

        /* 步骤2：上传分片。
         */
        List<PartETag> partETags = new ArrayList<PartETag>();
        // 计算文件有多少个分片。
        final long partSize = 1 * 1024 * 1024L;
        long fileLength = file.getSize();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            // 跳过已经上传的分片。
            file.getInputStream().skip(startPos);
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(applyCompany + folder + fileName);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(file.getInputStream());
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
            uploadPartRequest.setPartNumber(i + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
            partETags.add(uploadPartResult.getPartETag());
        }

        /* 步骤3：完成分片上传。
         */
        // 排序。partETags必须按分片号升序排列。
        Collections.sort(partETags, new Comparator<PartETag>() {
            @Override
            public int compare(PartETag p1, PartETag p2) {
                return p1.getPartNumber() - p2.getPartNumber();
            }
        });
        // 在执行该操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, applyCompany + folder + fileName, uploadId, partETags);
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        ossClient.shutdown();
        //获取图片返回的地址
        String fileURL = HTTPS + bucketName + endpoint.replace(HTTP, BaseGlobal.DI) + BaseGlobal.G + applyCompany + folder + fileName;
        return ApiResult.success(SUCCESS, fileURL);
    }

    /**
     * 上传文件
     *
     * @param name    文件名称
     * @param input   输入流
     * @param folder  保存的文件夹("/userHead")  ApiResult内有公开属性
     * @return 保存成功的地址
     * @throws IOException
     * @throws ClientException
     * @throws OSSException
     */
    public ApiResult uploadFile(String fileName, InputStream input, String folder) throws OSSException, ClientException, IOException {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        folder = folder + DateUtils.getMonth() + BaseGlobal.G + DateUtils.getDay() + BaseGlobal.G;
        //对象的内容
        ossClient.putObject(bucketName, folder + fileName, input);
        ossClient.shutdown();// 关闭OSSClient。
        //获取图片返回的地址
        String fileUrl = HTTPS + bucketName + endpoint.replace(HTTP, BaseGlobal.DI) + "/"+folder + fileName;
        return ApiResult.success(SUCCESS, fileUrl);
    }


    /**
     * file转MultipartFile
     *
     * @param file file
     * @return MultipartFile
     */
    public MultipartFile convertFileToMultipartFile(File file) throws IOException {
        String fileName = file.getName();
        String contentType = "multipart/form-data"; // or find contentType using some utility
        byte[] content = FileCopyUtils.copyToByteArray(file);
        return new MockMultipartFile(fileName, fileName, contentType, content);
    }
}
