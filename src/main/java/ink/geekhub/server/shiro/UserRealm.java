package ink.geekhub.server.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ink.geekhub.server.entity.User;
import ink.geekhub.server.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: book-of-geek
 * @description: 用户安全的Realm
 * @author: zerohua
 * @create: 2020-05-18 18:31
 **/
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserMapper userMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userMapper.selectOne(new QueryWrapper<User>(){{
            eq("email", token.getUsername());
        }});
        if(user == null){
            throw new AuthenticationException("sorry，用户不存在！");
        }
        return new SimpleAuthenticationInfo(
                user.getUsername(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                getName()
        );
    }
}
