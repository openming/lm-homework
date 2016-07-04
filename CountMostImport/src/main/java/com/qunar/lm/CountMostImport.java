package com.qunar.lm;

import java.io.*;
import java.util.*;

/**
 *  问题分析
 *  输入：java源文件目录
 *  输出：前十个被import最多的类
 *  思路：1遍历文件方法+2文件统计类方法+3最终输出显示方法
 *  细节：1递归遍历思路代码简单，性能受文件路径深度影响，
 *           非递归遍历性能稳定，更优。
 *           2注意*替代符，判定全面
 *           3排序，前10个，适于优先队列，注意比较器消除并列。
 * Created by lm on 2016/7/1 0001.
 */
public class CountMostImport {

    private Map<String,Integer> importClassMap= new HashMap<String,Integer>();
    /**
     * 非递归遍历文件方法
     */
    private void traversalFiles(String path){
        File file= new File(path);
        Stack<File> stack=new Stack();
        stack.push(file);
        while (!stack.empty()){
            file = stack.peek();
            stack.pop();
            for(File f:file.listFiles()){
                if(f.isFile()){
                    countFile(f);
                }else if(f.isDirectory()){
                    stack.push(f);
                }
            }
        }
    }
    /**
     * 计数文件中的import类方法
     */
    private void countFile(File file){
        if(file.getName().contains(".java"))
        {
            BufferedReader br=null;
            //System.out.println("处理文件："+file.getName());
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line;
                while((line=br.readLine())!=null)
                {
                    if(line.contains("import"))
                    {
                        line=line.replaceAll(";","");
                        line=line.replace("import","");
                        line=line.replace(" ","");
                        if(importClassMap.containsKey(line))
                        {
                            int time=importClassMap.get(line);
                            time++;
                            importClassMap.put(line,time);
                        }
                        else
                            importClassMap.put(line,1);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(br!=null)
                {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    /**
     * 显示最多被引用的类方法
     */
    private void countMostImport(int N){
        PriorityQueue<Map.Entry<String,Integer>> priorityQueue=new PriorityQueue<>(N + 1, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for (Map.Entry<String, Integer> entry : importClassMap.entrySet()) {
            priorityQueue.add(entry);
            if (priorityQueue.size() > N) {
                priorityQueue.poll();
            }
        }
        while(!priorityQueue.isEmpty()){
            Map.Entry<String, Integer> entry = priorityQueue.poll();
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
    }
}
