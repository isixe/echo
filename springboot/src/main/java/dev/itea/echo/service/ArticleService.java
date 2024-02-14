package dev.itea.echo.service;

import dev.itea.echo.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.UserRankVO;

import java.util.List;

/**
 * 文章表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface ArticleService extends IService<Article> {
    List<UserRankVO> getUserArticleNumRankList();
}
