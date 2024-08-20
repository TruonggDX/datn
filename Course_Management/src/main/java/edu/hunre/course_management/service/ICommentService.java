package edu.hunre.course_management.service;

import edu.hunre.course_management.entity.CommentEntity;
import edu.hunre.course_management.model.dto.CommentDTO;
import edu.hunre.course_management.model.dto.RatingDTO;
import edu.hunre.course_management.model.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface ICommentService {
    BaseResponse<Page<CommentDTO>> getAll(CommentDTO commentDTO, int page, int size);
    BaseResponse<?> addComment(CommentDTO commentDTO);
    BaseResponse<?> updateComment(Long id,CommentDTO commentDTO);
    BaseResponse<?> deleteComment(Long id);
    BaseResponse<?> findCommentById(Long id);

    BaseResponse<Long> countComment(Long accountId);
}
