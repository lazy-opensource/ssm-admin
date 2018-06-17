package com.lzy.innovate.controller.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzy.innovate.utils.KV;
import com.lzy.innovate.utils.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lzy on 2017/2/27.
 *
 * 针对前台dataTable插件的参数处理
 */
public class ParseFrontParamtHelper {

    private static Logger logger = LoggerFactory.getLogger("innovate_web_log");
    private Pagin model;
    private String sEcho = "";

    public ParseFrontParamtHelper(Pagin e){
        if (e == null){
            throw new RuntimeException("ParseFrontParamtHelper constructor param is null");
        }
        this.model = e;
    }


    /**
     *
     * 处理排序
     **/
    public void handlePaginAndSort(HttpServletRequest request){

        if (model == null){

            logger.info("ParseFrontParamtHelper handlePagin method param is null");
            return ;
        }

        Map<String, Object> pageParamMap = parsePageParam(request);

        int iDisplayStart = ((Integer) pageParamMap.get("iDisplayStart")).intValue();

        int iDisplayLength = ((Integer) pageParamMap.get("iDisplayLength")).intValue();


        model.setCurrent(iDisplayStart);
        model.setSize(iDisplayLength);

        int iSortCol_0 = ((Integer) pageParamMap.get("iSortCol_0")).intValue();

        String sortFieldName = (String) pageParamMap.get("mDataProp_"
                + iSortCol_0);

        String sortTypeString = (String) pageParamMap.get("sSortDir_0");

        Boolean needSort = (Boolean) pageParamMap
                .get("bSortable_" + iSortCol_0);

        if (needSort.booleanValue()) {
            model.setSortField(sortFieldName);
            model.setSortType(sortTypeString);
        }
        sEcho = pageParamMap.get("sEcho").toString();

        return ;
    }


    public Map<String, Object> handleResult(Pagin model){

        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap.put("sEcho", sEcho);
        resultMap.put("iTotalRecords", model.getTotal());
        resultMap.put("iTotalDisplayRecords", model.getTotal());
        resultMap.put("aaData", model.getRows());

        return resultMap;
    }


    /**
     * 解析前台参数
     * 该方法是自用方法，不可被子类覆盖
     * **/
    public final Map<String, Object> parsePageParam(HttpServletRequest request) {
        String data = request.getParameter("aoData");
        HashMap pageParamMap = new HashMap();
        JSONArray jsonArr = JSONArray.parseArray(data);
        Iterator i$ = jsonArr.iterator();

        while(i$.hasNext()) {

            Object o = i$.next();

            JSONObject jo = (JSONObject)o;
            KV dtpp = (KV) JSON.parseObject(jo.toJSONString(), KV.class);
            pageParamMap.put(dtpp.getName(), dtpp.getValue());
        }

        return pageParamMap;
    }


    /**
     * 将前台传过来的参数searchParam设置到当前泛型的类对象中
     * **/
    public void handleSearchParam(HttpServletRequest request){

        String searchParam = "";
        if (request == null){
            logger.info("request is null ");
            return ;
        }
        searchParam = request.getParameter("condition");
        logger.debug("search param -------- " + searchParam);

        HashMap<String , Object> searchParamMap = Sets.map();
        JSONArray searParamArr = JSONArray.parseArray(searchParam);
        Iterator i$ = searParamArr.iterator();

        //将前台参数换为对象DataTablesPageParam,放入Map
        while(i$.hasNext()) {
            Object key = i$.next();
            JSONObject v = (JSONObject)key;
            KV fieldname = (KV) JSON.parseObject(v.toJSONString(), KV.class);
            searchParamMap.put(fieldname.getName(), fieldname.getValue());
        }

        i$ = searchParamMap.keySet().iterator();
        while(i$.hasNext()) {

            String key1 = (String)i$.next();
            String v1 = (String)searchParamMap.get(key1);
            String fieldName = key1;
            if(!StringUtils.isEmpty(v1)) {
                if (!key1.contains("_q")){
                    Object tempStr = searchParamMap.get(key1 + "_q");
                    if (v1.contains("@_date_@") && key1.contains("create")){
                        String[] temp = v1.split("@_date_@");
                        String[] rule = searchParamMap.get(key1 + "_q").toString().split("@_date_@");
                        String startTime = temp[0];
                        String endTime = temp[1];

                        model.getConditions().put("create_startTime",startTime);
                        model.getConditions().put("create_endTime", endTime);
                        model.getRule().put("create_startTime" , rule[0]);
                        model.getRule().put("create_endTime" , rule[1]);
                    }else if (v1.contains("@_date_@") && key1.contains("update")){
                        String[] temp = v1.split("@_date_@");
                        String[] rule = tempStr != null ? tempStr.toString().split("@_date_@") : "".split("@_date_@");
                        String startTime = temp[0];
                        String endTime = temp[1];

                        model.getConditions().put("update_startTime",startTime);
                        model.getConditions().put("update_endTime", endTime);
                        model.getRule().put("update_startTime" , rule[0]);
                        model.getRule().put("update_endTime" , rule[1]);

                    }else{
                        model.getConditions().put(key1, v1);
                        model.getRule().put(key1, tempStr != null ? tempStr.toString() : "");
                    }

                }
            }

        }

        logger.info("front param model --------" + model.toString());

        return ;
    }




}
