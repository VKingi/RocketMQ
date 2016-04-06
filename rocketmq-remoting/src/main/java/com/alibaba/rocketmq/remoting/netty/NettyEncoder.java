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
package com.alibaba.rocketmq.remoting.netty;

import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.alibaba.rocketmq.remoting.common.RemotingUtil;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author shijia.wxr<vintage.wang@gmail.com>
 * @since 2013-7-13
 */
public class NettyEncoder extends MessageToByteEncoder<MessageLiteOrBuilder> {
    private static final Logger log = LoggerFactory.getLogger(RemotingHelper.RemotingLogName);

    @Override
    public void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, ByteBuf out)
            throws Exception {
        try {
            byte[] body = null;
            if (msg instanceof MessageLite) {
                body = ((MessageLite) msg).toByteArray();
            } else if (msg instanceof MessageLite.Builder) {
                body = ((MessageLite.Builder) msg).build().toByteArray();
            }

            if (body == null) {
                log.error("NettyEncoder body is null.");
                return;
            }

            out.writeInt(body.length);
            out.writeBytes(body);
        } catch (Exception e) {
            log.error("encode exception, " + RemotingHelper.parseChannelRemoteAddr(ctx.channel()), e);
            if (msg != null) {
                log.error(msg.toString());
            }
            RemotingUtil.closeChannel(ctx.channel());
        }
    }
}
