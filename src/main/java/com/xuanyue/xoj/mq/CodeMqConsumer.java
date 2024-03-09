package com.xuanyue.xoj.mq;

import com.rabbitmq.client.Channel;
import com.xuanyue.xoj.common.ErrorCode;
import com.xuanyue.xoj.exception.BusinessException;
import com.xuanyue.xoj.judge.JudgeService;
import com.xuanyue.xoj.model.entity.Question;
import com.xuanyue.xoj.model.entity.QuestionSubmit;
import com.xuanyue.xoj.model.enums.QuestionSubmitStatuEnum;
import com.xuanyue.xoj.service.QuestionService;
import com.xuanyue.xoj.service.QuestionSubmitService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static com.xuanyue.xoj.constant.MqConstant.CODE_QUEUE;


/**
 * Mq 消费者
 *
 * @author xuanyue_18
 * CreateTime 2023/6/24 15:53
 */
@Component
@Slf4j
public class CodeMqConsumer {

    @Resource
    private JudgeService judgeService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;


    /**
     * 指定程序监听的消息队列和确认机制
     *
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = {CODE_QUEUE}, ackMode = "MANUAL")
    private void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        if (message == null) {
            rejectMessage(channel, deliveryTag, ErrorCode.NULL_ERROR, "消息为空");
        }
        log.info("receiveMessage message = {}", message);
        long questionSubmitId = Long.parseLong(message);
        try {
            judgeService.doJudge(questionSubmitId);
            QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
            if (!questionSubmit.getStatus().equals(QuestionSubmitStatuEnum.SUCCESS.getValue())) {
                rejectMessage(channel, deliveryTag, ErrorCode.OPERATION_ERROR, "判题失败");
            }
            log.info("新提交的信息：{}", questionSubmit);
            // 设置通过数
            Long questionId = questionSubmit.getQuestionId();
            log.info("题目: {}", questionId);
            Question question = questionService.getById(questionId);
            Integer acceptedNum = question.getAcceptedNum();
            Question updateQuestion = new Question();
            synchronized (question.getAcceptedNum()) {
                acceptedNum = acceptedNum + 1;
                updateQuestion.setId(questionId);
                updateQuestion.setAcceptedNum(acceptedNum);
                boolean save = questionService.updateById(updateQuestion);
                if (!save) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存数据失败");
                }
            }
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            rejectMessage(channel, deliveryTag, ErrorCode.SYSTEM_ERROR, "系统错误");
            throw new RuntimeException(e);
        }
    }

    private void rejectMessage(Channel channel, long deliveryTag, ErrorCode errorCode, String errorMessage) throws IOException {
        channel.basicNack(deliveryTag, false, false);
        throw new BusinessException(errorCode, errorMessage);
    }
}
