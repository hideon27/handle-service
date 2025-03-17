package com.example.handle.service;

import com.example.handle.dto.ApiResponse;
import com.example.handle.model.CoreSegments;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ImageService {
    String getIdByAccount(String u_account);
    int insertImageInfo(String imageId, String imageName, String imagePath, String uploaderNum, String stratumId, double stratumLen,
                       double segStart, double segEnd, double segLen, String segType, String sequenceNo);
    ApiResponse<?> handleImageUpload(MultipartFile picture);
    ApiResponse<?> handleImageUploadWithPath(MultipartFile picture);
    ApiResponse<?> processImage(String imageId);
    String getImageAsBase64(String imageId) throws IOException;
    ApiResponse<?> uploadImageInfo(Map<String, String> receivedData, String token);
    List<CoreSegments> getImageInfoByIdAndName(String imageId, String imageName);
    void updateImageInfoByName(String imageName, String stratumId, double segStart, double segEnd, double segLen, String segType, String imageId);
    void deleteImageInfoByImageId(String image_id);
} 