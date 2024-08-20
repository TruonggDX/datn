package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.LevelDTO;
import edu.hunre.course_management.model.response.BaseResponse;

import java.util.List;

public interface ILevelService {
    BaseResponse<List<LevelDTO>> getAllLevels();
}
