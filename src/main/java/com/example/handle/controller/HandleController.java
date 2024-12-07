package com.example.handle.controller;

import com.example.handle.function.JWTUtils;
import com.example.handle.model.CoreSegments;
import com.example.handle.model.Users;
import com.example.handle.service.HandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import com.auth0.jwt.interfaces.DecodedJWT;
import javax.servlet.http.HttpServletRequest;
//import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.nio.file.Files;
import org.springframework.util.Base64Utils;
import com.example.handle.dto.ApiResponse;
import com.example.handle.dto.requestdata.*;
import com.example.handle.dto.resultdata.*;
import com.example.handle.model.Stratums;


@Api(tags = "API接口")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class HandleController {

    @Autowired
    private HandleService handleService;

    @Value("${file.upload-dir}") // 从配置文件中读取上传目录
    private String uploadDir;

    private final WebClient webClient;

    public HandleController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5000").build();  //本地
    }

    // 1. 用户认证相关接口
    @ApiOperation("用户登录")
    @PostMapping("/post/login")
    public ApiResponse<?> loginUser(@RequestBody UserLoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        int result = handleService.getResultByNamePassword(username, password);
        if (result == 1) {
            // 更新最后登录时间
            handleService.updateLastLoginTime(username);
            // 生成token
            Map<String, String> payload = new HashMap<>();
            payload.put("userId", username);
            String token = JWTUtils.getToken(payload);
            return ApiResponse.success(Collections.singletonMap("token", token));
        } else {
            return ApiResponse.fail("账号出错");
        }
    }

    @ApiOperation("管理员登录")
    @PostMapping("/post/adminLogin")
    public ApiResponse<?> loginAdmin(@RequestBody Map<String, String> receivedData) {
        String username = receivedData.get("username");
        String password = receivedData.get("password");
        int result = handleService.getResultByNamePasswordAdmin(username, password);
        if (result == 1) {
            // 更新最后登录时间
            handleService.updateAdminLastLoginTime(username);
            // 生成token
            Map<String, String> payload = new HashMap<>();
            payload.put("userId", username);
            String token = JWTUtils.getToken(payload);
            return ApiResponse.success(Collections.singletonMap("token", token));
        } else {
            return ApiResponse.fail("账号出错");
        }
    }

    @ApiOperation("注册用户")
    @PostMapping("/post/register")
    public ApiResponse<?> register(@RequestBody UserRegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        String name = registerDTO.getName();
        String sex = registerDTO.getSex();
        String number = registerDTO.getNumber();
        String id = registerDTO.getId();
        String email = registerDTO.getEmail();
        String tel = registerDTO.getTel();
        String en = registerDTO.getEn();
        int result = 0;
        try {
            result = handleService.insertUser(username, number, password, name, sex, id, email, tel, en);
        } catch (DataAccessException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                return ApiResponse.fail("工号重复");
            }
        }
        if (result == 1) {
            return ApiResponse.success(Collections.singletonMap("result", "注册成功"));
        } else {
            return ApiResponse.fail("存在其他问题");
        }
    }

    @ApiOperation("用户信息token")
    @GetMapping("/userinfo")
    public ApiResponse<?> userinfo(HttpServletRequest request) {
        Users result;
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        DecodedJWT verify = JWTUtils.verify(token);
        String account = verify.getClaim("userId").asString();
        try {
            result = handleService.getUserInfoByAccount(account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("账号出错");
        }
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    // 2. 图片上传和处理相关接口
    @ApiOperation("上传图片")
    @PostMapping("/uploadimage")
    public ApiResponse<?> uploadImage(@RequestParam("picture") MultipartFile picture) {
        if (picture.isEmpty()) {
            return ApiResponse.fail("未上传文件");
        }
        try {
            // 保存上传的文件
            String fileName = picture.getOriginalFilename();
            String filePath = uploadDir + "/" + fileName;

            System.out.println(filePath);
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                if (!file.exists()) {
                    file.createNewFile();
                    System.out.println("File created: " + filePath);
                } else {
                    System.out.println("File already exists: " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("An error occurred while creating the file.");
            }
            picture.transferTo(file);
            // 构建请求数据
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("key", file.getPath());
            
            // 发送 HTTP 请求到另一个服务器
            Mono<Map> responseMono = webClient.post()
                    .uri("/predict")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class);
            Map<String, Object> response = responseMono.block();

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
        } catch (WebClientResponseException e) {
            return ApiResponse.fail(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/images/{picture_name}")
    public String getImageAsBase64(@PathVariable("picture_name") String picture_name) throws IOException {
        // 读取图片文件
        File file = new File(uploadDir+"/"+picture_name);
        byte[] imageBytes = Files.readAllBytes(file.toPath());

        return Base64Utils.encodeToString(imageBytes);
    }

    @ApiOperation("上传图片")
    @PostMapping("/post/upload")
    public ApiResponse<?> upload(@RequestBody Map<String, String> receivedData, HttpServletRequest request) {
        // String token = request.getHeader("token");
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        DecodedJWT verify = JWTUtils.verify(token);
        String account = verify.getClaim("userId").asString();
        String uploader_num = "";
        try {
            uploader_num = handleService.getIdByAccount(account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询出错：" + e.getMessage());
        }
        if (!Objects.equals(uploader_num, "")) {
            try {
                handleService.insertImageInfo(receivedData.get("IMAGE_ID"),receivedData.get("IMAGE_NAME"),
                        uploadDir+"/"+receivedData.get("IMAGE_NAME")+".jpg", 
                        uploader_num,
                        receivedData.get("STRATUM_ID"),
                        Double.parseDouble(receivedData.get("STRATUM_LEN")),
                        Double.parseDouble(receivedData.get("SEG_START")), 
                        Double.parseDouble(receivedData.get("SEG_END")),
                        Double.parseDouble(receivedData.get("SEG_LEN")), 
                        receivedData.get("SEG_TYPE"),
                        "0");
            } catch (DataAccessException e) {
                return ApiResponse.fail("操作失败"+e.getMessage());
            }
            return ApiResponse.success(Collections.singletonMap("result", "插入成功"));
        } else {
            return ApiResponse.fail("未找到用户信息");
        }
    }

    // 3. 基础数据查询接口
    @ApiOperation("从数据库列出工程队名")
    @GetMapping("/getEngineeringTeamName")
    public ApiResponse<?> getEngineeringTeamName() {
        List<EngineeringDTO> data = handleService.getEngineeringTeamName();
        return ApiResponse.success(Collections.singletonMap("result", data));
    }

    @ApiOperation("添加图片时从岩芯表获得岩芯信息")
    @GetMapping("/getStratumName")
    public ApiResponse<?> getStratumName() {
        List<StratumDTO> data = handleService.getStratumName();
        return ApiResponse.success(Collections.singletonMap("result", data));
    }

    @ApiOperation("获得图片信息")
    @GetMapping("/change/showImageInfo")
    public ApiResponse<?> showImageInfo(@RequestParam String imageName) {
        List<CoreSegments> result;
        try {
            result = handleService.getImageInfoByName(imageName);
        } catch (DataAccessException e) {
            return ApiResponse.fail("账号出错");
        }
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("获得地层信息")
    @GetMapping("/change/showStratumInfo")
    public ApiResponse<?> showStratumInfo(@RequestParam String stratumId) {
        List<Stratums> result;
        try {
            result = handleService.getStratumInfoByName(stratumId);
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("根据输入参数获得图片信息(segType和imageName都为模糊查询)")
    @PostMapping("/get/getImageInfo")
    public ApiResponse<?> getImageInfo(@RequestBody CoreSegmentQueryDTO queryDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("image_name", queryDTO.getImageName());
        params.put("seg_start", queryDTO.getSegStart());
        params.put("seg_end", queryDTO.getSegEnd());
        params.put("seg_len", queryDTO.getSegLen());
        params.put("seg_type", queryDTO.getSegType());
        params.put("stratum_id", queryDTO.getStratumId());
        params.put("uploader_num", queryDTO.getUploaderNum());
        
        List<ImageAndStratumDTO> result = handleService.getImageInfoByDynamicParams(params);
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("根据输入参数获得图片信息")
    @GetMapping("/get/getImageInfo")
    public ApiResponse<?> getImageInfo_get(@RequestParam(required = false) String imageName,
                                                   @RequestParam(required = false) String segStart,
                                                   @RequestParam(required = false) String segEnd,
                                                   @RequestParam(required = false) String segLen,
                                                   @RequestParam(required = false) String segType,
                                                   @RequestParam(required = false) String stratumId,
                                                   @RequestParam(required = false) String uploaderNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("image_name", imageName);
        params.put("seg_start", segStart);
        params.put("seg_end", segEnd);
        params.put("seg_len", segLen);
        params.put("seg_type", segType);
        params.put("stratum_id", stratumId);
        params.put("uploader_num", uploaderNum);
        List<ImageAndStratumDTO> result = handleService.getImageInfoByDynamicParams(params);
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("获得用户信息")
    @GetMapping("/change/showUserInfo")
    public ApiResponse<?> showUserInfo(@RequestParam String u_account) {
        List<Users> result;
        try {
            result = handleService.getUserInfoByName(u_account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("账号出错");
        }
        // 返回用户信息的成功响应
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("根据输入参数获得用户信息")
    @GetMapping("/get/getUserInfo")
    public ApiResponse<?> getUserInfo_get(@RequestParam(required = false) String account,
                                               @RequestParam(required = false) String num,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String sex,
                                               @RequestParam(required = false) String tel) {
        Map<String, Object> params = new HashMap<>();
        List<Users> result;
        params.put("u_account", account);
        params.put("u_num", num);
        params.put("u_name", name);
        params.put("u_sex", sex);
        params.put("u_tel", tel);
        System.out.println(params);
        try {
            result = handleService.getUserInfoByDynamicParams(params);
            Map<String, Object> response = new HashMap<>();
            response.put("result", result);
            return ApiResponse.success(response);
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询失败");
        }
    }

    @ApiOperation("根据输入参数获得地层信息(id,name,add,pro都为模糊查询)")
    @GetMapping("/get/getStratumInfo")
    public ApiResponse<?> getStratumInfo(@RequestParam(required = false) String stratumId,
                                       @RequestParam(required = false) String stratumName,
                                       @RequestParam(required = false) String stratumLen,
                                       @RequestParam(required = false) String stratumAdd,
                                       @RequestParam(required = false) String stratumPro,
                                       @RequestParam(required = false) String stratumIntegrity) {
        Map<String, Object> params = new HashMap<>();
        params.put("stratum_id", stratumId);
        params.put("stratum_name", stratumName);
        params.put("stratum_len", stratumLen);
        params.put("stratum_add", stratumAdd);
        params.put("stratum_pro", stratumPro);
        params.put("integrity", stratumIntegrity);

        try {
            List<Stratums> result = handleService.getStratumInfoByDynamicParams(params);
            Map<String, Object> response = new HashMap<>();
            response.put("result", result);
            return ApiResponse.success(response);
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询失败");
        }
    }

    // 4. 信息管理相关接口
    @ApiOperation("更新图片信息获取token")
    @GetMapping("/change/updateImageInfo")
    public ApiResponse<?> updateImageInfo(@RequestParam String uploadNum, HttpServletRequest request) {
        // String token = request.getHeader("token");
        String token = request.getHeader("Authorization").substring("Bearer ".length());

        System.out.println(token);
        DecodedJWT verify = JWTUtils.verify(token);
        String account = verify.getClaim("userId").asString();

        List<Users> result;
        try {
            result = handleService.getUserInfoByAccNum(account, uploadNum);
        } catch (DataAccessException e) {
            return ApiResponse.fail("账号出错");
        }
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("更新图片信息")
    @PostMapping("/change/updateSubmit")
    public ApiResponse<?> updateSubmit(@RequestBody Map<String, String> receivedData) {
        String imageName = receivedData.get("imageName");
        String stratumId = receivedData.get("stratumId");
        double segStart = Double.parseDouble(receivedData.get("segStart"));
        double segEnd = Double.parseDouble(receivedData.get("segEnd"));
        double segLen = Double.parseDouble(receivedData.get("segLen"));
        String segType = receivedData.get("segType");
        String oldImageName = receivedData.get("oldImageName");
        try {
            handleService.updateImageInfoByName(imageName, stratumId, segStart, segEnd, segLen, segType, oldImageName);
            // // 重命名文件
            // File oldFile = new File(uploadDir + "/" + oldImageName + ".jpg");
            // File newFile = new File(uploadDir + "/" + imageName + ".jpg");
            // oldFile.renameTo(newFile);
        } catch (DataAccessException e) {
            return ApiResponse.fail("更新失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "更新成功"));
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/change/updateSubmitUser")
    public ApiResponse<?> updateSubmitUser(@RequestBody Map<String, String> receivedData) {
        //Map<String, Object> responseBody = new HashMap<>();
        String u_oldAccount = receivedData.get("uOldAccount");
        String u_account = receivedData.get("uAccount");
        String u_num = receivedData.get("uNum");
        String u_password = receivedData.get("uPassword");
        String u_name = receivedData.get("uName");
        String u_sex = receivedData.get("uSex");
        String u_id = receivedData.get("uId");
        String u_email = receivedData.get("uEmail");
        String u_tel = receivedData.get("uTel");
        String u_et_name = receivedData.get("uEtName");
        try {
            handleService.updateUserInfoByAccount(u_oldAccount, u_account, u_num, u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return ApiResponse.fail("更新失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "更新成功"));
    }

    
    @ApiOperation("更新地层信息")
    @PostMapping("/change/updateSubmitStratum")
    public ApiResponse<?> updateSubmitStratum(@RequestBody Map<String, String> receivedData) {
        String oldStratumId = receivedData.get("oldStratumId");
        String stratumId = receivedData.get("stratumId");
        String stratumName = receivedData.get("stratumName");
        double stratumLen = Double.parseDouble(receivedData.get("stratumLen"));
        String stratumAdd = receivedData.get("stratumAdd");
        String stratumPro = receivedData.get("stratumPro");
        String stratumIntegrity = receivedData.get("stratumIntegrity");

        try {
            handleService.updateStratumInfoById(oldStratumId, stratumId, stratumName, stratumLen, stratumAdd, stratumPro, stratumIntegrity);
        } catch (DataAccessException e) {
            return ApiResponse.fail("更新失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "更新成功"));
    }

    @ApiOperation("插入岩柱信息")
    @PostMapping("/post/insertStratum")
    public ApiResponse<?> insertStratum(@RequestBody Map<String, String> receivedData) {
        String stratumId = receivedData.get("stratumId");
        String stratumName = receivedData.get("stratumName");
        double stratumLen = Double.parseDouble(receivedData.get("stratumLen"));
        String stratumAdd = receivedData.get("stratumAdd");
        String stratumPro = receivedData.get("stratumPro");
        try {
            int result = handleService.insertStratumInfo(stratumId, stratumName, stratumLen, stratumAdd, stratumPro);
            if (result == 1) {
                return ApiResponse.success(Collections.singletonMap("result", "插入成功"));
            } else {
                return ApiResponse.fail("插入失败");
            }
        } catch (DataAccessException e) {
            return ApiResponse.fail("插入失败：" + e.getMessage());
        }
    }

    @ApiOperation("检查岩柱完整性(触发式)")
    @GetMapping("/get/checkStratumIntegrity_trigger")
    public ApiResponse<?> checkStratumIntegrity_trigger(@RequestParam String stratumId) {
        //插入岩芯信息时检查岩柱完整性(触发式)，检查某个stratumId的完整性
        try {
            Map<String, Object> result = handleService.validateStratumIntegrity(stratumId);
            return ApiResponse.success(Collections.singletonMap("result", result));
        } catch (Exception e) {
            return ApiResponse.fail(e.getMessage());
        }
    }


    @ApiOperation("检查岩柱完整性(非触发式)")
    @GetMapping("/get/checkStratumIntegrity_notrigger")
    public ApiResponse<?> checkStratumIntegrity_notrigger() {
        //一般是全局检查，速度较慢，可以设置定时任务，防止事务导致触发式失效
        //TODO: 检查岩柱完整性
        return ApiResponse.success(Collections.singletonMap("result", "检查成功"));
    }


//    @ApiOperation("编录")
//    @PostMapping("/Catalog")
//    public ApiResponse<?> Catalog(@RequestBody Map<String, String> receivedData, HttpServletRequest request) {
//        String u_account = receivedData.get("u_account");
//        try {
//            handleService.deleteUserInfoByAccount(u_account);
//        } catch (DataAccessException e) {
//            return ApiResponse.fail("删除失败");
//        }
//        return ApiResponse.success(Collections.singletonMap("result", "删除成功"));
//    }

    @ApiOperation("删除图片")
    @PostMapping("/change/deleteImage")
    public ApiResponse<?> deleteImage(@RequestBody Map<String, String> receivedData, HttpServletRequest request) {
        String imageName = receivedData.get("imageName");
        String uploadNum = receivedData.get("uploadNum");
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        DecodedJWT verify = JWTUtils.verify(token);
        String account = verify.getClaim("userId").asString();

        try {
            handleService.getUserInfoByAccNum(account, uploadNum);
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询数据库失败");
        }
        try {
            handleService.deleteImageInfoByImageName(imageName);
        } catch (DataAccessException e) {
            return ApiResponse.fail("删除失败");
        }
        new File(uploadDir+"/"+imageName+".jpg").delete();
        return ApiResponse.success(Collections.singletonMap("result", "删除成功"));
    }

    @ApiOperation("删除用户")
    @PostMapping("/change/deleteUser")
    public ApiResponse<?> deleteUser(@RequestBody Map<String, String> receivedData, HttpServletRequest request) {
        String u_account = receivedData.get("u_account");
        try {
            handleService.deleteUserInfoByAccount(u_account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("删除失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "删除成功"));
    }

    @ApiOperation("删除地层")
    @PostMapping("/change/deleteStratum")
    public ApiResponse<?> deleteStratum(@RequestBody Map<String, String> receivedData) {
        String stratumId = receivedData.get("stratumId");
        try {
            handleService.deleteStratumInfoById(stratumId);
        } catch (DataAccessException e) {
            return ApiResponse.fail("删除失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "删除成功"));
    }
    
}
