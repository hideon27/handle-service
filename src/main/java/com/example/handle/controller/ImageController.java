package com.example.handle.controller;

import com.example.handle.dto.ApiResponse;
import com.example.handle.model.CoreSegments;
import com.example.handle.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@Api(tags = "图片处理接口")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {
    @Autowired
    private ImageService imageService;

    @ApiOperation("上传图片")
    @PostMapping("/image/upload")
    public ApiResponse<?> uploadImage(@RequestParam("picture") MultipartFile picture,
                                    @RequestParam("image_id") String imageId) {
        return imageService.handleImageUpload(picture);
    }

    @ApiOperation("上传图片(路径传递)")
    @PostMapping("/uploadimage_path")
    public ApiResponse<?> uploadImagePath(@RequestParam("picture") MultipartFile picture) {
        return imageService.handleImageUploadWithPath(picture);
    }

    @ApiOperation("处理图片")
    @GetMapping("/image/process")
    public ApiResponse<?> processImage(@RequestParam("image_id") String imageId) {
        return imageService.processImage(imageId);
    }

    @GetMapping("/images/{image_id}")
    public String getImageAsBase64(@PathVariable("image_id") String imageId) throws IOException {
        return imageService.getImageAsBase64(imageId);
    }

    @ApiOperation("上传图片信息")
    @PostMapping("/post/upload")
    public ApiResponse<?> upload(@RequestBody Map<String, String> receivedData,
                               @RequestHeader("Authorization") String token) {
        return imageService.uploadImageInfo(receivedData, token.substring("Bearer ".length()));
    }

    @ApiOperation("获得图片信息")
    @GetMapping("/change/showImageInfo")
    public ApiResponse<?> showImageInfo(@RequestParam(required = false) String imageId,
                                      @RequestParam(required = false) String imageName) {
        List<CoreSegments> result = imageService.getImageInfoByIdAndName(imageId, imageName);
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("更新图片信息")
    @PostMapping("/change/updateSubmit")
    public ApiResponse<?> updateSubmit(@RequestBody Map<String, String> receivedData) {
        String imageId = receivedData.get("imageId");
        String imageName = receivedData.getOrDefault("imageName", "");
        String stratumId = receivedData.getOrDefault("stratumId", "");
        double segStart = Double.parseDouble(receivedData.getOrDefault("segStart", "-1"));
        double segEnd = Double.parseDouble(receivedData.getOrDefault("segEnd", "-1"));
        double segLen = Double.parseDouble(receivedData.getOrDefault("segLen", "-1"));
        String segType = receivedData.getOrDefault("segType", "");
        
        imageService.updateImageInfoByName(imageName, stratumId, segStart, segEnd, segLen, segType, imageId);
        return ApiResponse.success(Collections.singletonMap("result", "更新成功"));
    }

    @ApiOperation("删除图片")
    @PostMapping("/change/deleteImage")
    public ApiResponse<?> deleteImage(@RequestBody Map<String, String> receivedData) {
        imageService.deleteImageInfoByImageId(receivedData.get("imageId"));
        return ApiResponse.success(Collections.singletonMap("result", "删除成功"));
    }
} 