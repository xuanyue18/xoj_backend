package com.xuanyue.xoj.judge.codesanbox;

import com.xuanyue.xoj.judge.codesanbox.impl.ExampleCodeSandbox;
import com.xuanyue.xoj.judge.codesanbox.impl.RemoteCodeSandbox;
import com.xuanyue.xoj.judge.codesanbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 *
 * @author xuanyue_18
 * @date 2023/9/10 18:46
 */
public class CodeSandboxFactory {

    /**
     * 创建代码沙箱示例
     *
     * @param type 沙箱类型
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}