package com.base.sbc.config.common;

import com.google.common.collect.Lists;

import java.util.List;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
public class IdGen {
	 // ==============================Fields===========================================
	
	/** 工作机器ID(0~31) */
    private long workerId = 0;

    /** 数据中心ID(0~31) */
    private long datacenterId = 0;
    
    /** 开始时间截 (2018-01-01) */
    private final long twepoch = 1514736000000L;

    /** 机器id所占的位数 */
    private final long workerIdBits = 5L;

    /** 数据标识id所占的位数 */
    private final long datacenterIdBits = 5L;

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 支持的最大数据标识id，结果是31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 12L;

    /** 机器ID向左移12位 */
    private final long workerIdShift = sequenceBits;

    /** 数据标识id向左移17位(12+5) */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================
    /**
     * 构造函数
     * @param workerId 工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     * @return 
     */
    public void snowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    public String nextIdStr() {
        return String.valueOf(nextId());
    }
    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
    
    /**
     * 获取id
     * @return
     */
    public static Long getId() {
    	IdGen idWorker = new IdGen();
    	return idWorker.nextId();
    }
    /**
     * 生成List<String> id集合
     * @param i 个数
     * @return 
     */
    public static List<String> getIds(int i) {
    	if(i<0) {
    		return null;
    	}
    	IdGen idWorker = new IdGen();
    	List<String> list = Lists.newArrayList();
    	for (int j = 0; j < i; j++) {
    		 String id = String.valueOf(idWorker.nextId());
    		 list.add(id);
		}
    	return list;
    }
    
    //==============================Test=============================================
    /** 测试 */
   /* public static void main(String[] args) {
    	List<String> ids = IdGen.getIds(5);
    	List<String> ids2 = IdGen.getIds(5);
    	for (int i = 0; i < ids.size(); i++) {
			System.out.println(ids.get(i));
			System.out.println(ids2.get(i));
		}
       IdGen idWorker = new IdGen();
       IdGen idWorker2 = new IdGen();
        for (int i = 0; i < 100; i++) {
            long id = idWorker.nextId();
            System.out.println(id);
            long id2 = idWorker2.nextId();
            System.out.println(id2);
        }
//            System.out.println("长度："+new Long(id).toString().length());
//            long thisD = 1514736000000L+(id>>22);
//            System.out.println(DateUtil3.longToDateStr(thisD)); 
           
//            	String idAll = Long.toBinaryString(id);
//            System.out.println();
//            System.out.println(idAll);
//            System.out.println( Long.toBinaryString(System.currentTimeMillis()));
           
//            System.out.println(id);
//            int l = new Long(id).toString().length();
//            System.out.println(l);
        
        
        
    }*/
}
