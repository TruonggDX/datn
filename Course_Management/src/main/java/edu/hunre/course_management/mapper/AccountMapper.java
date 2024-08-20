package edu.hunre.course_management.mapper;

import edu.hunre.course_management.entity.AccountEntity;
import edu.hunre.course_management.model.request.AccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "id",target = "idAccount")
    @Mapping(source = "imageEntity.file",target = "image")
    AccountRequest toRequset(AccountEntity accountEntity);
    AccountEntity toEntity(AccountRequest accountRequest);
    default String map(byte[] value) {
        return value != null ? Base64.getEncoder().encodeToString(value) : null;
    }

}
