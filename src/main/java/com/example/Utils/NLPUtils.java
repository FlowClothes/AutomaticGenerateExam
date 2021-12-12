package com.example.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NLPUtils {

    private JSONArray NLPResult;

    private String person = "";
    private String date = "";
    private String location = "";
    private String event = "";

    /**
     * 判断某个句子是否重要
     * 判断规则：时间，地点，人名，事件，4项中至少包含3项
     * importance>=3，则认为句子是重要的
     *
     * @param NLPResult
     */
    public static boolean checkImportance(JSONObject NLPResult)
    {
        int importance_of_person = 0;
        int importance_of_org = 0;
        int importance_of_date = 0;
        int importance_of_location = 0;

        int importance = 0;
        //从msra换成ontonotes
//        JSONArray ner_msra = NLPResult.getJSONArray("ner/msra");
        JSONArray ner_ontonotes = NLPResult.getJSONArray("ner/ontonotes");

        for (int i = 0; i < ner_ontonotes.size(); i++)
        {
            JSONArray temp = ner_ontonotes.getJSONArray(i);
            if ("DATE".equals(temp.getString(1)))
            {
                importance_of_date = 1;
            }
            if ("PERSON".equals(temp.getString(1)))
            {
                importance_of_person = 1;
            }
            if ("ORG".equals(temp.getString(1)))
            {
                importance_of_org = 1;
            }
            if ("LOCATION".equals(temp.getString(1)) || "GPE".equals(temp.getString(1)))
            {
                importance_of_location = 1;
            }
        }
        importance = importance_of_date + importance_of_org + importance_of_location + importance_of_person;
        if (importance >= 3)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 获取句子中的实体关系
     * 返回值为JSONArray
     *
     * @param NLPResult
     * @return
     */
    public static JSONArray getEntities(JSONObject NLPResult)
    {
        JSONArray srl = NLPResult.getJSONArray("srl");
        JSONArray result = new JSONArray();
        JSONArray srl_with_ARG0 = new JSONArray();

        //第一层循环，循环的是分句
        for (int i = 0; i < srl.size(); i++)
        {
            JSONArray temp = srl.getJSONArray(i);
            //第二个循环，循环的是分句中的每一个实体关系
            //如果第二个循环中有ARG0，说明这一个分句有主语
            for (int j = 0; j < temp.size(); j++)
            {
                JSONArray entity = temp.getJSONArray(j);

                if (entity.getString(1).equals("ARG0"))
                {
                    srl_with_ARG0.add(temp);
                }
            }
        }

        //如果句子中只有ARG0，没有ARG1到ARG5，那么这个句子不要
        boolean[] hasARG12345 = new boolean[srl_with_ARG0.size()];
        for (int i = 0; i < hasARG12345.length; i++)
        {
            hasARG12345[i] = false;
        }
        //是false的全部删除

        for (int i = 0; i < srl_with_ARG0.size(); i++)
        {
            boolean hasARG1_5 = false;
            JSONArray temp = srl_with_ARG0.getJSONArray(i);

            for (int j = 0; j < temp.size(); j++)
            {
                JSONArray entity = temp.getJSONArray(j);
                if (entity.getString(1).equals("ARG1")
                        || entity.getString(1).equals("ARG2")
                        || entity.getString(1).equals("ARG3")
                        || entity.getString(1).equals("ARG4")
                        || entity.getString(1).equals("ARG5"))
                {
                    hasARG1_5 = true;
                }
            }


            if (hasARG1_5)
            {
                hasARG12345[i] = true;
            }
        }

        for (int i = hasARG12345.length - 1; i >= 0; i--)
        {
            if (!hasARG12345[i])
            {
                srl_with_ARG0.remove(i);
            }
        }


        for (int i = 0; i < srl_with_ARG0.size(); i++)
        {
            JSONArray temp = srl_with_ARG0.getJSONArray(i);

            JSONArray child_array = new JSONArray();

            for (int j = 0; j < temp.size(); j++)
            {
                JSONArray entity = temp.getJSONArray(j);

                JSONObject arg0_object = new JSONObject();
                JSONObject arg1_object = new JSONObject();
                JSONObject arg2_object = new JSONObject();
                JSONObject arg3_object = new JSONObject();
                JSONObject arg4_object = new JSONObject();
                JSONObject arg5_object = new JSONObject();
                JSONObject pred_object = new JSONObject();

                if (entity.getString(1).equals("ARG0"))
                {
                    arg0_object.put("ARG0", entity.getString(0));
                    child_array.add(arg0_object);
                }

                if (entity.getString(1).equals("ARG1"))
                {
                    arg1_object.put("ARG1", entity.getString(0));
                    child_array.add(arg1_object);
                }
                if (entity.getString(1).equals("ARG2"))
                {
                    arg2_object.put("ARG2", entity.getString(0));
                    child_array.add(arg2_object);
                }
                if (entity.getString(1).equals("ARG3"))
                {
                    arg3_object.put("ARG3", entity.getString(0));
                    child_array.add(arg3_object);
                }
                if (entity.getString(1).equals("ARG4"))
                {
                    arg4_object.put("ARG4", entity.getString(0));
                    child_array.add(arg4_object);
                }
                if (entity.getString(1).equals("ARG5"))
                {
                    arg5_object.put("ARG5", entity.getString(0));
                    child_array.add(arg5_object);
                }

                if (entity.getString(1).equals("PRED"))
                {
                    pred_object.put("PRED", entity.getString(0));
                    child_array.add(pred_object);
                }

                if (entity.getString(1).equals("ARGM-TMP"))
                {
                    pred_object.put("ARGM-TMP", entity.getString(0));
                    child_array.add(pred_object);
                }

            }

            result.add(child_array);
        }


        System.out.println(srl_with_ARG0);

        return srl_with_ARG0;
    }

    public static JSONObject executePython(String toPath)
    {
        String exe = "python";
        //要执行python文件的位置
        String command = "D:\\Future\\Others\\JavaExercise\\NLP\\src\\main\\java\\Runtime.py";
        //toPath传递给python的参数
        String[] cmdArr = new String[]{exe, command, toPath};
        Process process = null;

        String resultStr = "";
        JSONObject resultJSONO = null;

        try
        {
            process = Runtime.getRuntime().exec(cmdArr);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "gb2312"));
            String line = null;
            while ((line = in.readLine()) != null)
            {
                resultStr += line;
            }
            resultJSONO = JSONObject.parseObject(resultStr);

            process.waitFor();
            return resultJSONO;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
