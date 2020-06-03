package com.quinn.framework.model;

import com.quinn.framework.api.message.MessageInstance;
import com.quinn.framework.api.message.MessageSendRecord;
import com.quinn.util.base.StringUtil;
import com.quinn.util.base.model.BaseResult;
import com.quinn.util.constant.CharConstant;
import com.quinn.util.constant.StringConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 直接消息信息
 * 实例中同一属性线程之间不共享
 *
 * @author Qunhu.liao
 * @since 2020-02-08
 */
@Setter
@Getter
public class DirectMessageInfo {

    /**
     * 消息主题最大长度
     */
    public static final int MAX_SUBJECT_LENGTH = 30;

    /**
     * 默认线程容量
     */
    public static final int DEFAULT_THREAD_SIZE = 3;

    public DirectMessageInfo(String batchKey) {
        this.batchKey = batchKey;
    }

    public static DirectMessageInfo newInstance() {
        String uuid = StringUtil.uuid();
        return new DirectMessageInfo(uuid);
    }

    public static DirectMessageInfo newInstance(String batchKey) {
        return new DirectMessageInfo(batchKey);
    }

    /**
     * 批次号
     */
    private String batchKey;

    /**
     * 消息数
     */
    private int instNum;

    /**
     * 错误消息
     */
    private StringBuffer errorMessage = new StringBuffer();

    /**
     * 等待所有线程走完锁
     */
    private CountDownLatch countDownLatchTotal = new CountDownLatch(DEFAULT_THREAD_SIZE);

    /**
     * 参数换算的线程
     */
    private MessageThread paramThread;

    /**
     * 内容填充人线程
     */
    private MessageThread contentThread;

    /**
     * 收件人填充信息
     */
    private MessageThread receiverThread;

    /**
     * 消息类型 + 语言为key，生成不同消息实例
     */
    private Map<String, MessageInstance> instanceMap = new HashMap<>();

    /**
     * 消息类型 + 语言为key，生成不同消息发送记录
     */
    private Map<String, List<MessageSendRecord>> sendRecordListMap = new HashMap<>();

    /**
     * 添加消息实例
     *
     * @param messageInstance 消息实例
     */
    public void addInstance(MessageInstance messageInstance) {
        messageInstance.setBatchKey(batchKey);
        instanceMap.put(messageInstance.sendGroup(), messageInstance);
    }

    /**
     * 添加消息实例
     *
     * @param key      消息实例
     * @param instance 消息实例
     */
    public void addInstance(String key, MessageInstance instance) {
        instance.setBatchKey(batchKey);
        instanceMap.put(key, instance);
    }

    /**
     * 添加发送地址
     *
     * @param sendRecordList 发送地址列表
     */
    public void addSendRecords(List<MessageSendRecord> sendRecordList) {
        if (CollectionUtils.isEmpty(sendRecordList)) {
            return;
        }

        String key1 = StringConstant.STRING_EMPTY;
        List<MessageSendRecord> sendRecordList1 = null;

        for (MessageSendRecord record : sendRecordList) {
            String key = record.sendGroup();

            if (!key.equals(key1)) {
                key1 = key;
                sendRecordList1 = sendRecordListMap.get(key);
                if (sendRecordList1 == null) {
                    sendRecordList1 = new ArrayList<>();
                    sendRecordListMap.put(key, sendRecordList1);
                }
            }
            sendRecordList1.add(record);
        }
    }

    /**
     * 根据主键获取消息实例
     *
     * @param key 主键（类型+":"+语言）
     * @return 消息实例
     */
    public MessageInstance getInstance(String key) {
        MessageInstance instance = instanceMap.get(key);
        if (instance != null) {
            return instance;
        }

        if (instanceMap.size() == 1) {
            return instanceMap.values().iterator().next();
        }

        String[] ks = key.split(":");
        instance = instanceMap.get("ALL" + ":" + ks[1]);
        if (instance != null) {
            return instance;
        }

        instance = instanceMap.get(ks[0] + ":" + "ALL");
        if (instance != null) {
            return instance;
        }

        return instanceMap.get("ALL:ALL");
    }

    /**
     * 异步线程启动
     * 线程启动之前建立依赖关系
     * 因为不确定地址类型，暂且约定收件人一定依赖参数
     * 内容信息一定依赖地址信息
     *
     * @param messageExecutorService 线程池
     */
    public void executeBy(ExecutorService messageExecutorService) {

        // 如果参数解析线程不为空，收件人、参数都会
        CountDownLatch countDownLatch = null;
        if (paramThread != null) {
            countDownLatch = new CountDownLatch(1);
            // 我countDown
            paramThread.setLatchForParam(countDownLatch);
            // 我 await
            receiverThread.setLatchForParam(countDownLatch);
        }

        // 如果消息内容线程不为空
        if (contentThread != null) {
            CountDownLatch latchForReceiver = new CountDownLatch(1);
            // 我 countDown
            receiverThread.setLatchForReceiver(latchForReceiver);
            // 我 await
            contentThread.setLatchForReceiver(latchForReceiver);

            if (countDownLatch != null) {
                // 我 await
                contentThread.setLatchForParam(countDownLatch);
            }
        }

        if (paramThread != null) {
            messageExecutorService.execute(paramThread);
        }

        // 收件人的线程一定会创建：因为依赖模板需要查数据、解析；直接传入可能衣需要代入参数解析
        messageExecutorService.execute(receiverThread);

        if (contentThread != null) {
            messageExecutorService.execute(contentThread);
        }
    }

    /**
     * 添加线程
     *
     * @param thread 线程
     */
    public void addThread(MessageThread thread) {
        thread.setCountDownLatchTotal(countDownLatchTotal);
        // 避免生成Class$1
        switch (thread.threadType().code) {
            case 10:
                this.paramThread = thread;
                break;
            case 20:
                this.contentThread = thread;
                break;
            case 30:
                this.receiverThread = thread;
                break;
            default:
                break;
        }
    }

    /**
     * 准备好，让异步线程走完
     *
     * @return 是否准备好
     */
    public BaseResult ready() {
        // FIXME
        if (errorMessage.length() > 0) {
            return BaseResult.fail(errorMessage.substring(1));
        }

        // 总锁数为 3：如果参数没线程，减1
        if (paramThread == null) {
            countDownLatchTotal.countDown();
        }

        // 总锁数为 3：如果内容没线程（一定无），减1
        if (contentThread == null) {
            countDownLatchTotal.countDown();
        }

        // 总锁数为 3：如果收件人没线程（一定有），减1
        if (receiverThread == null) {
            countDownLatchTotal.countDown();
        }

        try {
            countDownLatchTotal.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        if (errorMessage.length() > 0) {
            return BaseResult.fail(errorMessage.substring(1));
        }

        if (CollectionUtils.isEmpty(instanceMap)) {
            return BaseResult.fail("未解析到消息实例");
        }

        if (CollectionUtils.isEmpty(sendRecordListMap)) {
            return BaseResult.fail("未解析到消息发送对象");
        }

        return BaseResult.success(this);
    }

    /**
     * 拼接错误消息
     *
     * @param message 错误消息
     */
    public void appendError(String message) {
        if (errorMessage.length() > 0) {
            errorMessage.append(CharConstant.LINE_BREAK);
        }
        errorMessage.append(message);
    }

    /**
     * 初始化-调用此方法表示不走模板解析
     *
     * @param messageSendParam 消息发送参数
     */
    public void initDirect(MessageSendParam messageSendParam) {
        MessageInstance instance = MessageInfoFactory.createInstance(messageSendParam);
        addInstance(instance);
    }

}
