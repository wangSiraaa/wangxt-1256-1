package com.fatigue.risk.controller;

import com.fatigue.risk.common.Result;
import com.fatigue.risk.config.FatigueProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/common")
public class CommonController {

    private final FatigueProperties fatigueProperties;

    public CommonController(FatigueProperties fatigueProperties) {
        this.fatigueProperties = fatigueProperties;
    }

    @GetMapping("/threshold")
    public Result<Map<String, Object>> getThreshold() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("maxConsecutiveOrders", fatigueProperties.getMaxConsecutiveOrders());
        map.put("maxOnlineHours", fatigueProperties.getMaxOnlineHours());
        map.put("consecutiveHoursThreshold", fatigueProperties.getConsecutiveHoursThreshold());
        map.put("requireRestMinutes", fatigueProperties.getRequireRestMinutes());
        return Result.success(map);
    }

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }
        String newName = UUID.randomUUID().toString().replace("-", "") + ext;
        Path filePath = dirPath.resolve(newName);
        file.transferTo(filePath.toFile());
        Map<String, String> result = new HashMap<>();
        result.put("url", "/uploads/" + newName);
        result.put("name", newName);
        result.put("originalName", original);
        return Result.success(result);
    }
}
