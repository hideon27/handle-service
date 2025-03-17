package com.example.handle.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.handle.dto.ApiResponse;
import com.example.handle.function.JWTUtils;
import com.example.handle.mapper.ImageMapper;
import com.example.handle.model.CoreSegments;
import com.example.handle.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageMapper imageMapper;

    @Value("${file_windows.upload-dir}")
    private String uploadDir;

    private final WebClient webClient;

    public ImageServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5000").build();
    }

    @Override
    public String getIdByAccount(String u_account) {
        return imageMapper.getIdByAccount(u_account);
    }

    @Override
    public int insertImageInfo(String imageId, String imageName, String imagePath,
                             String uploaderNum, String stratumId, double stratumLen,
                             double segStart, double segEnd, double segLen,
                             String segType, String sequenceNo) {
        return imageMapper.insertImageInfo(imageId, imageName, imagePath, uploaderNum,
                stratumId, stratumLen, segStart, segEnd, segLen, segType, sequenceNo);
    }

    @Override
    public ApiResponse<?> handleImageUpload(MultipartFile picture) {
        if (picture.isEmpty()) {
            return ApiResponse.fail("未上传文件");
        }
        try {
            String fileName = picture.getOriginalFilename();
            String filePath = uploadDir + "/" + fileName;
            File file = new File(filePath);
            Files.copy(picture.getInputStream(), file.toPath(), 
                      java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return ApiResponse.success("图片上传成功");
        } catch (IOException e) {
            return ApiResponse.fail("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<?> handleImageUploadWithPath(MultipartFile picture) {
        if (picture.isEmpty()) {
            return ApiResponse.fail("未上传文件");
        }
        try {
            String fileName = picture.getOriginalFilename();
            String filePath = uploadDir + "/" + fileName;
            File file = new File(filePath);
            
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            
            picture.transferTo(file);
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("key", file.getPath());
            
            Map response = webClient.post()
                    .uri("/predict")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("class_indict", response.get("class_indict"));
                responseBody.put("prob", response.get("prob"));
                responseBody.put("path", file.getPath());
                return ApiResponse.success(responseBody);
            } else {
                return ApiResponse.fail("No response from server");
            }
        } catch (IOException e) {
            return ApiResponse.fail("文件保存失败: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<?> processImage(String imageId) {
        try {
            String fileName = imageId + ".jpg";
            String filePath = uploadDir + "/" + fileName;
            File file = new File(filePath);
            
            if (!file.exists()) {
                return ApiResponse.fail("图片不存在");
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String base64Image = Base64Utils.encodeToString(fileContent);
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("image_name", fileName);
            requestBody.put("image_data", base64Image);
            
            Map response = webClient.post()
                    .uri("/predict")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            if (response != null) {
                return ApiResponse.success(response);
            } else {
                return ApiResponse.fail("服务器无响应");
            }
        } catch (IOException e) {
            return ApiResponse.fail("图片处理失败: " + e.getMessage());
        }
    }

    @Override
    public String getImageAsBase64(String imageId) throws IOException {
        File file = new File(uploadDir + "/" + imageId + ".jpg");
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        return Base64Utils.encodeToString(imageBytes);
    }

    @Override
    public ApiResponse<?> uploadImageInfo(Map<String, String> receivedData, String token) {
        DecodedJWT verify = JWTUtils.verify(token);
        String account = verify.getClaim("userId").asString();
        String uploader_num = "";
        try {
            uploader_num = getIdByAccount(account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询出错：" + e.getMessage());
        }
        if (!Objects.equals(uploader_num, "")) {
            try {
                insertImageInfo(receivedData.get("IMAGE_ID"),
                        receivedData.get("IMAGE_NAME"),
                        uploadDir + "/" + receivedData.get("IMAGE_ID") + ".jpg",
                        uploader_num,
                        receivedData.get("STRATUM_ID"),
                        Double.parseDouble(receivedData.get("STRATUM_LEN")),
                        Double.parseDouble(receivedData.get("SEG_START")),
                        Double.parseDouble(receivedData.get("SEG_END")),
                        Double.parseDouble(receivedData.get("SEG_LEN")),
                        receivedData.get("SEG_TYPE"),
                        "0");
            } catch (DataAccessException e) {
                return ApiResponse.fail("操作失败" + e.getMessage());
            }
            return ApiResponse.success(Collections.singletonMap("result", "插入成功"));
        } else {
            return ApiResponse.fail("未找到用户信息");
        }
    }

    @Override
    public List<CoreSegments> getImageInfoByIdAndName(String imageId, String imageName) {
        return imageMapper.getImageInfoByIdAndName(imageId, imageName);
    }

    @Override
    public void updateImageInfoByName(String imageName, String stratumId,
                                    double segStart, double segEnd, double segLen,
                                    String segType, String imageId) {
        imageMapper.updateImageInfoByName(imageName, stratumId, segStart, segEnd,
                segLen, segType, imageId);
    }

    @Override
    public void deleteImageInfoByImageId(String image_id) {
        imageMapper.deleteImageInfoByImageId(image_id);
    }
} 