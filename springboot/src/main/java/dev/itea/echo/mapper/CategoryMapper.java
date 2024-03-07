package dev.itea.echo.mapper;

import dev.itea.echo.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 类别表 Mapper 接口
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> getCategotyListByName(String categoryName);
}
