package com.gloiot.hygooilstation.ui.activity.uiview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gloiot.hygooilstation.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 顶部弹出菜单
 */
public class TopMenuWindow extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "TopMenuWindow";

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动TopMenuWindow的Intent
     * @param context
     * @param names
     * @return
     */
    public static Intent createIntent(Context context, String[] names) {
        return createIntent(context, names, new ArrayList<Integer>());


    }

    /**启动TopMenuWindow的Intent
     * @param context
     * @param nameList
     * @return
     */
    public static Intent createIntent(Context context, ArrayList<String> nameList) {
        return createIntent(context, nameList, null);
    }

    /**启动TopMenuWindow的Intent
     * @param context
     * @param names
     * @param intentCodes
     * @return
     */
    public static Intent createIntent(Context context, String[] names, int[] intentCodes) {
        return new Intent(context, TopMenuWindow.class).
                putExtra(INTENT_NAMES, names).
                putExtra(INTENT_INTENTCODES, intentCodes);
    }

    /**启动TopMenuWindow的Intent
     * @param context
     * @param names
     * @param intentCodeList
     * @return
     */
    public static Intent createIntent(Context context, String[] names, ArrayList<Integer> intentCodeList) {
        return new Intent(context, TopMenuWindow.class).
                putExtra(INTENT_NAMES, names).
                putExtra(INTENT_INTENTCODES, intentCodeList);


    }

    /**启动TopMenuWindow的Intent
     * @param context
     * @param nameList
     * @param intentCodeList
     * @return
     */
    public static Intent createIntent(Context context,
                                      ArrayList<String> nameList, ArrayList<Integer> intentCodeList) {
        return new Intent(context, TopMenuWindow.class).
                putStringArrayListExtra(INTENT_NAMES, nameList).
                putIntegerArrayListExtra(INTENT_INTENTCODES, intentCodeList);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    private boolean isAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_menu_window);

        isAlive = true;

        init();
    }

    public static final String INTENT_NAMES = "INTENT_NAMES";
    public static final String INTENT_INTENTCODES = "INTENT_INTENTCODES";

    public static final String RESULT_NAME = "RESULT_NAME";
    public static final String RESULT_POSITION = "RESULT_POSITION";
    public static final String RESULT_INTENT_CODE = "RESULT_INTENT_CODE";

    private ArrayList<String> nameList = null;
    private ArrayList<Integer> intentCodeList = null;
    private ArrayAdapter<String> adapter;
    private ListView lvTopMenu;
    private View llTopMenuWindowBg;


    private void init() {

        llTopMenuWindowBg = findViewById(R.id.llTopMenuWindowBg);
        llTopMenuWindowBg.setOnClickListener(this);

        Intent intent = getIntent();

        int[] intentCodes = intent.getIntArrayExtra(INTENT_INTENTCODES);
        if (intentCodes == null || intentCodes.length <= 0) {
            intentCodeList = intent.getIntegerArrayListExtra(INTENT_INTENTCODES);
        } else {
            intentCodeList = new ArrayList<Integer>();
            for (int code : intentCodes) {
                intentCodeList.add(code);
            }
        }

        String[] menuItems = intent.getStringArrayExtra(INTENT_NAMES);
        if (menuItems == null || menuItems.length <= 0) {
            nameList = intent.getStringArrayListExtra(INTENT_NAMES);
        } else {
            nameList = new ArrayList<String>(Arrays.asList(menuItems));
        }

        if (nameList == null || nameList.size() <= 0) {
            Log.e(TAG, "init   nameList == null || nameList.size() <= 0 >> finish();return;");
            finish();
            return;
        }

        adapter = new ArrayAdapter<String>(this, R.layout.top_menu_list_item, R.id.tvTopMenuListItem, nameList);

        lvTopMenu = (ListView) findViewById(R.id.lvTopMenuWindowMenu);
        lvTopMenu.setAdapter(adapter);
        lvTopMenu.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llTopMenuWindowBg) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent().putExtra(RESULT_POSITION, position);
        if (intentCodeList != null && intentCodeList.size() > position) {
            intent.putExtra(RESULT_INTENT_CODE, intentCodeList.get(position));
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        if (isAlive == false) {
            Log.e(TAG, "finish  isAlive == false >> return;");
            return;
        }

        llTopMenuWindowBg.setEnabled(false);

        super.finish();
//        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        super.onDestroy();
    }
}
