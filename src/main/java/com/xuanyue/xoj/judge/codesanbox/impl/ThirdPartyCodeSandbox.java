package com.xuanyue.xoj.judge.codesanbox.impl;

import com.xuanyue.xoj.judge.codesanbox.CodeSandbox;
import com.xuanyue.xoj.model.dto.codesandbox.ExecuteCodeRequest;
import com.xuanyue.xoj.model.dto.codesandbox.ExecuteCodeResponse;


/**
 * 第三方代码沙箱(调用网上的现成的代码沙箱)
 *
 * @author xuanyue_18
 * @date 2023/9/10 16:30
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
