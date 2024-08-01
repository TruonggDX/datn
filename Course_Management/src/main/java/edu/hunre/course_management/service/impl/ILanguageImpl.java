package edu.hunre.course_management.service.impl;

import edu.hunre.course_management.entity.CategoryEntity;
import edu.hunre.course_management.entity.LanguageEntity;
import edu.hunre.course_management.mapper.LanguageProficiencyMapper;
import edu.hunre.course_management.model.dto.LanguageDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.repository.LanguageRepository;
import edu.hunre.course_management.service.ILanguageService;
import edu.hunre.course_management.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ILanguageImpl implements ILanguageService {
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private LanguageProficiencyMapper languageProficiencyMapper;

    @Override
    public BaseResponse<Page<LanguageDTO>> getLanguageProficiency(int page, int size) {
       Pageable pageable = PageRequest.of(page, size);
       Page<LanguageEntity> proficiencyEntities = languageRepository.findAllLanguageLevel(pageable);
       List<LanguageDTO> proficiencyList = proficiencyEntities.getContent().stream().map(languageProficiencyMapper::toDto).collect(Collectors.toList());
        BaseResponse<Page<LanguageDTO>> response = new BaseResponse<>();
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(new PageImpl<>(proficiencyList, pageable, proficiencyEntities.getTotalElements()));
        return response;
    }

    @Override
    public BaseResponse<?> addLanguageLevel(LanguageDTO languageProficiencyDTO) {
        BaseResponse<LanguageDTO> response = new BaseResponse<>();
        LanguageEntity languageEntity = languageProficiencyMapper.toEntity(languageProficiencyDTO);
        languageEntity.setDeleted(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        languageEntity.setCreatedBy(authentication.getName());
        languageEntity.setCreatedDate(LocalDateTime.now());
        languageEntity = languageRepository.save(languageEntity);
        languageProficiencyDTO = languageProficiencyMapper.toDto(languageEntity);
        response.setData(languageProficiencyDTO);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());

        return response;
    }

    @Override
    public BaseResponse<?> deleteLanguage(Long id) {
        BaseResponse<LanguageDTO> response = new BaseResponse<>();
        Optional<LanguageEntity> languageEntityOptional = languageRepository.findById(id);
        if (languageEntityOptional.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
        }
        LanguageEntity languageEntity = languageEntityOptional.get();
        languageEntity.setDeleted(true);
        languageEntity.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        languageEntity.setModifiedBy(authentication.getName());
        LanguageDTO languageDTO = languageProficiencyMapper.toDto(languageEntity);
        languageRepository.save(languageEntity);

        response.setData(languageDTO);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Override
    public BaseResponse<?> findById(Long id) {
        BaseResponse<LanguageDTO> response = new BaseResponse<>();
        Optional<LanguageEntity> languageEntityOptional = languageRepository.findById(id);
        if (languageEntityOptional.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        LanguageEntity languageEntity = languageEntityOptional.get();
        if (languageEntity.getDeleted()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        LanguageDTO languageDTO = languageProficiencyMapper.toDto(languageEntityOptional.get());
        response.setData(languageDTO);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Override
    public BaseResponse<?> updateLanguage(Long id, LanguageDTO languageProficiencyDTO) {
        BaseResponse<LanguageDTO> response = new BaseResponse<>();
        Optional<LanguageEntity> languageEntityOptional = languageRepository.findById(id);
        if (languageEntityOptional.isEmpty()){
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            return response;
        }
        LanguageEntity languageEntity = languageEntityOptional.get();
        languageEntity = languageProficiencyMapper.toEntity(languageProficiencyDTO);
        languageEntity.setDeleted(false);
        languageEntity.setModifiedDate(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        languageEntity.setModifiedBy(authentication.getName());

        languageRepository.save(languageEntity);
        LanguageDTO languageDTO = languageProficiencyMapper.toDto(languageEntity);
        response.setData(languageDTO);
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());

        return response;
    }

    @Override
    public BaseResponse<List<LanguageDTO>> findByCondition(String condition) {
        BaseResponse<List<LanguageDTO>> response = new BaseResponse<>();
        List<LanguageEntity> languageEntities = languageRepository.findLanguageByCondition(condition);

        if (languageEntities == null || languageEntities.isEmpty()) {
            response.setMessage(Constant.HTTP_MESSAGE.FAILED);
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setData(new ArrayList<>());
        }
        List<LanguageDTO> languageDTOS = new ArrayList<>();
        for (LanguageEntity languageEntity : languageEntities) {
            LanguageDTO languageDTO = languageProficiencyMapper.toDto(languageEntity);
            languageDTOS.add(languageDTO);
        }
        response.setMessage(Constant.HTTP_MESSAGE.SUCCESS);
        response.setCode(HttpStatus.OK.value());
        response.setData(languageDTOS);
        return response;
    }

}
