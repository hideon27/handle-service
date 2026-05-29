package com.example.handle.service;

import com.example.handle.dto.ApiResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.List;

public interface ImageService {
    String getIdByAccount(String u_account);
    int insertImageInfo(String imageId, String imageName, String imagePath, String uploaderNum, String stratumId, double stratumLen,
                       double segStart, double segEnd, double segLen, String segType, String sequenceNo);
    ApiResponse<?> handleImageUpload(MultipartFile picture);
    ApiResponse<?> handleImageUploadWithPath(MultipartFile picture);
    ApiResponse<?> processImage(String imageId);
    String getImageAsBase64(String imageId) throws IOException;
    ApiResponse<?> uploadImageInfo(Map<String, String> receivedData, String token);
    List<Map<String, Object>> getImageInfoByIdAndName(String imageId, String imageName,
                                                      String segType, String segLen,
                                                      String segStart, String segEnd,
                                                      String stratumId, String uploaderNum);
    ApiResponse<?> updateImageInfoByName(String imageName, String stratumId, double segStart, double segEnd,
                                         double segLen, String segType, String imageId, String token);
    void deleteImageInfoByImageId(String image_id);
} 
