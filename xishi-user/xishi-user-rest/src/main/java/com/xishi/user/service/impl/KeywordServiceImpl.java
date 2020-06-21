package com.xishi.user.service.impl;

import com.common.base.util.StrKit;
import com.xishi.user.model.pojo.Keyword;
import com.xishi.user.dao.mapper.KeywordMapper;
import com.xishi.user.service.IKeywordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LX
 * @since 2020-02-19
 */
@Service
public class KeywordServiceImpl extends ServiceImpl<KeywordMapper, Keyword> implements IKeywordService {

    @Override
    public String replaceWord(String content) {
        List<Keyword> list = this.list();
        for (Keyword keyword : list) {
            String word = keyword.getWord();
            boolean contains = content.contains(word);
            if (contains){
                content = content.replaceAll(word,"*");
            }
        }

        return content;
    }

}
