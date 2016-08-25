package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xuery on 2016/8/24.
 */
@Service
public class SensitiveService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    /**
     * 默认敏感词替换符
     */
    private static final String DEFAULT_REPLACEMENT = "***";

    /**
     * 前缀树类
     * 一个节点下面的子节点是不可能相同的
     */
    private class TrieNode{
        /**
         * true 关键字的终结
         * false 继续
         */
        private boolean end = false;

        /**
         * 当前key的下一个字符（可能多个，用map存储），value为子节点对应的节点
         */
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        /**
         * 向指定位置添加节点树
         */
        void addSubNodes(Character key,TrieNode node){
            subNodes.put(key,node);
        }

        /**
         * 获取下一个节点
         */
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd() {
            return end;
        }

        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount(){
            return subNodes.size();
        }
    }

    /**
     * 根节点
     */
    private TrieNode root = new TrieNode();

    /**
     * 判断是否为一个符号
     */
    private boolean isSymbol(char c){
        int ic = (int)c;
        //0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic < 0x2E80 || ic > 0x9FFF);
    }

    /**
     * 构造字典树
     */
    private void  addWord(String lineText){
        TrieNode tempNode = root;
        //循环每个字节
        for(int i=0;i<lineText.length();i++){
            Character c = lineText.charAt(i);
            //过滤掉空格
            if(isSymbol(c)){
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);  //看根节点下是否存在c
            if(node == null){
                //没有则重新创建即可
                node = new TrieNode();
                tempNode.addSubNodes(c,node);
            }

            tempNode = node;   //存在则往下走

            if(i == lineText.length()-1){
                //结尾标志设为true
                tempNode.setKeywordEnd(true);
            }
        }

    }

    /**
     * 继承InitialBean保证在调用Controller之前已经建好了字典数
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream inputStream = null;
        BufferedReader br = null;
        try{
            inputStream = SensitiveService.class.getClassLoader().getResourceAsStream("SensitiveWords.txt");
            br = new BufferedReader(new InputStreamReader(inputStream));
            String lineText;
            while((lineText = br.readLine())!=null){
                lineText = lineText.trim();
                addWord(lineText);
            }
        }catch(Exception e){
            logger.info("读取敏感词文件失败:"+e.getMessage());
        }finally{
            inputStream.close();
            br.close();
        }
    }

    /**
     * 过滤敏感词
     * @param text
     * @return
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder sb = new StringBuilder();

        TrieNode tempNode = root;

        int begin = 0;     //每次与root对齐的位置
        int position = 0;  //当前比较的位置

        while(position < text.length()){
            char c = text.charAt(position);
            //空格直接跳过   这样真的好吗
            if(isSymbol(c)){
                if(tempNode == root){
                    sb.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            //当前位置的匹配结束
            if(tempNode == null){
                // 以begin开始的字符串不存在敏感词
                sb.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin+1;
                begin = position;
                //回到树的初始节点
                tempNode = root;
            }else if(tempNode.isKeywordEnd()){
                //发现敏感词  从begin到position的位置用replacement替换掉
                sb.append(replacement);
                position = position +1 ;
                begin = position;
                tempNode = root;
            }else{
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();

    }

    public static void main(String[] args) throws Exception{
//        SensitiveService s = new SensitiveService();
//        s.afterPropertiesSet();
        SensitiveService s = new SensitiveService();
        s.addWord("色情网");
        s.addWord("好色");
        System.out.print(s.filter("你好X色情**XX"));

    }
}
