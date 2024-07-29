package edu.hunre.course_management.service;

import edu.hunre.course_management.model.dto.LanguageDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface ILanguageService {
    BaseResponse<Page<LanguageDTO>> getLanguageProficiency(int page, int size);
    BaseResponse<?> addLanguageLevel(LanguageDTO languageProficiencyDTO);
    BaseResponse<?> deleteLanguage(Long id);
    BaseResponse<?> findById(Long id);
    BaseResponse<?> updateLanguage(Long id, LanguageDTO languageProficiencyDTO);


}
