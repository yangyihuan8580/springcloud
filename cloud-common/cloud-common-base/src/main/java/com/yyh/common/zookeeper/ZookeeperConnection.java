package com.yyh.common.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/4/4
 * \* Time: 18:07
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class ZookeeperConnection implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("receive the event:" + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            countDownLatch.countDown();
        }
        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            System.out.println("NodeChildrenChanged" + watchedEvent.getPath());
        }
    }

    public void await() throws InterruptedException {
        countDownLatch.await();
    }


//    public static void main(String[] args) throws IOException {
//        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperConnection());
//        System.out.println(zooKeeper.getState());
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("zookeeper session established");
//    }


}