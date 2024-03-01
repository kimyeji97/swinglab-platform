package com.dailystudy.swinglab.service.framework.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SwinglabUserDetail extends User
{
    @Serial
    private static final long serialVersionUID = 2377821761785224090L;

    private com.dailystudy.swinglab.service.business.common.domain.entity.user.User user;

    public SwinglabUserDetail (String username, String password, com.dailystudy.swinglab.service.business.common.domain.entity.user.User user)
    {
        super(username, password, new ArrayList<>(List.of(new SimpleGrantedAuthority(StringUtils.join("ROLE_", "ALL")))));
        this.user = user;
    }

    public com.dailystudy.swinglab.service.business.common.domain.entity.user.User getUser ()
    {
        return this.user;
    }
}
