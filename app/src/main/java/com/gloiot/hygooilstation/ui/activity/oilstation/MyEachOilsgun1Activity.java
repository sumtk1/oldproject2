package com.gloiot.hygooilstation.ui.activity.oilstation;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.server.network.RequestAction;
import com.gloiot.hygooilstation.ui.activity.BaseActivity;
import com.gloiot.hygooilstation.ui.adapter.MyOilgun1Adapter;
import com.gloiot.hygooilstation.ui.widget.MyDialogBuilder;
import com.gloiot.hygooilstation.ui.widget.swipe.OnItemClickListener;
import com.gloiot.hygooilstation.utils.CommonUtlis;
import com.gloiot.hygooilstation.utils.ConstantUtlis;
import com.gloiot.hygooilstation.utils.MToast;
import com.gloiot.hygooilstation.utils.SharedPreferencesUtils;
import com.gloiot.hygooilstation.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 每个油品型号对应的油枪
 */
public class MyEachOilsgun1Activity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_oilsgun_top, rl_oilsgun_addOilgun;
    private TextView tv_oilsgun_oilstype, tv_nodata;
    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private List<String[]> list = new ArrayList<>();
    private String stationId, oilsType, oilsId;
    private int deletePosition = -1;//记录删除的位置，初始化为-1
    private MyOilgun1Adapter mMenuAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        requestHandleArrayList.add(requestAction.getOilsguns(this, oilsId, stationId));
    }

    @Override
    public int initResource() {
        return R.layout.activity_my_each_oilsgun1;
    }

    @Override
    public void initComponent() {
        rl_oilsgun_top = (RelativeLayout) findViewById(R.id.rl_oilsgun_top);
        rl_oilsgun_addOilgun = (RelativeLayout) findViewById(R.id.rl_oilsgun_addOilgun);
        tv_oilsgun_oilstype = (TextView) findViewById(R.id.tv_oilsgun_oilstype);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
    }

    @Override
    public void initData() {
        CommonUtlis.setTitleBar(this, true, "我的油枪", "");
        stationId = SharedPreferencesUtils.getString(mContext, ConstantUtlis.SP_STATIONID, "");
        Intent intent = getIntent();
        oilsType = intent.getStringExtra("oilsType");
        oilsId = intent.getStringExtra("oilsId");
        tv_oilsgun_oilstype.setText(oilsType);
        if (oilsType.equals("0#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.orange_F29305);
        } else if (oilsType.equals("92#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.green_6ED12B);
        } else if (oilsType.equals("93#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.green_20DC91);
        } else if (oilsType.equals("95#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.purple_D824CF);
        } else if (oilsType.equals("97#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.blue_176BEA);
        } else if (oilsType.equals("98#")) {
            rl_oilsgun_top.setBackgroundResource(R.color.purple_992FDF);
        } else {
            rl_oilsgun_top.setBackgroundResource(R.color.blue_176BEA);
        }
        rl_oilsgun_addOilgun.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_oilsgun_addOilgun:
                Intent intent = new Intent(this, AddOilgunActivity.class);
                intent.putExtra("oilsId", oilsId);
                intent.putExtra("oilsType", oilsType);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void processData() {

        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mSwipeMenuRecyclerView.addItemDecoration(new ListViewDecoration(mContext));// 添加分割线。

        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);

        mMenuAdapter = new MyOilgun1Adapter(oilsType, list);
        mMenuAdapter.setOnItemClickListener(onItemClickListener);
        mSwipeMenuRecyclerView.setAdapter(mMenuAdapter);

    }

    /**
     * 条目点击监听
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
//            Toast.makeText(mContext, "我是第" + position + "条。", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

//            int width = getResources().getDimensionPixelSize(R.dimen.item_height);
            int width =170;//单位为px,没有适配性


            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.color.orange_FF690C)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

            }
        }
    };
    /**
     * 菜单点击监听
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
//                list.remove(adapterPosition);
//                mMenuAdapter.notifyItemRemoved(adapterPosition);

                final MyDialogBuilder myDialogBuilder;
                final int p = adapterPosition;
                myDialogBuilder = MyDialogBuilder.getInstance(mContext);
                myDialogBuilder.withIcon(R.mipmap.iconfont_gantanhao)
                        .withContene("确定要删除这个油枪吗?")
                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                        .setBtnClick("确定", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismissNoAnimator();
                                deletePosition = p;

                                requestHandleArrayList.add(requestAction.deleteOilsguns(MyEachOilsgun1Activity.this, list.get(p)[0]));
                            }
                        })
                        .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBuilder.dismiss();
                            }
                        })
                        .show();
            }
        }
    };


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case RequestAction.TAG_MYOILSGUNS:

//                Log.e("当前油品对应的油枪", response.toString());

                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[2];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = jsonObject.getString("id");
                        a[1] = jsonObject.getString("油枪号");
                        list.add(a);
                    }
                    tv_nodata.setText("");
                    processData();
                } else {
                    tv_nodata.setText("无数据");
                }

                break;

            case RequestAction.TAG_DELETEOILSGUN:

                MToast.showToast(mContext, "删除油枪成功");//然后需要刷新一遍数据,不需要再次调接口
                if (deletePosition != -1) {//为了保险，其实只要删除成功，这里肯定不等于-1
                    list.remove(deletePosition);
                    mMenuAdapter.notifyItemRemoved(deletePosition);
                }
                deletePosition = -1;
                break;
            default:
                break;
        }
    }

}
