package com.example.handle.schedule;

import com.example.handle.dto.resultdata.StratumSegmentDTO;
import com.example.handle.mapper.HandleMapper;
import com.example.handle.model.Stratums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StratumIntegritySchedule {

    @Autowired
    private HandleMapper handleMapper;
    
    @Value("${file_linux.stratum-integrity-result-dir}")
    private String stratumResultDir;
    
    @Value("${file_linux.core-integrity-result-dir}")
    private String coreResultDir;
    
    @Value("${file_linux.valid-core-dir}")
    private String validCoreDir;

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void checkStratumIntegrity(){
        log.info("Start checking stratum integrity");
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            
            createDirectoryIfNotExists(stratumResultDir);
            createDirectoryIfNotExists(coreResultDir);
            createDirectoryIfNotExists(validCoreDir);
            
            String stratumFilePath = stratumResultDir + File.separator + "stratum_statistics_" + timestamp + ".csv";
            String coreFilePath = coreResultDir + File.separator + "core_segments_" + timestamp + ".csv";
            String validCorePath = validCoreDir + File.separator + "valid_core_segments_" + timestamp + ".csv";
            
            log.info("Getting all stratums...");
            List<Stratums> allStratums = handleMapper.getAllStratums().stream()
                .filter(s -> s != null && s.getStratumId() != null)
                .collect(Collectors.toList());
            log.info("Found {} valid stratums", allStratums.size());
            
            // 存储所有完整的地层信息
            Map<String, List<StratumSegmentDTO>> validStratumSegments = new HashMap<>();
            List<StratumSegmentDTO> allResults = new ArrayList<>();
            
            for (Stratums stratum : allStratums) {
                String stratumId = stratum.getStratumId();
                log.info("Processing stratum: {}", stratumId);
                
                try {
                    Double stratumLength = handleMapper.getStratumLength(stratumId);
                    List<StratumSegmentDTO> segments = handleMapper.getStratumAndSegments(stratumId).stream()
                        .filter(segment -> segment != null)
                        .collect(Collectors.toList());
                    
                    if (segments.isEmpty()) {
                        log.warn("No segments found for stratum: {}", stratumId);
                        StratumSegmentDTO emptySegment = StratumSegmentDTO.builder()
                            .stratumId(stratumId)
                            .stratumName(stratum.getStratumName())
                            .stratumLen(stratumLength)
                            .stratumAdd(stratum.getStratumAdd())
                            .build();
                        allResults.add(emptySegment);
                        handleMapper.updateStratumIntegrity(stratumId, "NO");
                        continue;
                    }
                    
                    List<StratumSegmentDTO> validSegments = segments.stream()
                        .filter(segment -> segment.getSegStart() != null && 
                                         segment.getSegEnd() != null && 
                                         segment.getSegLen() != null)
                        .collect(Collectors.toList());
                    
                    double actualTotalLength = validSegments.stream()
                        .mapToDouble(StratumSegmentDTO::getSegLen)
                        .sum();
                    
                    boolean isValid = stratumLength != null && !validSegments.isEmpty() &&
                                    Math.abs(stratumLength - actualTotalLength) < 0.001;
                    
                    if (isValid) {
                        validSegments.sort((a, b) -> Double.compare(
                            Objects.requireNonNull(a.getSegStart()),
                            Objects.requireNonNull(b.getSegStart())
                        ));
                        
                        int sequenceNo = 1;
                        for (StratumSegmentDTO segment : validSegments) {
                            handleMapper.updateSequenceNo(stratumId, segment.getSegStart(), sequenceNo);
                            segment.setSequenceNo(sequenceNo++);
                        }
                        
                        // 保存完整的地层信息
                        validStratumSegments.put(stratumId, validSegments);
                    }
                    
                    allResults.addAll(segments);
                    handleMapper.updateStratumIntegrity(stratumId, isValid ? "YES" : "NO");
                    
                } catch (Exception e) {
                    log.error("Failed to process stratum: {}", stratumId, e);
                    StratumSegmentDTO errorSegment = StratumSegmentDTO.builder()
                        .stratumId(stratumId)
                        .stratumName(stratum.getStratumName())
                        .stratumLen(handleMapper.getStratumLength(stratumId))
                        .stratumAdd(stratum.getStratumAdd())
                        .build();
                    allResults.add(errorSegment);
                    handleMapper.updateStratumIntegrity(stratumId, "NO");
                }
            }
            
            // 写入地层统计文件
            writeStratumStatistics(allResults, allStratums, stratumFilePath);
            // 写入所有岩心段文件
            writeValidCoreSegments(allResults, coreFilePath);
            // 写入已验证完整的岩心段文件
            writeValidatedCoreSegments(validStratumSegments, validCorePath);
            
            log.info("Stratum integrity check completed");
            log.info("Stratum statistics file generated: {}", stratumFilePath);
            log.info("Core segments file generated: {}", coreFilePath);
            log.info("Valid core segments file generated: {}", validCorePath);
        } catch (IOException e) {
            log.error("Failed to execute stratum integrity check", e);
        }
    }
    
    private void writeValidatedCoreSegments(Map<String, List<StratumSegmentDTO>> validStratumSegments, 
                                          String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write('\ufeff');
            writeCsvLine(writer, "StratumID", "StratumName", "ImageName", 
                "StartDepth(m)", "EndDepth(m)", "Length(m)", "SequenceNo");
            
            // 按地层ID排序
            List<String> sortedStratumIds = new ArrayList<>(validStratumSegments.keySet());
            Collections.sort(sortedStratumIds);
            
            for (String stratumId : sortedStratumIds) {
                List<StratumSegmentDTO> segments = validStratumSegments.get(stratumId);
                for (StratumSegmentDTO segment : segments) {
                    writeCsvLine(writer,
                        Objects.toString(segment.getStratumId(), ""),
                        Objects.toString(segment.getStratumName(), ""),
                        Objects.toString(segment.getImageName(), ""),
                        String.format("%.3f", segment.getSegStart()),
                        String.format("%.3f", segment.getSegEnd()),
                        String.format("%.3f", segment.getSegLen()),
                        String.valueOf(segment.getSequenceNo())
                    );
                }
            }
        }
    }
    
    private void writeStratumStatistics(List<StratumSegmentDTO> results, List<Stratums> allStratums, 
                                      String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write('\ufeff');
            writeCsvLine(writer, "StratumID", "StratumName", "ExpectedLength(m)", "ActualLength(m)", "IntegrityStatus");
            
            // 确保所有地层都被写入
            for (Stratums stratum : allStratums) {
                String stratumId = stratum.getStratumId();
                Double stratumLen = handleMapper.getStratumLength(stratumId);
                
                // 查找该地层的所有岩心段
                List<StratumSegmentDTO> segments = results.stream()
                    .filter(s -> s != null && stratumId.equals(s.getStratumId()))
                    .collect(Collectors.toList());
                
                Double totalLength = segments.stream()
                    .filter(s -> s.getSegLen() != null)
                    .mapToDouble(StratumSegmentDTO::getSegLen)
                    .sum();
                
                boolean isValid = stratumLen != null && !segments.isEmpty() &&
                                segments.stream().allMatch(s -> s.getSegLen() != null) &&
                                Math.abs(stratumLen - totalLength) < 0.001;
                
                writeCsvLine(writer,
                    Objects.toString(stratumId, ""),
                    Objects.toString(stratum.getStratumName(), ""),
                    String.format("%.3f", stratumLen != null ? stratumLen : 0.0),
                    String.format("%.3f", totalLength),
                    isValid ? "YES" : "NO"
                );
            }
        }
    }
    
    private void writeValidCoreSegments(List<StratumSegmentDTO> results, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write('\ufeff');
            writeCsvLine(writer, "StratumID", "StratumName", "ImageName", 
                "StartDepth(m)", "EndDepth(m)", "Length(m)", "SequenceNo");
            
            // 只写入有效的岩心段
            for (StratumSegmentDTO segment : results) {
                if (segment != null && segment.getSegStart() != null && 
                    segment.getSegEnd() != null && segment.getSegLen() != null) {
                    writeCsvLine(writer,
                        Objects.toString(segment.getStratumId(), ""),
                        Objects.toString(segment.getStratumName(), ""),
                        Objects.toString(segment.getImageName(), ""),
                        String.format("%.3f", segment.getSegStart()),
                        String.format("%.3f", segment.getSegEnd()),
                        String.format("%.3f", segment.getSegLen()),
                        String.valueOf(segment.getSequenceNo())
                    );
                }
            }
        }
    }
    
    private void writeCsvLine(BufferedWriter writer, String... values) throws IOException {
        for (int i = 0; i < values.length; i++) {
            String value = Objects.toString(values[i], "");
            if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
                value = value.replace("\"", "\"\"");
                writer.write("\"" + value + "\"");
            } else {
                writer.write(value);
            }
            
            if (i < values.length - 1) {
                writer.write(",");
            }
        }
        writer.newLine();
    }
    
    private void createDirectoryIfNotExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
} 