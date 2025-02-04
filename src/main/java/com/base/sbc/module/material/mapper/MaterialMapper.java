package com.base.sbc.module.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.vo.MaterialChildren;
import com.base.sbc.module.material.vo.MaterialVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 19:07:58
 */
@Mapper
public interface MaterialMapper extends BaseMapper<Material> {
    /**
     * 多条件关联查询
     * @param materialQueryDto materialDto对象
     * @return MaterialAllDto集合
     */
    List<MaterialVo> listQuery(MaterialQueryDto materialQueryDto);

    List<MaterialChildren> linkageQueryName(@Param("search") String search, @Param("materialCategoryIds") List<String> materialCategoryIds,
                                            @Param("folderId") String folderId,
                                            @Param("status")String status, @Param("createId")String createId);

    Long getFileSize(@Param("userId") String userId ,@Param("folderIds") List<String> folderIds);


    List<MaterialChildren> linkageDescInfo(@Param("search") String search, @Param("materialCategoryIds") List<String> materialCategoryIds,
                                            @Param("folderId") String folderId,
                                            @Param("status")String status, @Param("createId")String createId);
}
