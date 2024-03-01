package com.dailystudy.swinglab.service.framework.utils;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

public class SwinglabPasswordEncoder implements PasswordEncoder
{
    @Override
    public String encode(CharSequence rawPassword)
    {
        return Hashing.sha256().hashString(rawPassword, StandardCharsets.UTF_8).toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword)
    {
        return StringUtils.equals(encode(rawPassword), encodedPassword);
    }
}
