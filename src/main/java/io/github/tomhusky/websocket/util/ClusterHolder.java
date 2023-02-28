package io.github.tomhusky.websocket.util;

import io.github.tomhusky.websocket.configuration.WebSocketProperties;

/**
 * <p>
 * 集群工具
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2023/1/29 14:58
 */
public class ClusterHolder {

    private static volatile WebSocketProperties.Cluster cluster;

    public ClusterHolder(WebSocketProperties properties) {
        cluster = properties.getCluster();
    }

    public static boolean isCluster() {
        return cluster.getEnable();
    }

    public static String redisTopic() {
        return cluster.getTopic();
    }
}
