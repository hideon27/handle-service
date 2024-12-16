package com.example.handle.service;

import com.example.handle.mapper.HandleMapper;
import com.example.handle.model.CoreSegments;
import com.example.handle.model.Users;
//import com.example.handle.model.Administrators;
import com.example.handle.model.Stratums;
import com.example.handle.dto.resultdata.EngineeringDTO;
import com.example.handle.dto.resultdata.ImageAndStratumDTO;
import com.example.handle.dto.resultdata.StratumDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.example.handle.dto.resultdata.StratumSegmentDTO;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HandleService {
    @Autowired
    private HandleMapper handleMapper;

    //// 1. 用户认证相关方法
    public int getResultByNamePassword(String username, String password) {
        return handleMapper.getResultByNamePassword(username, password);
    }

    public int getResultByNamePasswordAdmin(String username, String password) {
        return handleMapper.getResultByNamePasswordAdmin(username, password);
    }

    public int insertUser(String username, String number, String password, String name,
                         String sex, String id, String email, String tel, String en) throws DataAccessException {
        return handleMapper.insertUser(username, number, password, name, sex, id, email, tel, en);
    }

    public void updateLastLoginTime(String uAccount) {
        handleMapper.updateLastLoginTime(uAccount);
    }

    public void updateAdminLastLoginTime(String aAccount) {
        handleMapper.updateAdminLastLoginTime(aAccount);
    }

    public Users getUserInfoByAccount(String name) throws DataAccessException {
        return handleMapper.getUserByAccount(name);
    }

    //// 2. 图片上传和处理相关方法
    public String getIdByAccount(String u_account) throws DataAccessException {
        return handleMapper.getIdByAccount(u_account);
    }

    public int insertImageInfo(String imageId, String imageName, String imagePath,
                             String uploaderNum, String stratumId, double stratumLen,
                             double segStart, double segEnd, double segLen,
                             String segType, String sequenceNo) throws DataAccessException {
        return handleMapper.insertImageInfo(imageId, imageName, imagePath, uploaderNum,
                stratumId, stratumLen, segStart, segEnd, segLen, segType, sequenceNo);
    }

    public int insertStratumInfo(String stratumId, String stratumName, double stratumLen,
                              String stratumAdd, String stratumPro) throws DataAccessException {
        return handleMapper.insertStratumInfo(stratumId, stratumName, stratumLen, stratumAdd, stratumPro);
    }


    public Map<String, Object> validateStratumIntegrity(String stratumId) {
        Double stratumLength = handleMapper.getStratumLength(stratumId);
        Double totalSegmentLength = handleMapper.getTotalSegmentLength(stratumId);
        List<StratumSegmentDTO> segments = handleMapper.getStratumAndSegments(stratumId);
        
        boolean isValid = stratumLength != null && totalSegmentLength != null && 
                         Math.abs(stratumLength - totalSegmentLength) < 0.001;
        
        if (isValid) {
            segments.sort((a, b) -> Double.compare(a.getSegStart(), b.getSegStart()));
            int sequenceNo = 1;
            for (StratumSegmentDTO segment : segments) {
                segment.setSequenceNo(sequenceNo);
                handleMapper.updateSequenceNo(stratumId, segment.getSegStart(), sequenceNo);
                sequenceNo++;
            }
        }

        // 更新岩柱完整性
        handleMapper.updateStratumIntegrity(stratumId, isValid ? "YES" : "NO");

        Map<String, Object> result = new HashMap<>();
        result.put("isValid", isValid);
        result.put("stratumInfo", segments);
        return result;
    }



    //// 3. 基础数据查询方法
    public List<EngineeringDTO> getEngineeringTeamName() {
        return handleMapper.getEngineeringTeamName();
    }

    public List<StratumDTO> getStratumName() {
        return handleMapper.getStratumName();
    }

    public List<CoreSegments> getImageInfoByName(String image_name) throws DataAccessException {
        return handleMapper.getImageInfoByName(image_name);
    }

    public List<ImageAndStratumDTO> getImageInfoByDynamicParams(Map<String, Object> params) {
        return handleMapper.getImageInfoByDynamicParams(params);
    }

    public List<Users> getUserInfoByName(String u_account) throws DataAccessException {
        return handleMapper.getUserInfoByName(u_account);
    }

    public List<Users> getUserInfoByDynamicParams(Map<String, Object> params) throws DataAccessException {
        return handleMapper.getUserInfoByDynamicParams(params);
    }

    public List<Stratums> getStratumInfoByName(String stratum_id) throws DataAccessException {
        return handleMapper.getStratumInfoByName(stratum_id);
    }

    public List<Stratums> getStratumInfoByDynamicParams(Map<String, Object> params) throws DataAccessException {
        return handleMapper.getStratumInfoByDynamicParams(params);
    }

    //// 4. 信息管理相关方法
    public List<Users> getUserInfoByAccNum(String account, String num) throws DataAccessException {
        return handleMapper.getUserInfoByAccNum(account, num);
    }

    public void updateImageInfoByName(String imageName, String stratumId,
                                    double segStart, double segEnd, double segLen,
                                    String segType, String oldImageName) {
        handleMapper.updateImageInfoByName(imageName, stratumId, segStart, segEnd,
                segLen, segType, oldImageName);
    }

    public void updateUserInfoByAccount(String u_oldAccount, String u_account,
                                      String u_num, String u_password, String u_name,
                                      String u_sex, String u_id, String u_email,
                                      String u_tel, String u_et_name) throws DataAccessException {
        handleMapper.updateUserInfoByAccount(u_oldAccount, u_account, u_num,
                u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name);
    }

    public void updateStratumInfoById(String oldStratumId, String stratumId,
                                    String stratumName, double stratumLen,
                                    String stratumAdd, String stratumPro,
                                    String integrity) throws DataAccessException {
        handleMapper.updateStratumInfoById(oldStratumId, stratumId, stratumName,
                stratumLen, stratumAdd, stratumPro, integrity);
    }

    public void deleteImageInfoByImageName(String image_name) throws DataAccessException {
        handleMapper.deleteImageInfoByImageName(image_name);
    }

    public void deleteUserInfoByAccount(String u_account) throws DataAccessException {
        handleMapper.deleteUserInfoByAccount(u_account);
    }

    public void deleteStratumInfoById(String stratum_id) throws DataAccessException {
        handleMapper.deleteStratumInfoById(stratum_id);
    }
    @Transactional
    public List<Map<String, Object>> checkStratumIntegrityGlobally() {
        List<Map<String, Object>> integrityIssues = new ArrayList<>();
        // 1. 查询所有完整性为"NO"的岩柱
        List<Map<String, Object>> allStrata = handleMapper.getAllStrataWithIssues();
        // 2. 遍历所有岩柱，检查其完整性
        for (Map<String, Object> stratum : allStrata) {
            String stratumId = (String) stratum.get("stratum_id");
            //System.out.println(stratumId);
            // 3. 获取每个岩柱的长度和段的总长度
            Double stratumLength = handleMapper.getStratumLength(stratumId);
            Double totalSegmentLength = handleMapper.getTotalSegmentLength(stratumId);
            List<StratumSegmentDTO> segments = handleMapper.getStratumAndSegments(stratumId);

            boolean isValid = stratumLength != null && totalSegmentLength != null &&
                    Math.abs(stratumLength - totalSegmentLength) < 0.001;
            if (isValid) {
                segments.sort((a, b) -> Double.compare(a.getSegStart(), b.getSegStart()));
                int sequenceNo = 1;
                for (StratumSegmentDTO segment : segments) {
                    segment.setSequenceNo(sequenceNo);
                    handleMapper.updateSequenceNo(stratumId, segment.getSegStart(), sequenceNo);
                    sequenceNo++;
                }
            }
            // 更新岩柱完整性
            handleMapper.updateStratumIntegrity(stratumId, isValid ? "YES" : "NO");
        }
        return integrityIssues;
    }
}
