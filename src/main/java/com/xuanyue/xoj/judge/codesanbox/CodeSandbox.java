package com.xuanyue.xoj.judge.codesanbox;

import com.xuanyue.xoj.judge.codesanbox.model.ExecuteCodeRequest;
import com.xuanyue.xoj.judge.codesanbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 * @author xuanyue_18
 * @date 2023/9/10 16:15
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
