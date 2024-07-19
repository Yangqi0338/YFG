package com.base.sbc.module.material.service;

import com.base.sbc.client.flowable.entity.AnswerDto;
import com.base.sbc.module.common.service.BaseService;
import com.base.sbc.module.material.dto.MaterialQueryDto;
import com.base.sbc.module.material.dto.MaterialSaveDto;
import com.base.sbc.module.material.entity.Material;
import com.base.sbc.module.material.vo.AssociationMaterialVo;
import com.base.sbc.module.material.vo.MaterialLinkageVo;
import com.base.sbc.module.material.vo.MaterialVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 类描述：素材库 service类
 * @author 卞康
 * @version 1.0
 * @date 创建时间：2023-3-24 16:26:15
 */
public interface MaterialService extends BaseService<Material> {

    /**
     * 条件查询
     *
     * @param materialQueryDto 请求封装对象
     * @return 返回的封装对象
     */
     PageInfo<MaterialVo> listQuery(MaterialQueryDto materialQueryDto);

    boolean toExamine(AnswerDto dto);

    /**
     * 新增
     * @param materialSaveDto materialSaveDto类
     */
    String add(MaterialSaveDto materialSaveDto);

    /**
     * 批量修改素材
     * @param materialSaveDtoList 素材列表
     * @return 影响的条数
     */
    Integer updateList(List<MaterialSaveDto> materialSaveDtoList);

    /**
     * 获取关联素材库的详细信息
     * @param ids 素材id
     * @return
     */
    List<AssociationMaterialVo> getAssociationMaterial(List<String> ids);

    List<MaterialLinkageVo> linkageQuery(String search, String materialCategoryIds,String folderId, String personQuery);

    /**
     * 检查是否有关联的数据，true：有关联
     * @param folderIds
     * @return
     */
    boolean checkFolderRelation(List<String> folderIds);

    long getFileCount(String userId,List<String> folderIds);

    Long getFileSize(String userId,List<String> byAllFileIds);

    void mergeFolderReplace(String id, List<String> byMergeFolderIds);

    List<String> listImgQuery(MaterialQueryDto materialQueryDto);

    void delMaterialPersonSpace(List<String> userIds);

    Map<String, List<String>> listImg(MaterialQueryDto materialQueryDto);
}
