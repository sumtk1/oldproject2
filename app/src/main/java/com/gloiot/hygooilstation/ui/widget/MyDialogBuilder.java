package com.gloiot.hygooilstation.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygooilstation.R;
import com.gloiot.hygooilstation.utils.CuttedStr_two;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by admin on 2016/8/10.
 */
public class MyDialogBuilder extends Dialog  implements DialogInterface{
    public static final int SlideTop = 0;
    public static final int SlideTopDismiss = 1;

    public static final int BtnCancel = 0;
    public static final int BtnNormal = 1;

    public static final int EtText = 0;
    public static final int EtPwd = 1;
    public static final int EtNum = 2;
    public static final int EtDecimal_Tow = 3;

    public volatile static MyDialogBuilder instance;
    private Context context;
    private int wHeight, width; // 屏幕高度宽度
    private int startEffects, dismissEffects; // 开始动画效果，消失动画效果
    private View mDialogView, customView;
    private RelativeLayout mydialog_main, mydialog;
    private TextView mydialog_title, mydialog_content;
    private ImageView mydialog_icon;
    private LinearLayout mydialog_edittextlist, mydialog_buttonlist;
    private FrameLayout mydialog_custompanel;
    private View listviewSingle;
    private int listviewSingleItems;
    public MyDialogBuilder(Context context) {
        super(context);
        this.context = context;
    }
    public MyDialogBuilder(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    public static MyDialogBuilder getInstance(Context context) {
//        if (instance == null) {
//            synchronized (MyDialogBuilder.class) {
//                if (instance == null) {
        instance = new MyDialogBuilder(context, R.style.dialog_untran);
//                }
//            }
//        }
        return instance;
    }


    public void init() {
        wHeight = getScreenHeight();
        width = getScreenWidth();
        mDialogView = View.inflate(context, R.layout.mydialog, null);
        mydialog_main = (RelativeLayout) mDialogView.findViewById(R.id.mydialog_main);
        mydialog = (RelativeLayout) mDialogView.findViewById(R.id.mydialog);
        mydialog_icon = (ImageView) mDialogView.findViewById(R.id.mydialog_icon);
        mydialog_title = (TextView) mDialogView.findViewById(R.id.mydialog_title);
        mydialog_content = (TextView) mDialogView.findViewById(R.id.mydialog_content);
        mydialog_buttonlist = (LinearLayout) mDialogView.findViewById(R.id.mydialog_buttonlist);
        mydialog_edittextlist = (LinearLayout) mDialogView.findViewById(R.id.mydialog_edittextlist);
        mydialog_custompanel = (FrameLayout) mDialogView.findViewById(R.id.mydialog_custompanel);
        setContentView(mDialogView);
        mydialog_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                startAnimation();
            }
        });
    }

    /**
     * 获得dialogview
     * @return
     */
    public View getDialogView() {
        return mDialogView;
    }

    /**
     * 设置图标
     *
     * @param drawableResId
     * @return
     */
    public MyDialogBuilder withIcon(int drawableResId) {
        mydialog_icon.setVisibility(View.VISIBLE);
        mydialog_icon.setImageResource(drawableResId);
        return this;
    }
    public MyDialogBuilder withIcon(String imgUrl, int round) {
        mydialog_icon.setVisibility(View.VISIBLE);
//        CommonUtlis.setDisplayImageOptions(mydialog_icon,imgUrl,round,0);
        return this;
    }

    /**
     * 设置标题
     *
     * @param dialogTitle
     * @return
     */
    public MyDialogBuilder withTitie(String dialogTitle) {
        mydialog_title.setVisibility(View.VISIBLE);
        mydialog_title.setText(dialogTitle);
        return this;
    }

    /**
     * 设置内容
     *
     * @param dialogContent
     * @return
     */
    public MyDialogBuilder withContene(String dialogContent) {
        mydialog_content.setVisibility(View.VISIBLE);
        mydialog_content.setText(dialogContent);
        return this;
    }

    /**
     * 定制layout
     * @param resId 定制layout的ID
     * @return
     */
    public MyDialogBuilder setCustomView(int resId) {
        customView = View.inflate(context, resId, null);
        mydialog_custompanel.addView(customView);
        return this;
    }

    /**
     * 获得定制layout
     * @return
     */
    public View getCustomView() {
        return customView;
    }

    /**
     * 设置动画效果
     *
     * @param startEffects   开始动画
     * @param dismissEffects 结束动画
     * @return
     */
    public MyDialogBuilder withEffects(int startEffects, int dismissEffects) {
        this.startEffects = startEffects;
        this.dismissEffects = dismissEffects;
        return this;
    }


    /**
     * 动态添加按钮
     *
     * @param btnText  按钮字体
     * @param btnStyle 按钮样式
     * @param click    点击事件
     * @return
     */
    public MyDialogBuilder setBtnClick(String btnText, int btnStyle, View.OnClickListener click) {
        int textColour = 0;
        Button btn = new Button(context);
        mydialog_buttonlist.addView(btn);
        btn.setText(btnText);
        btn.setOnClickListener(click);
        // 设置按钮间距
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(40, 0, 40, 30);
        btn.setLayoutParams(lp);
        switch (btnStyle) {
            case BtnCancel:
                btn.setBackgroundResource(R.drawable.dialog_btn_cancel);
                break;
            case BtnNormal:
                btn.setBackgroundResource(R.drawable.dialog_btn_normal);
                break;
            default:
                btn.setBackgroundResource(R.drawable.dialog_btn_normal);
                break;
        }
        textColour = Color.parseColor("#ffffff");
        btn.setTextColor(textColour);
        return this;
    }


    /**
     * 动态添加输入框
     * @param etId
     * @param etHint
     * @param etInputType
     * @return
     */
    public MyDialogBuilder setEtClick(int etId, String etText, String etHint, int etInputType) {
        EditText et = new EditText(context);
        mydialog_buttonlist.addView(et);
        et.setId(100 + etId);
        et.setHint(etHint);
        et.setHintTextColor(Color.parseColor("#999999"));
        switch (etInputType) {
            case EtText:
                etInputType = InputType.TYPE_CLASS_TEXT;
                break;
            case EtPwd:
                etInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;
            case EtNum:
//                etInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER;
                et.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                break;
            case EtDecimal_Tow:
                CuttedStr_two.setPricePoint(et);
                et.setText("0.00");
                etInputType = InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER;
                break;
            default:
                break;
        }
        if (!TextUtils.isEmpty(etText)){
            et.setText(etText);
            et.setSelection(etText.length());
        }
//        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        et.setPadding(30, 20, 30, 20);
        et.setBackgroundResource(R.drawable.dialog_et);
        et.setInputType(etInputType);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.setMargins(40, 0, 40, 30);
        et.setLayoutParams(lp);
        return this;
    }


    /**
     * 单选listview
     * @param context
     * @param conten
     * @param position
     * @return
     */
    public MyDialogBuilder setListViewSingle(Context context, String[] conten, int position) {
        listviewSingle = View.inflate(context, R.layout.dialog_son_listview, null);
        mydialog_custompanel.addView(listviewSingle);
        ListView listView = (ListView) listviewSingle.findViewById(R.id.dialog_listview);
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice, conten));
        listView.setItemChecked(position, true);
        listviewSingleItems = position;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listviewSingleItems = position;
            }
        });
        setMaxHeight(listviewSingle);
        return this;
    }

    /**
     * 返回单选listview 选择位置
     * @return
     */
    public int getlistviewSingleItems() {
        return listviewSingleItems;
    }


    /**
     * 设置最大高度
     * @return
     */
    public MyDialogBuilder setMaxHeight() {
        ViewTreeObserver vto = mydialog_content.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mydialog_content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int cHeight = mydialog_content.getHeight();
                ViewGroup.LayoutParams lp = mydialog_content.getLayoutParams();
                if (cHeight >= wHeight * 1 / 2) {
                    lp.height = wHeight * 1 / 2;
                }
                mydialog_content.setLayoutParams(lp);
            }
        });
        return this;
    }

    /**
     * 设置最大高度
     * @return
     */
    public MyDialogBuilder setMaxHeight(final View view) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int cHeight = view.getHeight();
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (cHeight >= wHeight * 1 / 3) {
                    lp.height = wHeight * 1 / 3;
                }
                view.setLayoutParams(lp);
            }
        });
        return this;
    }


    /**
     * 显示Dialog
     */
    @Override
    public void show() {
        super.show();
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        new BaseEffects().start(mydialog_main, startEffects);
    }

    /**
     * 消失动画
     * @param mAnimatorSet
     */
    private void dismissAnimation(BaseEffects mAnimatorSet) {
        mAnimatorSet.start(mydialog_main, dismissEffects);
    }


    /**
     * Dialog消失
     */
    @Override
    public void dismiss() {
        instance = null;
        BaseEffects mAnimatorSet = new BaseEffects();
        dismissAnimation(mAnimatorSet);
        if (mAnimatorSet.getAnimatorSet() != null) {
            // 监听动画执行
            mAnimatorSet.getAnimatorSet().addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    superDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            superDismiss();
        }
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) this.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow(mydialog_content.getWindowToken(), 0);
        }
    }

    /**
     * Dialog消失无动画
     */
    public void dismissNoAnimator(){
        instance = null;
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) this.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow(mydialog_content.getWindowToken(), 0);
        }
        superDismiss();
    }

    /**
     * 继承父类消失Dialog
     */
    public void superDismiss() {
        super.dismiss();
    }


    public class BaseEffects {
        public final int DURATION = 1 * 400; // 动画执行时间
        private AnimatorSet mAnimatorSet;

        {
            mAnimatorSet = new AnimatorSet();
        }

        protected void setupAnimation(View view, int showtype) {
            switch (showtype) {
                case SlideTop:
                    mAnimatorSet = new AnimatorSet();
                    mAnimatorSet.playTogether(
                            ObjectAnimator.ofFloat(view, "translationY", -wHeight * 1 / 2, 0).setDuration(DURATION),
                            ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(DURATION * 3 / 2));
                    break;
                case SlideTopDismiss:
                    mAnimatorSet = new AnimatorSet();
                    mAnimatorSet.playTogether(
                            ObjectAnimator.ofFloat(view, "translationY", 0, wHeight * 1 / 2).setDuration(DURATION),
                            ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(DURATION));
                    break;
                default:
                    break;
            }
        }

        // 执行动画
        public void start(View view, int showtype) {
            reset(view);
            setupAnimation(view, showtype);
            mAnimatorSet.start();
        }

        public void reset(View view) {
            ViewHelper.setAlpha(view, 1);
            ViewHelper.setScaleX(view, 1);
            ViewHelper.setScaleY(view, 1);
            ViewHelper.setTranslationX(view, 0);
            ViewHelper.setTranslationY(view, 0);
            ViewHelper.setRotation(view, 0);
            ViewHelper.setRotationY(view, 0);
            ViewHelper.setRotationX(view, 0);
            ViewHelper.setPivotX(view, view.getMeasuredWidth() / 2.0f);
            ViewHelper.setPivotY(view, view.getMeasuredHeight() / 2.0f);
        }

        public AnimatorSet getAnimatorSet() {
            return mAnimatorSet;
        }
    }

    // 获得屏幕高度
    public int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    // 获得屏幕宽度
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
