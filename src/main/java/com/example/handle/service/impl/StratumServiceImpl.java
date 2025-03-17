package com.example.handle.service.impl;

import com.example.handle.dto.resultdata.EngineeringDTO;
import com.example.handle.dto.resultdata.ImageAndStratumDTO;
import com.example.handle.dto.resultdata.StratumDTO;
import com.example.handle.dto.resultdata.StratumSegmentDTO;
import com.example.handle.mapper.StratumMapper;
import com.example.handle.model.Stratums;
import com.example.handle.service.StratumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class StratumServiceImpl implements StratumService {
    @Autowired
    private StratumMapper stratumMapper;

    @Override
    public int insertStratumInfo(String stratumId, String stratumName, double stratumLen,
                               String stratumAdd, String stratumPro) {
        return stratumMapper.insertStratumInfo(stratumId, stratumName, stratumLen, stratumAdd, stratumPro);
    }

    @Override
    public Map<String, Object> validateStratumIntegrity(String stratumId) {
        Double stratumLength = stratumMapper.getStratumLength(stratumId);
        Double totalSegmentLength = stratumMapper.getTotalSegmentLength(stratumId);
        List<StratumSegmentDTO> segments = stratumMapper.getStratumAndSegments(stratumId);
        
        boolean isValid = stratumLength != null && totalSegmentLength != null && 
                         Math.abs(stratumLength - totalSegmentLength) < 0.001;
        
        if (isValid) {
            segments.sort((a, b) -> Double.compare(a.getSegStart(), b.getSegStart()));
            int sequenceNo = 1;
            for (StratumSegmentDTO segment : segments) {
                segment.setSequenceNo(sequenceNo);
                stratumMapper.updateSequenceNo(stratumId, segment.getSegStart(), sequenceNo);
                sequenceNo++;
            }
        }

        stratumMapper.updateStratumIntegrity(stratumId, isValid ? "YES" : "NO");

        Map<String, Object> result = new HashMap<>();
        result.put("isValid", isValid);
        result.put("stratumInfo", segments);
        return result;
    }

    @Override
    public List<EngineeringDTO> getEngineeringTeamName() {
        return stratumMapper.getEngineeringTeamName();
    }

    @Override
    public List<StratumDTO> getStratumName() {
        return stratumMapper.getStratumName();
    }

    @Override
    public List<ImageAndStratumDTO> getImageInfoByDynamicParams(Map<String, Object> params) {
        return stratumMapper.getImageInfoByDynamicParams(params);
    }

    @Override
    public List<Stratums> getStratumInfoByName(String stratum_id) {
        return stratumMapper.getStratumInfoByName(stratum_id);
    }

    @Override
    public List<Stratums> getStratumInfoByDynamicParams(Map<String, Object> params) {
        return stratumMapper.getStratumInfoByDynamicParams(params);
    }

    @Override
    public void updateStratumInfoById(String stratumId, String stratumName,
                                    double stratumLen, String stratumAdd,
                                    String stratumPro, String integrity) {
        stratumMapper.updateStratumInfoById(stratumId, stratumName,
                stratumLen, stratumAdd, stratumPro, integrity);
    }

    @Override
    public void deleteStratumInfoById(String stratum_id) {
        stratumMapper.deleteStratumInfoById(stratum_id);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> checkStratumIntegrityGlobally() {
        List<Map<String, Object>> integrityIssues = new ArrayList<>();
        List<Map<String, Object>> allStrata = stratumMapper.getAllStrataWithIssues();
        
        for (Map<String, Object> stratum : allStrata) {
            String stratumId = (String) stratum.get("stratum_id");
            Double stratumLength = stratumMapper.getStratumLength(stratumId);
            Double totalSegmentLength = stratumMapper.getTotalSegmentLength(stratumId);
            List<StratumSegmentDTO> segments = stratumMapper.getStratumAndSegments(stratumId);

            boolean isValid = stratumLength != null && totalSegmentLength != null &&
                    Math.abs(stratumLength - totalSegmentLength) < 0.001;
                    
            if (isValid) {
                segments.sort((a, b) -> Double.compare(a.getSegStart(), b.getSegStart()));
                int sequenceNo = 1;
                for (StratumSegmentDTO segment : segments) {
                    segment.setSequenceNo(sequenceNo);
                    stratumMapper.updateSequenceNo(stratumId, segment.getSegStart(), sequenceNo);
                    sequenceNo++;
                }
            }
            stratumMapper.updateStratumIntegrity(stratumId, isValid ? "YES" : "NO");
        }
        return integrityIssues;
    }
} 