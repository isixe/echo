package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.CommentArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.CommentArticleVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文章评论表 Mapper 接口
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CommentArticleMapper extends BaseMapper<CommentArticle> {

    IPage<CommentArticleVO> getArticleCommentByPage(Page<CommentArticleVO> page,  @Param(Constants.WRAPPER) QueryWrapper<CommentArticleVO> wrapper);
}
