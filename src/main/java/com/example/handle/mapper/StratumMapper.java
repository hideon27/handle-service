package com.example.handle.mapper;

import com.example.handle.dto.resultdata.EngineeringDTO;
import com.example.handle.dto.resultdata.ImageAndStratumDTO;
import com.example.handle.dto.resultdata.StratumDTO;
import com.example.handle.dto.resultdata.StratumSegmentDTO;
import com.example.handle.model.Stratums;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface StratumMapper {
    int insertStratumInfo(@Param("stratumId") String stratumId,
                         @Param("stratumName") String stratumName,
                         @Param("stratumLen") double stratumLen,
                         @Param("stratumAdd") String stratumAdd,
                         @Param("stratumPro") String stratumPro);

    Double getStratumLength(@Param("stratumId") String stratumId);
    Double getTotalSegmentLength(@Param("stratumId") String stratumId);
    List<StratumSegmentDTO> getStratumAndSegments(@Param("stratumId") String stratumId);
    void updateSequenceNo(@Param("stratumId") String stratumId,
                         @Param("segStart") double segStart,
                         @Param("sequenceNo") int sequenceNo);
    void updateStratumIntegrity(@Param("stratumId") String stratumId,
                               @Param("integrity") String integrity);

    List<EngineeringDTO> getEngineeringTeamName();
    List<StratumDTO> getStratumName();
    List<ImageAndStratumDTO> getImageInfoByDynamicParams(Map<String, Object> params);
    List<Stratums> getStratumInfoByName(@Param("stratum_id") String stratum_id);
    List<Stratums> getStratumInfoByDynamicParams(Map<String, Object> params);
    void updateStratumInfoById(@Param("stratumId") String stratumId,
                              @Param("stratumName") String stratumName,
                              @Param("stratumLen") double stratumLen,
                              @Param("stratumAdd") String stratumAdd,
                              @Param("stratumPro") String stratumPro,
                              @Param("integrity") String integrity);
    void deleteStratumInfoById(@Param("stratum_id") String stratum_id);
    List<Map<String, Object>> getAllStrataWithIssues();
    @Select("SELECT * FROM stratums")
    List<Stratums> getAllStratums();   
} 