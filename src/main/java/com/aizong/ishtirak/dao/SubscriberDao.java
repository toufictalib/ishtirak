package com.aizong.ishtirak.dao;

import java.util.List;

public interface SubscriberDao extends GenericDao<Object> {

    void deleteContents(List<Long> ids);

    void deleteEngines(List<Long> ids);

    void deleteBundles(List<Long> ids);
}
