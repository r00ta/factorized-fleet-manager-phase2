package com.r00ta.ffm.manager;

public interface ShardService {

    String getAssignedShardId(String id);

    boolean isAuthorizedShard(String shardId);

    /**
     * TODO: Shard-admin api to be implemented.
     * void addShard();
     * void deleteShard();
     * ...
     */
}
