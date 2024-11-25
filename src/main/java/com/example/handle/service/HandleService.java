package com.example.handle.service;

import com.example.handle.dto.resultdata.Engineering_1;
import com.example.handle.dto.resultdata.ImageAndStratum;
import com.example.handle.dto.resultdata.Stratums_1;
import com.example.handle.mapper.HandleMapper;
import com.example.handle.model.Image_info;
import com.example.handle.model.Users;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class HandleService {

    @Autowired(required = false)
    private HandleMapper handleMapper;

    ////select
    //查找工程队
    public List<Engineering_1> getEngineeringTeamName(){
        return handleMapper.getEngineeringTeamName();
    }
    //从岩芯表获得岩芯信息
    public List<Stratums_1> getStratumName(){
        return handleMapper.getStratumName();
    }
    //通过姓名获取用户信息
    public Users getUserInfoByAccount(String name)throws DataAccessException{
        return handleMapper.getUserInfoByAccount(name);
    }
    //获得图片信息
    public List<Image_info> getImageInfoByName(String image_name)throws DataAccessException{
        return handleMapper.getImageInfoByName(image_name);
    }
    public List<Users> getUserInfoByName(String account)throws DataAccessException{
        return handleMapper.getUserInfoByName(account);
    }
    //通过账户和工号查询用户信息
    public List<Users> getUserInfoByAccNum(String account,String num)throws  DataAccessException{
        return handleMapper.getUserInfoByAccNum(account,num);
    }
    //通过姓名和密码获得匹配结果，1为通过，0为未通过
    public int getResultByNamePassword(String u_account,String u_password){
        return handleMapper.getResultByNamePassword(u_account,u_password);
    }
    public int getResultByNamePasswordAdmin(String a_account,String a_password){
        return handleMapper.getResultByNamePasswordAdmin(a_account,a_password);
    }
    //通过账户号查找工号
    public String getIdByAccount(String u_account){
        return handleMapper.getIdByAccount(u_account);
    }
    //通过输入参数情况进行动态查询
    public List<ImageAndStratum> getImageInfoByDynamicParams(Map<String, Object> params)throws DataAccessException{
        return handleMapper.getImageInfoByDynamicParams(params);
    }

    public List<Users> getUserInfoByDynamicParams(Map<String, Object> params) throws DataAccessException {
        return handleMapper.getUserInfoByDynamicParams(params);
    }

    ////insert
    //用户信息插入
    public int insertUser(String u_account,String u_num, String u_password,String u_name,
                          String u_sex,String u_id,String u_email, String u_tel,String u_et_name) throws DataAccessException{
        return handleMapper.insertUser(u_account,u_num,u_password,u_name,u_sex,u_id,u_email,u_tel,u_et_name);
    }
    //图片信息插入
    public int insertImageInfo(String image_name,String image_path,String image_num,String image_sid,
                        double ima_start,double ima_end,double ima_depth,String s_type){
        return handleMapper.insertImageInfo(image_name,image_path,image_num,image_sid,ima_start,ima_end,ima_depth,s_type);
    }

    ////update
    //通过名字更新图片信息
    public void updateImageInfoByName(String image_name1,String image_sid,double ima_start,
                               double ima_end,double ima_depth,String s_type,String image_name2){
        handleMapper.updateImageInfoByName(image_name1,image_sid,ima_start,ima_end,ima_depth,s_type,image_name2);
    }
    public void updateUserInfoByAccount( String u_oldAccount,String u_account,String u_num,String u_password,String u_name, String u_sex, String u_id, String u_email, String u_tel, String u_et_name){
        handleMapper.updateUserInfoByAccount(u_oldAccount,u_account,u_num,u_password,u_name,u_sex,u_id,u_email,u_tel,u_et_name);
    }


    ////delete
    //通过图片名删除图片信息
    public void deleteImageInfoByImageName(String image_name){
        handleMapper.deleteImageInfoByImageName(image_name);
    }

    public void deleteUserInfoByAccount(String u_account){
        handleMapper.deleteUserInfoByAccount(u_account);
    }

}
