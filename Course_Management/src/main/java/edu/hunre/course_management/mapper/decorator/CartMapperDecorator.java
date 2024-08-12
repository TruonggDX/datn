package edu.hunre.course_management.mapper.decorator;

import edu.hunre.course_management.entity.CartEntity;
import edu.hunre.course_management.mapper.CartMapper;
import edu.hunre.course_management.mapper.ImageSourseMapper;
import edu.hunre.course_management.model.dto.CartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class CartMapperDecorator implements CartMapper {
    @Autowired
    private CartMapper delegate;

    @Autowired
    private ImageSourseMapper imageSourseMapper;



    @Override
    public CartDTO toDto(CartEntity cartEntity) {
        CartDTO cartDTO = delegate.toDto(cartEntity);
        if (cartEntity.getCourseEntity() != null && cartEntity.getCourseEntity().getImageEntityList() != null) {
            cartDTO.setImageFile(imageSourseMapper.getImageFile(cartEntity));
            cartDTO.setImageId(imageSourseMapper.getImageId(cartEntity));
        }
        return cartDTO;
    }

    @Override
    public CartEntity toEntity(CartDTO cartDTO) {
        return delegate.toEntity(cartDTO);
    }
}
