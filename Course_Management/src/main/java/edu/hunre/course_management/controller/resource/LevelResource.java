package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.LevelDTO;
import edu.hunre.course_management.model.dto.RoleDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.ILevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/level")
public class LevelResource {
    @Autowired
    private ILevelService levelService;
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<LevelDTO>>> getAll() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }
}
