package com.alibaba.rocketmq.common.protocol.body;

import com.alibaba.rocketmq.common.DataVersion;
import com.alibaba.rocketmq.common.TopicConfig;
import com.alibaba.rocketmq.common.protocol.RemotingSerializable;

import java.util.concurrent.ConcurrentHashMap;


public class TopicConfigSerializeWrapper extends RemotingSerializable {
    private ConcurrentHashMap<String, TopicConfig> topicConfigTable =
            new ConcurrentHashMap<String, TopicConfig>();
    private DataVersion dataVersion = new DataVersion();


    public ConcurrentHashMap<String, TopicConfig> getTopicConfigTable() {
        return topicConfigTable;
    }


    public void setTopicConfigTable(ConcurrentHashMap<String, TopicConfig> topicConfigTable) {
        this.topicConfigTable = topicConfigTable;
    }


    public DataVersion getDataVersion() {
        return dataVersion;
    }


    public void setDataVersion(DataVersion dataVersion) {
        this.dataVersion = dataVersion;
    }
}
