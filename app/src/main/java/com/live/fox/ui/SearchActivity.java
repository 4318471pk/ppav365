package com.live.fox.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.User;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.UserDetailActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;



/**
 * 搜索
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;

    private EditText etSearch;

    BaseQuickAdapter adapter;
    private int curPosition = 0;
    private long curFuns;

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setView();
    }

    public void setView() {
        rv = findViewById(R.id.rv_);
        refreshLayout = findViewById(R.id.refreshLayout);
        etSearch = findViewById(R.id.et_searchhead);
        findViewById(R.id.iv_searchhead_back).setOnClickListener(this);
        findViewById(R.id.tv_searchhead_search).setOnClickListener(this);

        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setTopPaddingStatusBarHeight(findViewById(R.id.layout_searchhead));

        setRecycleView();

        etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
                return true;
            }
            return false;
        });
    }


    public void setRecycleView() {

        refreshLayout.setEnablePureScrollMode(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_searchuser, new ArrayList<User>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                User user = (User) item;
                GlideUtils.loadDefaultCircleImage(context, user.getAvatar(), helper.getView(R.id.iv_head));
                helper.setText(R.id.tv_name, user.getNickname());
                helper.setText(R.id.tv_sign, StringUtils.isEmpty(user.getSignature()) ? getString(R.string.noSign) : user.getSignature());
                helper.setText(R.id.tv_fansnum, getString(R.string.fan) + "：" + user.getFans());
                if (user.isFollow()) {
                    helper.setBackgroundRes(R.id.tv_follow, R.drawable.shape_corners_20_gray);
                    helper.setText(R.id.tv_follow, getString(R.string.focused));
                } else {
                    helper.setBackgroundRes(R.id.tv_follow, R.drawable.shape_gradual_orange_light_corners_20);
                    helper.setText(R.id.tv_follow, getString(R.string.focus));
                }

                helper.addOnClickListener(R.id.tv_follow);
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (ClickUtil.isFastDoubleClick()) return;
            curPosition = position;
            User user = (User) adapter.getData().get(position);
            curFuns = user.getFans();
            UserDetailActivity.startActivityForResult(SearchActivity.this, user.getUid());
        });

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (ClickUtil.isFastDoubleClick()) return;
            User user = (User) adapter.getData().get(position);
            if (view.getId() == R.id.tv_follow) {
                doSetFollowApi(position, user);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            boolean flag = data.getExtras().getBoolean("ISFOLLOW");
            long fans = data.getExtras().getLong("FANNUM", curFuns);
            ((User) adapter.getItem(curPosition)).setFollow(flag);
            ((User) adapter.getItem(curPosition)).setFans(fans);
            adapter.notifyItemChanged(curPosition);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_searchhead_back:
                Constant.isAppInsideClick = true;
                finish();
                break;
            case R.id.tv_searchhead_search:
                doSearch();
                break;
        }
    }


    public void doSearch() {
        String content = etSearch.getText().toString().trim();
        if (StringUtils.isEmpty(content)) {
            return;
        }
        doSearchApi(content);
    }


    /**
     * 搜索用户
     */
    public void doSearchApi(String content) {
        showLoadingView();
        Api_User.ins().searchUser(content, new JsonCallback<List<User>>() {
            @Override
            public void onSuccess(int code, String msg, List<User> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                hideLoadingView();
                if (code == 0) {
                    if (data == null || data.size() == 0) {
                        showEmptyView(getString(R.string.noSuchUser));
                    } else {
                        hideEmptyView();
                        adapter.setNewData(data);
                    }
                } else {
                    showEmptyView(msg);
                }
            }
        });
    }

    /**
     * 设置/取消 关注
     */
    public void doSetFollowApi(int position, User user) {
        Api_User.ins().follow(user.getUid(), !user.isFollow(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    ((User) adapter.getItem(position)).setFollow(!user.isFollow());
                    ((User) adapter.getItem(position)).setFans(user.isFollow() ? user.getFans() + 1 : user.getFans() - 1);
                    adapter.notifyItemChanged(position);
                } else {
                    showToastTip(false, msg);
                }

            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

}
