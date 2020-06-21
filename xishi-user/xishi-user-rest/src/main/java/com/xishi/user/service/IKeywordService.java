package com.xishi.user.service;

import com.xishi.user.model.pojo.Keyword;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LX
 * @since 2020-02-19
 */
public interface IKeywordService extends IService<Keyword> {

    String replaceWord(String content);
}
