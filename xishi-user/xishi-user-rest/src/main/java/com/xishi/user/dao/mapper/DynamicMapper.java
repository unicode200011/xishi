package com.xishi.user.dao.mapper;

import com.xishi.user.model.pojo.Dynamic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 动态列表 Mapper 接口
 * </p>
 *
 * @author LX
 * @since 2019-11-14
 */
public interface DynamicMapper extends BaseMapper<Dynamic> {

    void changePraiseNum(@Param("id") Long id,@Param("type") Integer type);

    void changeCommentNum(@Param("id") Long dynamicId,@Param("type") Integer type);
}
