package com.example.handle.service;

import com.example.handle.dto.resultdata.EngineeringDTO;
import com.example.handle.dto.resultdata.ImageAndStratumDTO;
import com.example.handle.dto.resultdata.StratumDTO;
import com.example.handle.mapper.HandleMapper;
import com.example.handle.model.CoreSegments;
import com.example.handle.model.Users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HandleService {

    @Autowired(required = false)
    private HandleMapper handleMapper;

    /// /select
    //查找工程队
    public List<EngineeringDTO> getEngineeringTeamName() {
        return handleMapper.getEngineeringTeamName();
    }

    //从岩芯表获得岩芯信息
    public List<StratumDTO> getStratumName() {
        return handleMapper.getStratumName();
    }

    //通过姓名获取用户信息
    public Users getUserInfoByAccount(String name) throws DataAccessException {
        return handleMapper.getUserInfoByAccount(name);
    }

    //获得图片信息
    public List<CoreSegments> getImageInfoByName(String image_name) throws DataAccessException {
        return handleMapper.getImageInfoByName(image_name);
    }

    public List<Users> getUserInfoByName(String account) throws DataAccessException {
        return handleMapper.getUserInfoByName(account);
    }

    //通过账户和工号查询用户信息
    public List<Users> getUserInfoByAccNum(String account, String num) throws DataAccessException {
        return handleMapper.getUserInfoByAccNum(account, num);
    }

    //通过姓名和密码获得匹配结果，1为通过，0为未通过
    public int getResultByNamePassword(String u_account, String u_password) {
        int count = handleMapper.getResultByNamePassword(u_account, u_password);
        return count > 0 ? 1 : 0;
    }

    public int getResultByNamePasswordAdmin(String a_account, String a_password) {
        int count = handleMapper.getResultByNamePasswordAdmin(a_account, a_password);
        return count > 0 ? 1 : 0;
    }

    //通过账户号查找工号
    public String getIdByAccount(String u_account) {
        return handleMapper.getIdByAccount(u_account);
    }

    //通过输入参数情况进行动态查询
    public List<ImageAndStratumDTO> getImageInfoByDynamicParams(Map<String, Object> params) throws DataAccessException {
        return handleMapper.getImageInfoByDynamicParams(params);
    }

    public List<Users> getUserInfoByDynamicParams(Map<String, Object> params) throws DataAccessException {
        return handleMapper.getUserInfoByDynamicParams(params);
    }

    /// /insert
    //用户信息插入
    public int insertUser(String u_account, String u_num, String u_password, String u_name,
                          String u_sex, String u_id, String u_email, String u_tel, String u_et_name) throws DataAccessException {
        return handleMapper.insertUser(u_account, u_num, u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name);
    }

    //图片信息插入
    public int insertImageInfo(String image_id, String image_name, String image_path, String uploader_num,
                               String stratum_id, double stratum_len, double seg_start, double seg_end,
                               double seg_len, String seg_type, String sequence_no) {
        return handleMapper.insertImageInfo(image_id, image_name, image_path, uploader_num,
                stratum_id, stratum_len, seg_start, seg_end,
                seg_len, seg_type, sequence_no);
    }

    /// /update
    //通过名字更新图片信息
    public void updateImageInfoByName(String imageName, String stratumId, double segStart,
                                      double segEnd, double segLen, String segType, String oldImageName) {
        handleMapper.updateImageInfoByName(imageName, stratumId, segStart, segEnd, segLen, segType, oldImageName);
    }

    public void updateUserInfoByAccount(String u_oldAccount, String u_account, String u_num, String u_password, String u_name, String u_sex, String u_id, String u_email, String u_tel, String u_et_name) {
        handleMapper.updateUserInfoByAccount(u_oldAccount, u_account, u_num, u_password, u_name, u_sex, u_id, u_email, u_tel, u_et_name);
    }

    //更新用户最后登录时间
    public void updateLastLoginTime(String uAccount) {
        handleMapper.updateLastLoginTime(uAccount);
    }

    //更新管理员最后登录时间
    public void updateAdminLastLoginTime(String aAccount) {
        handleMapper.updateAdminLastLoginTime(aAccount);
    }

    /// /delete
    //通过图片名删除图片信息
    public void deleteImageInfoByImageName(String image_name) {
        handleMapper.deleteImageInfoByImageName(image_name);
    }

    public void deleteUserInfoByAccount(String u_account) {
        handleMapper.deleteUserInfoByAccount(u_account);
    }

    public int insertStratum(String stratumId, String stratumName, double stratumLen, String stratumAdd, String stratumPro) {
        return handleMapper.insertStratum(stratumId, stratumName, stratumLen, stratumAdd, stratumPro);

    }

    /**
     * 校验岩芯段总长度是否等于岩柱长度，并返回岩柱及岩芯段信息
     *
     * @param stratumId 岩柱ID
     * @return 查询结果及校验结果
     */
    public Map<String, Object> validateStratumIntegrity(String stratumId) {
        // 获取岩柱长度
        Double stratumLength = handleMapper.getStratumLength(stratumId);
        // 获取岩芯段总长度
        Double totalSegmentLength = handleMapper.getTotalSegmentLength(stratumId);
        // 获取岩柱及岩芯段信息
        List<Map<String, Object>> stratumAndSegments = handleMapper.getStratumAndSegments(stratumId);

        // 校验岩芯段长度之和是否等于岩柱长度
        boolean isValid = stratumLength != null && totalSegmentLength != null && stratumLength.equals(totalSegmentLength);

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", isValid);
        result.put("stratumInfo", stratumAndSegments);

        return result;
    }
    }


