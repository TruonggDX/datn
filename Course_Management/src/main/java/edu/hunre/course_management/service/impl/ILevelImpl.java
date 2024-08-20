package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.LevelEntity;
import edu.hunre.course_management.mapper.LevelMapper;
import edu.hunre.course_management.model.dto.LevelDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.LevelRepository;
import edu.hunre.course_management.service.ILevelService;
import edu.hunre.course_management.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ILevelImpl implements ILevelService {

    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private LevelMapper levelMapper;

    @Override
    public BaseResponse<List<LevelDTO>> getAllLevels() {
        BaseResponse<List<LevelDTO>> response = new BaseResponse<>();
        List<LevelEntity> levels = levelRepository.findAllLevel();
        List<LevelDTO> levelDTOS = levels.stream()
                .map(levelMapper::toDto)
                .collect(Collectors.toList());
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setData(levelDTOS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }



}
