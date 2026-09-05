package com.example.familymenu.auth.api;

import com.example.familymenu.common.exception.BusinessException;

public final class CurrentUser {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<Long>();
    private CurrentUser() {}
    public static void set(Long userId) { USER_ID.set(userId); }
    public static Long get() { return USER_ID.get(); }
    public static Long requireId() {
        Long userId = get();
        if (userId == null) throw new BusinessException("请先登录");
        return userId;
    }
    public static void clear() { USER_ID.remove(); }
}
