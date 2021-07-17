package com.academicresearchplatformbackend.security;

import com.academicresearchplatformbackend.MO.MiddleResult;
import com.academicresearchplatformbackend.dao.Menu;
import com.academicresearchplatformbackend.dao.Role;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.UserService;
import com.academicresearchplatformbackend.utils.MyUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("authorizer")


public class RbacRealm extends AuthorizingRealm {
    private UserService userService;
    private MyUtils myUtils;

    @Autowired
    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    @Autowired
    public void setMyUtils(MyUtils myUtils) {
        this.myUtils = myUtils;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        MiddleResult<User> mr= myUtils.getCurrentAuthenticatedUser();
        if (mr.isSuccess()) {
            User user = mr.getData();
            List<Role> roles = userService.getRolesByUserId(user.getId());
            roles.forEach(i -> {
                s.addRole(i.getName());
                List<Menu> menus = i.getMenus();
                menus.forEach(p-> s.addStringPermission(p.getName()));
            });
        }

        return s;

    }



    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = authenticationToken.getPrincipal().toString();
        User user = userService.findByUsername(username);
        if (user != null) {
            String password = user.getPassword();
            String salt = user.getSalt();
            SimpleAuthenticationInfo s = new SimpleAuthenticationInfo(username, password, ByteSource.Util.bytes(salt), getName());
            return s;
        } else {
            throw new AuthenticationException();
        }
    }
}
