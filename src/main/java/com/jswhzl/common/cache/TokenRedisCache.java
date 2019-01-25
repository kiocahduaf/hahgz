/**
 *
 */
package com.jswhzl.common.cache;

import com.jswhzl.common.base.BaseRedisCache;
import com.jswhzl.common.bean.Token;
import org.springframework.stereotype.Component;

/**
 * @author Jinjichao
 */
@Component
public class TokenRedisCache extends BaseRedisCache<Token> {

    /**
     *
     */
    public TokenRedisCache() {
        this.prefix = Token.OBJECT_KEY;
    }


}
