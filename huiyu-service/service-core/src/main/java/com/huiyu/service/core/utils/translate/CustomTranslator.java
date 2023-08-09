package com.huiyu.service.core.utils.translate;

import com.huiyu.service.core.service.SpellbookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义翻译映射
 *
 * @author Naccl
 * @date 2023-08-03
 */
@Component
public class CustomTranslator {
    private static SpellbookService spellbookService;

    private static Map<Character, Character> replaceMap = new HashMap<>();

    private static Pattern printablePattern = Pattern.compile("[a-zA-Z0-9\\p{Punct} ]*");
    private static Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fff]");
    private static Pattern indexPattern = Pattern.compile("[\\u4e00-\\u9fff0-9a-zA-Z]");

    public CustomTranslator(SpellbookService spellbookService) {
        CustomTranslator.spellbookService = spellbookService;
    }

    static {
        replaceMap.put('，', ',');
        replaceMap.put('。', '.');
        replaceMap.put('！', '!');
        replaceMap.put('？', '?');
        replaceMap.put('；', ';');
        replaceMap.put('：', ':');
        replaceMap.put('‘', '\'');
        replaceMap.put('’', '\'');
        replaceMap.put('“', '"');
        replaceMap.put('”', '"');
        replaceMap.put('（', '(');
        replaceMap.put('）', ')');
        replaceMap.put('【', '[');
        replaceMap.put('】', ']');
    }

    /**
     * 预处理待翻译文本
     *
     * @param sourceText 待翻译文本
     * @return 预处理结果
     */
    public static String preProcessText(String sourceText) {
        if (StringUtils.isEmpty(sourceText)) {
            return "";
        }

        // 将中文全角标点符号替换为半角标点符号
        StringBuilder sb = new StringBuilder();
        for (char c : sourceText.toCharArray()) {
            if (replaceMap.containsKey(c)) {
                sb.append(replaceMap.get(c));
            } else {
                sb.append(c);
            }
        }

        // 按逗号分割成数组
        String[] splitTextArr = sb.toString().split(",");

        for (int i = 0; i < splitTextArr.length; i++) {
            String text = splitTextArr[i];
            if (isPrintable(text)) {
                continue;
            }
            if (containsChinese(text)) {
                String[] split = splitString(text);

                // 匹配预定义的翻译prompt
                String translatedPrompt = spellbookService.getPromptByName(split[1]);
                if (StringUtils.isEmpty(translatedPrompt)) {
                    continue;
                }

                // 替换为翻译后的prompt
                splitTextArr[i] = split[0] + translatedPrompt + split[2];
            }
        }

        return String.join(",", splitTextArr);
    }

    /**
     * 判断是否只包含英文字母、数字、标点符号和空格
     *
     * @param text 待判断文本
     * @return true/false
     */
    private static boolean isPrintable(String text) {
        Matcher matcher = printablePattern.matcher(text);
        return matcher.matches();
    }

    /**
     * 判断是否包含中文
     *
     * @param text 待判断文本
     * @return true/false
     */
    private static boolean containsChinese(String text) {
        Matcher matcher = chinesePattern.matcher(text);
        return matcher.find();
    }

    /**
     * 切分字符串，第一个汉字、数字、字母和最后一个汉字、数字、字母为分界点，切分成三部分，将特殊字符和待翻译文本分隔开，便于更精准匹配映射表
     *
     * @param text 待切分文本
     * @return 切分结果
     */
    private static String[] splitString(String text) {
        int firstIndex = findFirstIndex(text);
        int lastIndex = findLastIndex(text);

        String part1 = text.substring(0, firstIndex);
        String part2 = text.substring(firstIndex, lastIndex + 1);
        String part3 = text.substring(lastIndex + 1);

        return new String[]{part1, part2, part3};
    }

    /**
     * 查找第一个汉字、数字、字母的索引
     *
     * @param text 待查找文本
     * @return 索引
     */
    private static int findFirstIndex(String text) {
        Matcher matcher = indexPattern.matcher(text);
        return matcher.find() ? matcher.start() : -1;
    }

    /**
     * 查找最后一个汉字、数字、字母的索引
     *
     * @param text 待查找文本
     * @return 索引
     */
    private static int findLastIndex(String text) {
        String reverseText = new StringBuilder(text).reverse().toString();
        return text.length() - findFirstIndex(reverseText) - 1;
    }

}
