package com.example.handle.controller;

import com.example.handle.function.JWTUtils;
import com.example.handle.model.Image_info;
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
import com.example.handle.dto.resultdata.*;


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

    @ApiOperation("从数据库列出工程队名")
    @GetMapping("/getEngineeringTeamName")
    public ApiResponse<?> getEngineeringTeamName() {
        List<Engineering_1> data = handleService.getEngineeringTeamName();
        return ApiResponse.success(Collections.singletonMap("result", data));
    }

    @ApiOperation("添加图片时从岩芯表获得岩芯信息")
    @GetMapping("/getStratumName")
    public ApiResponse<?> getStratumName() {
        List<Stratums_1> data = handleService.getStratumName();
        return ApiResponse.success(Collections.singletonMap("result", data));
    }

    
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
//            System.out.println(file.exists());
//            System.out.println(uploadDir + "/" + fileName);
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

    @ApiOperation("用户登录")
    @PostMapping("/post/login")
    public ApiResponse<?> loginUser(@RequestBody Map<String, String> receivedData) {
        String username = receivedData.get("username");
        String password = receivedData.get("password");
        int result = handleService.getResultByNamePassword(username, password);
        if (result == 1) {
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
    public ApiResponse<?> login(@RequestBody Map<String, String> receivedData) {
        //Map<String, Object> responseBody = new HashMap<>();
        String username = receivedData.get("username");
        String password = receivedData.get("password");
        String name = receivedData.get("name");
        String sex = receivedData.get("sex");
        String number = receivedData.get("number");
        String id = receivedData.get("id");
        String email = receivedData.get("email");
        String tel = receivedData.get("tel");
        String en = receivedData.get("en");
        int result = 0;
        try {
            result = handleService.insertUser(username, number, password, name, sex, id, email, tel, en);
        } catch (DataAccessException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                //responseBody.put("message", "工号重复");
                return ApiResponse.fail("工号重复");
            }
        }
        if (result == 1) {
            return ApiResponse.success(Collections.singletonMap("result", "注册成功"));
        } else {
            //responseBody.put("message", "存在其他问题");
            return ApiResponse.fail("存在其他问题");
        }
    }

    @ApiOperation("用户信息token")
    @GetMapping("/userinfo")
    public ApiResponse<?> userinfo(HttpServletRequest request) {
        Users result;
        // String token = request.getHeader("token");
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        DecodedJWT verify = JWTUtils.verify(token);
        String account = verify.getClaim("userId").asString();
        // System.out.println(token);
        // System.out.println(account);
        try {
            result = handleService.getUserInfoByAccount(account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("账号出错");
        }
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("获得图片信息")
    @GetMapping("/change/showImageInfo")
    public ApiResponse<?> showImageInfo(@RequestParam String imageName) {
        List<Image_info> result;
        try {
            result = handleService.getImageInfoByName(imageName);
        } catch (DataAccessException e) {
            return ApiResponse.fail("账号出错");
        }
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
        String imageSid = receivedData.get("imageSid");
        Double imageStart = Double.parseDouble(receivedData.get("imageStart"));
        Double imageEnd = Double.parseDouble(receivedData.get("imageEnd"));
        Double imageDepth = Double.parseDouble(receivedData.get("imageDepth"));
        String imageType = receivedData.get("imageType");
        String imageNameOld = receivedData.get("imageNameOld");
        try {
            handleService.updateImageInfoByName(imageName, imageSid, imageStart, imageEnd, imageDepth, imageType, imageNameOld);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            //responseBody.put("message", "更新失败");
            return ApiResponse.fail("更新失败");
        }
        File oldFile = new File(uploadDir+"/"+imageNameOld+".jpg");
        File newFile = new File(uploadDir+"/"+imageName+".jpg");
        oldFile.renameTo(newFile);
        return ApiResponse.success(Collections.singletonMap("result", "更新成功"));
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/change/updateSubmitUser")
    public ApiResponse<?> updateSubmitUser(@RequestBody Map<String, String> receivedData) {
        //Map<String, Object> responseBody = new HashMap<>();
        String u_oldAccount = receivedData.get("u_oldAccount");
        String u_account = receivedData.get("u_account");
        String u_num = receivedData.get("u_num");
        String u_password = receivedData.get("u_password");
        String u_name = receivedData.get("u_name");
        String u_sex = receivedData.get("u_sex");
        String u_id = receivedData.get("u_id");
        String u_email = receivedData.get("u_email");
        String u_tel = receivedData.get("u_tel");
        String u_et_name = receivedData.get("u_et_name");
        try {
            handleService.updateUserInfoByAccount(u_oldAccount, u_account, u_num, u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return ApiResponse.fail("更新失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "更新成功"));
    }

    @ApiOperation("删除图片")
    @PostMapping("/change/deleteImage")
    public ApiResponse<?> deleteImage(@RequestBody Map<String, String> receivedData, HttpServletRequest request) {
        //Map<String, Object> responseBody = new HashMap<>();
        String imageName = receivedData.get("imageName");
        String uploadNum = receivedData.get("uploadNum");

        // String token = request.getHeader("token");
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
        //String currentAccount = receivedData.get("currentAccount");
        try {
            handleService.deleteUserInfoByAccount(u_account);
        } catch (DataAccessException e) {
            return ApiResponse.fail("删除失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", "删除成功"));
    }

    @ApiOperation("根据输入参数获得图片信息")
    @PostMapping("/get/getImageInfo")
    public ApiResponse<?> getImageInfo_post(@RequestBody Map<String, String> receivedData) {
        Map<String, Object> params = new HashMap<>();

        List<ImageAndStratum> result;
        params.put("image_sid", receivedData.get("imageSid"));
        params.put("ima_start", receivedData.get("imageStart"));
        params.put("ima_end", receivedData.get("imageEnd"));
        params.put("ima_depth", receivedData.get("imageDepth"));
        params.put("s_type", receivedData.get("imageType"));
        try {
            result = handleService.getImageInfoByDynamicParams(params);
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询失败");
        }
        return ApiResponse.success(Collections.singletonMap("result", result));
    }

    @ApiOperation("根据输入参数获得图片信息")
    @GetMapping("/get/getImageInfo")
    public ApiResponse<?> getImageInfo_get(@RequestParam(required = false) String imageSid,
                                                   @RequestParam(required = false) String imageStart,
                                                   @RequestParam(required = false) String imageEnd,
                                                   @RequestParam(required = false) String imageDepth,
                                                   @RequestParam(required = false) String imageType) {
        Map<String, Object> params = new HashMap<>();

        List<ImageAndStratum> result;
        params.put("image_sid", imageSid);
        params.put("ima_start", imageStart);
        params.put("ima_end", imageEnd);
        params.put("ima_depth", imageDepth);
        params.put("s_type", imageType);
        System.out.println(params);
        try {
            result = handleService.getImageInfoByDynamicParams(params);
        } catch (DataAccessException e) {
            return ApiResponse.fail("无法查询");
        }
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
            return ApiResponse.success(Collections.singletonMap("result", result));
        } catch (DataAccessException e) {
            return ApiResponse.fail("查询失败");
        }
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
                handleService.insertImageInfo(receivedData.get("IMAGE_NAME"),
                        //receivedData.get("IMAGE_PATH"), uploader_num,
                        //强制改变上传路径为后端统一配置
                        uploadDir+"/"+receivedData.get("IMAGE_NAME")+".jpg", uploader_num,
                        receivedData.get("IMAGE_SID"),
                        Double.parseDouble(receivedData.get("IMA_START")), Double.parseDouble(receivedData.get("IMA_END")),
                        Double.parseDouble(receivedData.get("IMA_DEPTH")), receivedData.get("S_TYPE"));
            } catch (DataAccessException e) {
                //responseBody.put("message", e.getMessage());
                return ApiResponse.fail("操作失败"+e.getMessage());
            }
            return ApiResponse.success(Collections.singletonMap("result", "插入成功"));
        } else {
            return ApiResponse.fail("未找到用户信息");
        }
    }



    //    public ResponseEntity<?> login(HttpServletRequest request) {
//        // 获取请求行
//        String requestLine = request.getMethod() + " " + request.getRequestURI() + " " + request.getProtocol();
//        // 获取请求头
//        String headers = Collections.list(request.getHeaderNames())
//                .stream()
//                .map(name -> name + ": " + Collections.list(request.getHeaders(name)))
//                .collect(Collectors.joining("\n"));
//        // 获取请求体
//        String body = null;
//        try (BufferedReader reader = request.getReader()) {
//            body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 打印完整的 HTTP 请求报文内容
//        String requestMessage = requestLine + "\n" + headers + "\n\n" + body;
//        System.out.println("Request Message:\n" + requestMessage);
//
//        // 处理请求
//        // ...
//
//        return ResponseEntity.ok("Request message logged.");
//    }
}
