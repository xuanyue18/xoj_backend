package com.xuanyue.xoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuanyue.xoj.common.BaseResponse;
import com.xuanyue.xoj.common.ErrorCode;
import com.xuanyue.xoj.common.ResultUtils;
import com.xuanyue.xoj.exception.BusinessException;
import com.xuanyue.xoj.judge.JudgeService;
import com.xuanyue.xoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.xuanyue.xoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.xuanyue.xoj.model.entity.QuestionSubmit;
import com.xuanyue.xoj.model.entity.User;
import com.xuanyue.xoj.model.vo.QuestionSubmitVO;
import com.xuanyue.xoj.service.QuestionSubmitService;
import com.xuanyue.xoj.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author xuanyue18
 */
// @RestController
// @RequestMapping("/question_submit")
@Slf4j
@Deprecated
// @Api(tags = "题目提交相关接口")
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return questionSubmitId 提交记录的id
     */
    @PostMapping("/")
    @ApiOperation("提交题目")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表（除管理员外, 普通用户只能看到非答案, 提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation("分页获取题目提交信息列表(管理员和当前用户)")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

}
