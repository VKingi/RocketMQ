/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.rocketmq.filtersrv;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.common.protocol.CommandUtil;
import com.alibaba.rocketmq.common.protocol.RequestCode;
import com.alibaba.rocketmq.common.protocol.ResponseCode;
import com.alibaba.rocketmq.common.protocol.protobuf.Command.MessageCommand;
import com.alibaba.rocketmq.common.protocol.protobuf.FiltersrvHeader.RegisterFilterServerRequestHeader;
import com.alibaba.rocketmq.common.protocol.protobuf.FiltersrvHeader.RegisterFilterServerResponseHeader;
import com.alibaba.rocketmq.remoting.RemotingClient;
import com.alibaba.rocketmq.remoting.exception.MessageCommandException;
import com.alibaba.rocketmq.remoting.exception.RemotingConnectException;
import com.alibaba.rocketmq.remoting.exception.RemotingSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemotingTimeoutException;
import com.alibaba.rocketmq.remoting.netty.NettyClientConfig;
import com.alibaba.rocketmq.remoting.netty.NettyRemotingClient;


/**
 * Broker对外调用的API封装
 *
 * @author shijia.wxr<vintage.wang@gmail.com>
 * @since 2014-4-10
 */
public class FilterServerOuterAPI {
    private final RemotingClient remotingClient;


    public FilterServerOuterAPI() {
        this.remotingClient = new NettyRemotingClient(new NettyClientConfig());
    }


    public void start() {
        this.remotingClient.start();
    }


    public void shutdown() {
        this.remotingClient.shutdown();
    }


    public RegisterFilterServerResponseHeader registerFilterServerToBroker(//
                                                                           final String brokerAddr,// 1
                                                                           final String filterServerAddr// 2
    ) throws MessageCommandException, RemotingConnectException, RemotingSendRequestException,
            RemotingTimeoutException, InterruptedException, MQBrokerException {
        MessageCommand request = CommandUtil.createRequestBuiler(RequestCode.REGISTER_FILTER_SERVER)
                .setRegisterFilterServerRequestHeader(RegisterFilterServerRequestHeader.newBuilder().setFilterServerAddr(filterServerAddr))
                .build();

        MessageCommand response = this.remotingClient.invokeSync(brokerAddr, request, 3000);
        assert response != null;
        switch (response.getCode()) {
            case ResponseCode.SUCCESS: {
                RegisterFilterServerResponseHeader responseHeader = response.getRegisterFilterServerResponseHeader();

                return responseHeader;
            }
            default:
                break;
        }

        throw new MQBrokerException(response.getCode(), response.getRemark());
    }
}
