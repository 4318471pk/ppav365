package com.live.fox.ui.game;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Advert;
import com.live.fox.entity.GameColumn;
import com.live.fox.entity.GameItem;
import com.live.fox.entity.GameListItem;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_AgGame;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Cp;
import com.live.fox.server.Api_FwGame;
import com.live.fox.server.Api_KyGame;
import com.live.fox.server.Api_Pay;
import com.live.fox.server.Api_TYGame;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.GameTopBanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 游戏页面
 */
public class GameFragment extends BaseFragment {

    private RecyclerView rv1;
    private RecyclerView rv2;
    private GameTopBanner gameBanner;

    public static final String SINGLE_ACTIVITY_ALL_GAME = "single activity all game";

    BaseQuickAdapter rvAdapter1;
    BaseQuickAdapter rvAdapter2;

    int listSel = 0;
    GameListItem curGameListItem; //左边选中的GameListItem

    Map<Long, String> saveMap = new HashMap<>();
    List<GameItem> gameHistoryList;
    User user;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.game_fragment, container, false);
        setView(rootView);
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) return;

        StatusBarUtil.setStatusBarFulAlpha(requireActivity());
        BarUtils.setStatusBarVisibility(requireActivity(), true);
        BarUtils.setStatusBarLightMode(requireActivity(), true);
    }


    public void setView(View bindSource) {
        rv1 = bindSource.findViewById(R.id.rv_1);
        rv2 = bindSource.findViewById(R.id.game_list_content);
        gameBanner = bindSource.findViewById(R.id.game_banner);

        setTopPaddingStatusBarHeight(bindSource.findViewById(R.id.rl_top));

        user = DataCenter.getInstance().getUserInfo().getUser();

        setCloseBtn(bindSource);


        showLoadingView();
        gameBanner.setVisibility(View.GONE);

        //获取本地游戏历史记录
        String gameHistory = SPUtils.getInstance("gameHistory").getString("content", "");
        if (!StringUtils.isEmpty(gameHistory)) {
            gameHistoryList = GsonUtil.getObjects(gameHistory, GameItem[].class);
        } else {
            gameHistoryList = new ArrayList<>();
        }

        setGameListRecycleView();
        setGameContentRecycleView();
        //获取游戏列表
        doGetGameListApi();
    }

    private void setCloseBtn(View view) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            boolean isShowClose = getArguments().getBoolean(SINGLE_ACTIVITY_ALL_GAME, false);
            if (isShowClose) {
                ImageView close = view.findViewById(R.id.all_game_title_close);
                close.setVisibility(View.VISIBLE);
                close.setOnClickListener(view1 -> {
                    FragmentActivity fragmentActivity = requireActivity();
                    if (fragmentActivity instanceof AllGameActivity) {
                        fragmentActivity.finish();
                    }
                });
            }
        }
    }

    public void setGameTopBanner(boolean isVisible) {
        if (isVisible) {
            //显示广告
            String bannerContent = SPUtils.getInstance("ad_gameBanner").getString("content", "");
            LogUtils.e("bannerContent: " + bannerContent);
            if (!StringUtils.isEmpty(bannerContent)) {
                List<Advert> bannerAdList = GsonUtil.getObjects(bannerContent, Advert[].class);
                gameBanner.setVisibility(View.VISIBLE);
                gameBanner.setData(getActivity(), bannerAdList);
            }
        } else {
            gameBanner.setVisibility(View.GONE);
        }
    }

    //左边游戏列表
    public void setGameListRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv1.setLayoutManager(layoutManager);
        rv1.setAdapter(rvAdapter1 = new BaseQuickAdapter(R.layout.item_gamelist, new ArrayList<GameListItem>()) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                GameListItem kyGame = (GameListItem) item;

                if (listSel == helper.getLayoutPosition()) {
                    helper.getView(R.id.view_red).setVisibility(View.VISIBLE);
                    ((TextView) helper.getView(R.id.tv_)).setTextColor(Color.BLACK);
                    helper.getView(R.id.rl_root).setBackgroundColor(Color.parseColor("#F5F6F7"));
                } else {
                    helper.getView(R.id.view_red).setVisibility(View.INVISIBLE);
                    ((TextView) helper.getView(R.id.tv_)).setTextColor(Color.parseColor("#a6a5a8"));
                    helper.getView(R.id.rl_root).setBackgroundColor(Color.WHITE);
                }

                helper.setText(R.id.tv_, kyGame.getName());

            }
        });

        rvAdapter1.setOnItemClickListener((adapter, view, position) -> {
            LogUtils.e(position + ",");
            listSel = position;
            rvAdapter1.notifyDataSetChanged();

            curGameListItem = (GameListItem) rvAdapter1.getItem(position);
            if (saveMap.get(curGameListItem.getId()) == null) {
                showLoadingDialog(" ", false, false);
                doGetGameContentApi(curGameListItem.getId());
            } else {
                List<GameColumn> result = GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                if (curGameListItem.getId() == 1) {
                    addGameHistoryToList(result);
                }
                //只有推荐才显示广告
                setGameTopBanner(curGameListItem.getId() == 1 ? true : false);
                rvAdapter2.setNewData(result);
            }
        });
    }


    //右边内容列表
    public void setGameContentRecycleView() {
        rv2.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv2.setLayoutManager(layoutManager);
        rv2.setAdapter(rvAdapter2 = new BaseQuickAdapter(R.layout.item_gamecolumn, new ArrayList<GameColumn>()) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            protected void convert(BaseViewHolder helper, Object item) {

                GameColumn gameColumn = (GameColumn) item;
                helper.setText(R.id.tv_title, gameColumn.getTitle());
                ((TextView) helper.getView(R.id.tv_title)).getPaint().setFakeBoldText(true);

                RecyclerView rv = helper.getView(R.id.rv_gamecolumn);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                rv.setLayoutManager(layoutManager);

                BaseQuickAdapter adapter;
                rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_gamecontenta, gameColumn.getList()) {
                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

                    }

                    @Override
                    protected void convert(BaseViewHolder helper, Object item) {
                        GameItem gameItem = (GameItem) item;
                        helper.setText(R.id.tv_, gameItem.getName());
                        GlideUtils.loadImage(requireContext(), gameItem.getIcon(), helper.getView(R.id.iv_));
                    }
                });

                adapter.setOnItemClickListener((adapter1, view, position) -> {
                    GameItem gameItem = (GameItem) adapter1.getItem(position);
                    if (gameItem != null) {
                        loginGame(gameItem);
                    }
                });
            }
        });

    }

    //获取左边 游戏栏目列表
    public void doGetGameListApi() {
        Api_Config.ins().getKYGameList(new JsonCallback<List<GameListItem>>() {
            @Override
            public void onSuccess(int code, String msg, List<GameListItem> result) {
                if (result != null) LogUtils.e("getKYGameList : " + new Gson().toJson(result));
                if (code == 0 && result != null && result.size() > 0) {
                    curGameListItem = result.get(0);
                    rvAdapter1.setNewData(result);
                    if (result.size() > 0) {
                        doGetGameContentApi(result.get(0).getId());
                    }
                } else {
                    hideLoadingView();
                    showEmptyViewWithButton(msg, getString(R.string.clickRetry), view -> {
                        showLoadingView();
                        doGetGameListApi();
                    });
                }
            }
        });
    }

    //获取右边 游戏栏目
    public void doGetGameContentApi(long parentId) {
        Api_Config.ins().getKYGameDetailList(parentId, new JsonCallback<List<GameColumn>>() {
            @Override
            public void onSuccess(int code, String msg, List<GameColumn> result) {
                if (result != null) LogUtils.e("getKYGameList : " + new Gson().toJson(result));
                hideLoadingView();
                hideLoadingDialog();
                if (code == 0) {
                    if (parentId == 1) {
                        //如果有本地历史记录 且是推荐栏目 则加到推荐栏目中
                        addGameHistoryToList(result);
                    }
                    saveMap.put(parentId, new Gson().toJson(result));
                    //只有推荐才显示广告
                    setGameTopBanner(parentId == 1 ? true : false);
                    rvAdapter2.setNewData(result);
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    public void addGameHistoryToList(List<GameColumn> result) {
        if (result.size() == 0) {
            if (gameHistoryList.size() > 0) {
                //如果有本地历史记录 且是推荐栏目 则加到推荐栏目中
                GameColumn gameColumn = new GameColumn();
                gameColumn.setTitle(getString(R.string.historyRecord));
                gameColumn.setList(gameHistoryList);
                result.add(0, gameColumn);
            }
        } else {
            if (result.get(0).getTitle().equals(getString(R.string.historyRecord))) {
                if (gameHistoryList.size() > 0) {
                    GameColumn gameColumn = result.get(0);
                    gameColumn.getList().clear();
                    gameColumn.getList().addAll(gameHistoryList);
                } else {
                    result.remove(0);
                }
            } else {
                if (gameHistoryList.size() > 0) {
                    //如果有本地历史记录 且是推荐栏目 则加到推荐栏目中
                    GameColumn gameColumn = new GameColumn();
                    gameColumn.setTitle(getString(R.string.historyRecord));
                    gameColumn.setList(gameHistoryList);
                    result.add(0, gameColumn);
                }
            }
        }
    }

    //添加历史记录到本地
    public void addHistory(GameItem gameItem) {
        if (gameHistoryList == null) return;

        //如果列表中有此记录 则删掉
        int index = -1;
        for (int i = 0; i < gameHistoryList.size(); i++) {
            if (gameHistoryList.get(i).getGameId().equals(gameItem.getGameId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            gameHistoryList.remove(index);
        }

        gameHistoryList.add(0, gameItem);

        if (gameHistoryList.size() > 6) {
            gameHistoryList.remove(6);
        }
        SPUtils.getInstance("gameHistory").put("content", new Gson().toJson(gameHistoryList));
    }

    //登录游戏
    public void loginGame(GameItem gameItem) {
        showLoadingDialog(getString(R.string.baseLoading), false, false);
        switch (gameItem.getType()) {
            case 0: ////0：老朱；1：开元 2：AG
                Api_Pay.ins().getGame(user.getUid() + "", gameItem.getName(),
                        gameItem.getGameId(), 2, new JsonCallback<String>() {
                            @Override
                            public void onSuccess(int code, String msg, String data) {
                                if (data != null) LogUtils.e(data);
                                hideLoadingDialog();
                                if (code == 0) {
                                    GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), data);
                                    addHistory(gameItem);
                                    if (curGameListItem.getId() == 1) {
                                        //如果当前是推荐列表 则刷新右边列表
                                        List<GameColumn> gameColumnList =
                                                GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                                        addGameHistoryToList(gameColumnList);
                                        rvAdapter2.setNewData(gameColumnList);
                                    }
                                } else {
                                    ToastUtils.showShort(msg);
                                }
                            }
                        });
                break;

            case 1://1 开元
                Api_KyGame.ins().login(gameItem.getGameId(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null)
                            LogUtils.e("getKYGameList : " + new Gson().toJson(result));
                        hideLoadingDialog();
                        if (code == 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String url = jsonObject.optString("url");
                                LogUtils.e("url: " + url);
                                GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                                addHistory(gameItem);
                                if (curGameListItem.getId() == 1) {
                                    //如果当前是推荐列表 则刷新右边列表
                                    List<GameColumn> gameColumnList =
                                            GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                                    addGameHistoryToList(gameColumnList);
                                    rvAdapter2.setNewData(gameColumnList);
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShort(e.getMessage());
                            }
                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
                break;

            case 2: //2 AG
                Api_AgGame.ins().forwardGame(gameItem.getGameId(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        try {
                            if (result != null)
                                LogUtils.e("getAgGameList : " + new Gson().toJson(result));
                            hideLoadingDialog();
                            if (code == 0) {
                                JSONObject jsonObject = new JSONObject(result);
                                String url = jsonObject.optString("param");
                                LogUtils.e("param: " + url);
                                GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                                addHistory(gameItem);
                                if (curGameListItem.getId() == 1) {
                                    //如果当前是推荐列表 则刷新右边列表
                                    List<GameColumn> gameColumnList =
                                            GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                                    addGameHistoryToList(gameColumnList);
                                    rvAdapter2.setNewData(gameColumnList);
                                }
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        } catch (JSONException e) {
                            ToastUtils.showShort(e.getMessage());
                        }

                    }
                });
                break;

            case 3:
                Api_Cp.ins().geth5url(gameItem.getGameId(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null) LogUtils.e("geth5url : " + new Gson().toJson(result));
                        hideLoadingDialog();
                        if (code == 0) {
                            LogUtils.e("url: " + result);
                            GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), result);
                            addHistory(gameItem);
                            if (curGameListItem.getId() == 1) {
                                //如果当前是推荐列表 则刷新右边列表
                                List<GameColumn> gameColumnList =
                                        GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                                addGameHistoryToList(gameColumnList);
                                rvAdapter2.setNewData(gameColumnList);
                            }
                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
                break;
            case 4:
                Api_FwGame.ins().login(gameItem.getGameId(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null)
                            LogUtils.e("getKYGameList : " + new Gson().toJson(result));
                        hideLoadingDialog();
                        if (code == 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String url = jsonObject.optString("url");
                                LogUtils.e("url: " + url);
                                GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                                addHistory(gameItem);
                                if (curGameListItem.getId() == 1) {
                                    //如果当前是推荐列表 则刷新右边列表
                                    List<GameColumn> gameColumnList =
                                            GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                                    addGameHistoryToList(gameColumnList);
                                    rvAdapter2.setNewData(gameColumnList);
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShort(e.getMessage());
                            }

                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
                break;

            case 5:
            case 6: //bg
                Api_FwGame.ins().loginBg(gameItem.getGameId(), user.getUid() + "", new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        hideLoadingDialog();
                        try {
                            if (code == 0 && !TextUtils.isEmpty(result)) {
                                JSONObject jsonObject = new JSONObject(result);
                                String url = jsonObject.optString("url");
                                LogUtils.e("url: " + url);
                                GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                                addHistory(gameItem);
                                if (curGameListItem.getId() == 1) {
                                    //如果当前是推荐列表 则刷新右边列表
                                    List<GameColumn> gameColumnList =
                                            GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                                    addGameHistoryToList(gameColumnList);
                                    rvAdapter2.setNewData(gameColumnList);
                                }
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        } catch (JSONException e) {
                            ToastUtils.showShort(e.getMessage());
                        }
                    }
                });
                break;

            case 7://CMD
                Api_TYGame.ins().forwardGame(gameItem.getGameId(), user.getUid() + "",
                        new JsonCallback<String>() {
                            @Override
                            public void onSuccess(int code, String msg, String result) {
                                if (result != null)
                                    LogUtils.e("getBGGameList : " + new Gson().toJson(result));
                                hideLoadingDialog();
                                if (code == 0) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        String url = jsonObject.optString("url");
                                        LogUtils.e("url: " + url);
                                        GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                                        addHistory(gameItem);
                                        if (curGameListItem.getId() == 1) {
                                            //如果当前是推荐列表 则刷新右边列表
                                            List<GameColumn> gameColumnList =
                                                    GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                                            addGameHistoryToList(gameColumnList);
                                            rvAdapter2.setNewData(gameColumnList);
                                        }
                                    } catch (JSONException e) {
                                        ToastUtils.showShort(e.getMessage());
                                    }

                                } else {
                                    ToastUtils.showShort(msg);
                                }
                            }
                        });
                break;

            case 12:
                Api_TYGame.ins().sabaForWard(gameItem.getGameId(), user.getUid() + "",
                        new JsonCallback<String>() {
                            @Override
                            public void onSuccess(int code, String msg, String result) {
                                hideLoadingDialog();
                                if (code == 0) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        String url = jsonObject.optString("url");
                                        LogUtils.e("url: " + url);
                                        GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                                        addHistory(gameItem);
                                        if (curGameListItem.getId() == 1) {
                                            //如果当前是推荐列表 则刷新右边列表
                                            List<GameColumn> gameColumnList =
                                                    GsonUtil.getObjects(saveMap.get(curGameListItem.getId()), GameColumn[].class);
                                            addGameHistoryToList(gameColumnList);
                                            rvAdapter2.setNewData(gameColumnList);
                                        }
                                    } catch (JSONException e) {
                                        ToastUtils.showShort(e.getMessage());
                                    }
                                } else {
                                    ToastUtils.showShort(msg);
                                }
                            }
                        });
                break;

            default:
                hideLoadingDialog();
                ToastUtils.showShort(getString(R.string.comingSoon));
                break;
        }
    }
}

