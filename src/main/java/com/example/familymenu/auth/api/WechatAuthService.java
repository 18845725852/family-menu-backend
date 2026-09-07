package com.example.familymenu.auth.api;

import com.example.familymenu.common.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@org.springframework.context.annotation.Profile("!memory")
public class WechatAuthService {
    private final JdbcTemplate jdbc;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${wechat.appid:}") private String appid;
    @Value("${wechat.secret:}") private String secret;

    public WechatLoginResponse login(String code, String nickname) {
        if (appid.trim().isEmpty() || secret.trim().isEmpty()) {
            throw new BusinessException("微信登录尚未配置，请设置 WECHAT_APPID 和 WECHAT_SECRET");
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid
                + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        try {
            JsonNode result = objectMapper.readTree(restTemplate.getForObject(url, String.class));
            if (result.has("errcode")) throw new BusinessException("微信登录失败: " + result.path("errmsg").asText());
            String openid = result.path("openid").asText();
            if (openid.isEmpty()) throw new BusinessException("微信未返回用户标识");
            String name = nickname == null || nickname.trim().isEmpty()
                    ? DefaultNicknameGenerator.generate() : nickname.trim();
            jdbc.update("INSERT INTO users(openid,nickname) VALUES(?,?) ON DUPLICATE KEY UPDATE nickname=IF(nickname='微信用户', VALUES(nickname), nickname)", openid, name);
            Long userId = jdbc.queryForObject("SELECT id FROM users WHERE openid=?", Long.class, openid);
            String savedName = jdbc.queryForObject("SELECT nickname FROM users WHERE id=?", String.class, userId);
            String token = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
            jdbc.update("INSERT INTO user_sessions(user_id, token, expires_at) VALUES(?, ?, ?)",
                    userId, token, java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(30)));
            return new WechatLoginResponse(userId, savedName, token);
        } catch (BusinessException e) { throw e; }
        catch (Exception e) { throw new BusinessException("微信登录服务暂时不可用"); }
    }

    public void logout(Long userId, String token) {
        jdbc.update("DELETE FROM user_sessions WHERE user_id=? AND token=?", userId, token);
    }
}
