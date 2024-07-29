package edu.hunre.course_management.controller.resource;

import edu.hunre.course_management.model.dto.CustomerDTO;
import edu.hunre.course_management.model.dto.LanguageDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import edu.hunre.course_management.service.ILanguageService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/language")
public class LanguageResource {
    @Autowired
    private ILanguageService iLanguageService;
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<Page<LanguageDTO>>> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                  @Min(10) @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(iLanguageService.getLanguageProficiency(page, size));
    }
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<?>> create(@RequestBody LanguageDTO languageDTO) {
        BaseResponse<?> response = iLanguageService.addLanguageLevel(languageDTO);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public  ResponseEntity<BaseResponse<?>> update(@RequestBody LanguageDTO languageDTO, @PathVariable Long id) {
        BaseResponse<?> response = iLanguageService.updateLanguage(id, languageDTO);
        if (response.getCode() == HttpStatus.OK.value()){
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<BaseResponse<?>> getById(@PathVariable Long id) {
        BaseResponse<?> response = iLanguageService.findById(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.status(response.getCode()).body(response);
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        BaseResponse<?> response = iLanguageService.deleteLanguage(id);
        if (response.getCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }
}
