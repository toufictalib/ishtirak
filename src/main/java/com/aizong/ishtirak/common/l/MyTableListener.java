package com.aizong.ishtirak.common.l;

import java.awt.Window;

public interface MyTableListener {

    void add(Window owner, RefreshTableInterface refreshTableInterface);

    void view(Window owner, Long id);

    void edit(Window owner, Long id, RefreshTableInterface refreshTableInterface);

    void delete(Window owner, Long id, RefreshTableInterface refreshTableInterface);

    
}
