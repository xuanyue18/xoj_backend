package com.xuanyue.xoj.judge;

import com.xuanyue.xoj.judge.strategy.DefaultJudgeStrategy;
import com.xuanyue.xoj.judge.strategy.JavaLanguageJudgeStrategy;
import com.xuanyue.xoj.judge.strategy.JudgeContext;
import com.xuanyue.xoj.judge.strategy.JudgeStrategy;
import com.xuanyue.xoj.judge.codesanbox.model.JudgeInfo;
import com.xuanyue.xoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理(简化调用)
 *
 * @author xuanyue_18
 * @date 2023/9/10 21:02
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
