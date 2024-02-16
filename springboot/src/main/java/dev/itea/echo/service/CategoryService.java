package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 类别表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CategoryService extends IService<Category> {
    IPage<Category> getCategoryByPage(Pageable pageable, String keyword);
}
