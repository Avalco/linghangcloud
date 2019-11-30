package com.linghangcloud.android.TaskDetail;

import com.linghangcloud.android.GSON.Commit;
import java.util.*;

public class comunity {
    private List<Commit> list;
    public comunity(List<Commit> list) {
        this.list=list;
    }
    public List<Commit> getList() {
        return list;
    }

    public void setList(List<Commit> list) {
        this.list = list;
    }
}
