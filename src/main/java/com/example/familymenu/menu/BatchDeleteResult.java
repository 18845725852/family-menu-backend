package com.example.familymenu.menu;

import java.util.List;

public class BatchDeleteResult {
    private final int deleted;
    private final List<String> blocked;

    public BatchDeleteResult(int deleted, List<String> blocked) {
        this.deleted = deleted;
        this.blocked = blocked;
    }

    public int getDeleted() { return deleted; }
    public List<String> getBlocked() { return blocked; }
}
