package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import sun.font.TrueTypeFont;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AutoFull  {

    private void updateContact(Jedis jedis, String userName, String contactName) {
        Pipeline pipeline = jedis.pipelined();
        String userList = "recent" + userName;
        //很神奇，即使一开始没有key的也能够执行第一行的删除。。。。
        pipeline.lrem(userList,0,contactName);
        pipeline.lpush(userList,contactName);
        pipeline.ltrim(userList,0,100);
        //这里有个问题。。。用exec会报错，可能python和java不一样，后续再看看
        pipeline.syncAndReturnAll();
    }

    private void doNotLike(Jedis jedis, String userName, String contactName){
        String userList = "recent" + userName;
        jedis.lrem(userList,0,contactName);
    }

    private List<String> getContact(Jedis jedis, String userName, String search){
        String userList = "recent" + userName;
        List<String> contactList = jedis.lrange(userList,0,-1);
        List<String> match  = new ArrayList<String>();
        if(null != contactList && contactList.size() > 0){
            for(String contactName:contactList){
                if(contactName.toLowerCase().startsWith(search)){
                    match.add(contactName);
                }
            }
        }
        return match;
    }

    public static void main(String[] args) {
        Jedis jedis = JedisPoolBuildPool.getJedis();
        AutoFull autoFull = new AutoFull();
        System.out.println("请问你是谁");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();
        System.out.println("好的,"+userName);
        while(true){
            System.out.println("请问你想联系谁？");
            String contact =  sc.nextLine();
            System.out.println("你想联系的是"+contact);
            List<String> match = autoFull.getContact(jedis,userName,contact);
            if(null != match && match.size() > 0){
                System.out.println("这是搜索到最近联系人");
                for(String matchName:match){
                    System.out.print(matchName + ",");
                }
                System.out.println("请选择联系人编号");
                int flag = sc.nextInt();
                if(flag >= 0){
                    String contactName = match.get(flag);
                    System.out.println("好的，您已经选择了"+contactName);
                    autoFull.updateContact(jedis,userName,contactName);
                }else{
                    System.out.println("您不打算选择，那么请重新开始流程吧");
                    match.clear();
                    sc.nextLine();
                    continue;
                }

            }else{
                System.out.println("没有这么个联系人，是否要添加他呢？0是1否");
                int flag = sc.nextInt();
                if(0 == flag){
                    autoFull.updateContact(jedis,userName,contact);
                    System.out.println("好的，已添加");
                }else{
                    System.out.println("好的，那么请重新开始流程吧");
                    match.clear();
                    sc.nextLine();
                    continue;
                }

            }
            match.clear();
            sc.nextLine();

        }
    }
}
