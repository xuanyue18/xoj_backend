package com.xuanyue.xoj.judge.codesanbox.impl;

import com.xuanyue.xoj.judge.codesanbox.CodeSandbox;
import com.xuanyue.xoj.judge.codesanbox.model.ExecuteCodeRequest;
import com.xuanyue.xoj.judge.codesanbox.model.ExecuteCodeResponse;

/**
 * 远程代码沙箱(实际调用接口的沙箱)
 *
 * @author xuanyue_18
 * @date 2023/9/10 16:30
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
