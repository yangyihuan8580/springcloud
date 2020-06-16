package com.yyh.common.zookeeper;

import org.apache.xmlbeans.soap.SOAPArrayType;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/4/4
 * \* Time: 18:06
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class ZookeeperTest {

    public static final String ADDRESS = "47.105.193.7:2181";

    public static final String NODE_PREFIX = "/test";

    private static ZooKeeper zooKeeper;

    static {
        try {
            ZookeeperConnection watcher = new ZookeeperConnection();
            zooKeeper = new ZooKeeper(ADDRESS, 5000, watcher);
            watcher.await();
        } catch (IOException e) {
            System.out.println("zookeeper connect failed");
            e.printStackTrace();
        } catch (InterruptedException e) {

        }
        System.out.println("zookeeper connected");
    }

//    public String createSyncNode(String path, byte[] data, ZooDefs) {
//        zooKeeper.create()
//    }

    public static void main(String[] args) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        Stat exists = zooKeeper.exists(NODE_PREFIX, true);
        if (exists == null) {
            String s = zooKeeper.create(NODE_PREFIX, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        String s1 = zooKeeper.create(NODE_PREFIX + "/a1", "a1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        byte[] data = zooKeeper.getData(s1, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("getData event" + event);
            }
        }, null);
        System.out.println(new String(data, "UTF-8"));

        Stat stat = zooKeeper.setData(s1, "aaaa".getBytes(), -1);

//        String s2 = zooKeeper.create(NODE_PREFIX + "/a2", "a2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        String s3 = zooKeeper.create(NODE_PREFIX + "/a3", "a3".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//
//        System.out.println(zooKeeper.getChildren(NODE_PREFIX, true));
//        byte[] data = zooKeeper.getData(NODE_PREFIX + "/a1", false, null);
//        System.out.println("data:" + new String(data, "UTF-8"));

//        try {
//            zooKeeper.delete(s2, -1);
//        } catch (Exception e) {
//            System.out.println("delete zookeeper node " + s2);
//            e.printStackTrace();
//        }


//        zooKeeper.create(NODE_PREFIX, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, new AsyncCallback.StringCallback() {
//
//            @Override
//            public void processResult(int rc, String path, Object ctx, String name) {
//                System.out.println("rc:"+rc+",path:"+path+",ctx:"+ctx+"name,"+name);
//            }
//        }, "my test text...2");
        zooKeeper.close();
    }


}