package com.wangsy.ouraccounts.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.wangsy.ouraccounts.callback.OnQueryDataReceived;
import com.wangsy.ouraccounts.model.AccountModel;
import com.wangsy.ouraccounts.constants.TableConstants;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异步查询数据
 * <p/>
 * Created by wangsy on 15/10/31.
 */
public class QueryDataTask extends AsyncTask<Map<String, Object>, Void, Map<String, Object>> {

    /**
     * 每页显示数据的数目
     */
    private static final int PER_PAGE_COUNT = 10;

    /**
     * 线程查询数据完成后，通过回调，在ui线程进行处理
     */
    private OnQueryDataReceived onQueryDataReceived;

    private Context context;
    private ProgressDialog dialog;

    public static final String PAGE = "page";
    public static final String TOTAL_PAGES = "total_pages";
    public static final String TYPE = "type";
    public static final String START_DATETIME = "start_datetime";
    public static final String END_DATETIME = "end_datetime";
    public static final String RESULT_LIST = "result_list";

    public QueryDataTask(Context context, OnQueryDataReceived onQueryDataReceived) {
        this.context = context;
        this.onQueryDataReceived = onQueryDataReceived;
    }

    @SafeVarargs
    @Override
    protected final Map<String, Object> doInBackground(Map<String, Object>... params) {
        int page = (int) params[0].get(PAGE);
        String strType = (String) params[0].get(TYPE);
        String strStartDatetime = (String) params[0].get(START_DATETIME);
        String strEndDatetime = (String) params[0].get(END_DATETIME);

        int totalCount;
        int totalPages;
        List<AccountModel> resultList;

        if (null != strType && !"".equals(strType)
                && null != strStartDatetime && !"".equals(strStartDatetime)
                && null != strEndDatetime && !"".equals(strEndDatetime)) {

            totalCount = DataSupport.where(TableConstants.TYPE + " = ? and "
                    + TableConstants.DATETIME + " between ? and ?", strType, strStartDatetime, strEndDatetime)
                    .count(AccountModel.class);
            totalPages = totalCount % PER_PAGE_COUNT == 0 ? totalCount / PER_PAGE_COUNT : totalCount / PER_PAGE_COUNT + 1;
            resultList = DataSupport.where(TableConstants.TYPE + " = ? and "
                    + TableConstants.DATETIME + " between ? and ?", strType, strStartDatetime, strEndDatetime)
                    .order("datetime desc")
                    .limit(PER_PAGE_COUNT)
                    .offset(PER_PAGE_COUNT * page)
                    .find(AccountModel.class);

        } else if (null != strStartDatetime && !"".equals(strStartDatetime)
                && null != strEndDatetime && !"".equals(strEndDatetime)) {

            totalCount = DataSupport.where(TableConstants.DATETIME + " between ? and ?", strStartDatetime, strEndDatetime)
                    .count(AccountModel.class);
            totalPages = totalCount % PER_PAGE_COUNT == 0 ? totalCount / PER_PAGE_COUNT : totalCount / PER_PAGE_COUNT + 1;
            resultList = DataSupport.where(TableConstants.DATETIME + " between ? and ?", strStartDatetime, strEndDatetime)
                    .order("datetime desc")
                    .limit(PER_PAGE_COUNT)
                    .offset(PER_PAGE_COUNT * page)
                    .find(AccountModel.class);

        } else if (null != strType && !"".equals(strType)) {
            totalCount = DataSupport.where(TableConstants.TYPE + " = ?", strType).count(AccountModel.class);
            totalPages = totalCount % PER_PAGE_COUNT == 0 ? totalCount / PER_PAGE_COUNT : totalCount / PER_PAGE_COUNT + 1;
            resultList = DataSupport.where(TableConstants.TYPE + " = ?", strType)
                    .order("datetime desc")
                    .limit(PER_PAGE_COUNT)
                    .offset(PER_PAGE_COUNT * page)
                    .find(AccountModel.class);

        } else {
            totalCount = DataSupport.count(AccountModel.class);
            totalPages = totalCount % PER_PAGE_COUNT == 0 ? totalCount / PER_PAGE_COUNT : totalCount / PER_PAGE_COUNT + 1;
            resultList = DataSupport.order("datetime desc")
                    .limit(PER_PAGE_COUNT)
                    .offset(PER_PAGE_COUNT * page)
                    .find(AccountModel.class);
        }

        Map<String, Object> result = new HashMap<>();
        result.put(TOTAL_PAGES, totalPages);
        result.put(RESULT_LIST, resultList);

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "", "正在加载，请稍等...");
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        dialog.dismiss();
        onQueryDataReceived.onQueryDataReceived(result);
        super.onPostExecute(result);
    }
}
