package com.aizong.ishtirak.dao;

import java.util.List;

public interface SubscriberDao extends GenericDao<Object> {

    void deleteContents(List<Long> ids);
}
