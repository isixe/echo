package dev.itea.echo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 问答评论表
 *
 * @author isixe
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("comment_question")
@Schema(description = "问答评论表")
public class CommentQuestion extends Model<CommentQuestion> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "问答ID")
    @TableField("question_id")
    private Integer questionId;

    @Schema(description = "顶层评论ID")
    @TableField("root_comment_id")
    private Integer rootCommentId;

    @Schema(description = "关联父评论ID")
    @TableField("parent_comment_id")
    private Integer parentCommentId;

    @Schema(description = "评论内容")
    @TableField("content")
    private String content;

    @Schema(description = "支持数")
    @TableField("like_count")
    private Integer likeCount;

    @Schema(description = "否定数")
    @TableField("dislike_count")
    private Integer dislikeCount;

    @Schema(description = "发送时间")
    @TableField("created_time")
    private LocalDateTime createdTime;

    @Schema(description = "是否删除")
    @TableField("is_deleted")
    @TableLogic
    @JsonIgnore
    private Byte isDeleted;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}