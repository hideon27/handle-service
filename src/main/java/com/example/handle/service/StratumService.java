package com.example.handle.service;

import com.example.handle.dto.resultdata.EngineeringDTO;
import com.example.handle.dto.resultdata.ImageAndStratumDTO;
import com.example.handle.dto.resultdata.StratumDTO;
import com.example.handle.model.Stratums;
import java.util.List;
import java.util.Map;

public interface StratumService {
    int insertStratumInfo(String stratumId, String stratumName, double stratumLen, String stratumAdd, String stratumPro);
    Map<String, Object> validateStratumIntegrity(String stratumId);
    List<EngineeringDTO> getEngineeringTeamName();
    List<StratumDTO> getStratumName();
    List<ImageAndStratumDTO> getImageInfoByDynamicParams(Map<String, Object> params);
    List<Stratums> getStratumInfoByName(String stratum_id);
    List<Stratums> getStratumInfoByDynamicParams(Map<String, Object> params);
    void updateStratumInfoById(String stratumId, String stratumName, double stratumLen, String stratumAdd, String stratumPro, String integrity);
    void deleteStratumInfoById(String stratum_id);
    List<Map<String, Object>> checkStratumIntegrityGlobally();
} 