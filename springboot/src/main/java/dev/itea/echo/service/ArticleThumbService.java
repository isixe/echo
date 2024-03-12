package dev.itea.echo.service;

import dev.itea.echo.entity.ArticleThumb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author isixe
 * @since 2024-02-27
 */
public interface ArticleThumbService extends IService<ArticleThumb> {

    ArticleThumb get(Integer id);

    void delete(Integer id);
}
