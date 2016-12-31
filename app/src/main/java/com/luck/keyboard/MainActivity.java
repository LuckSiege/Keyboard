package com.luck.keyboard;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.luck.keyboard.adapter.MyFragmentPagerAdapter;
import com.luck.keyboard.emoji.WeiBoContentTextUtil;
import com.luck.keyboard.entity.Emoji;
import com.luck.keyboard.fragment.EmojiTabAFragment;
import com.luck.keyboard.fragment.EmojiTabBFragment;
import com.luck.keyboard.util.DisplayUtils;
import com.luck.keyboard.util.InputMethodUtils;
import com.luck.keyboard.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, EmojiTabAFragment.OnEmojiClickListener, EmojiTabBFragment.OnEmojiClickListener {
    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.emoji)
    ImageView emoji;
    @BindView(R.id.ll_face_container)
    LinearLayout ll_face_container;
    @BindView(R.id.vPager)
    ViewPager vPager;
    @BindView(R.id.rabs)
    RadioGroup rabs;
    @BindView(R.id.rab1)
    RadioButton rab1;
    @BindView(R.id.rab2)
    RadioButton rab2;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.ll_emoji)
    LinearLayout ll_emoji;
    @BindView(R.id.ll_photo)
    LinearLayout ll_photo;
    private List<Fragment> fragments = new ArrayList<>();
    private boolean keyBoard = false;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DisplayUtils.init(this);
        InputMethodUtils.detectKeyboard(this);
        int keyboardHeight = (int) SharedPreferencesUtil.getData(MainActivity.this, "kh", 0);
        if (keyboardHeight > 0) {
            updateEmotionPanelHeight(keyboardHeight);
        }
        rabs.setOnCheckedChangeListener(this);
        initEmoticonViewPage();
    }


    /**
     * 初始化Fragment
     */
    private void initEmoticonViewPage() {
        fragments.add(new EmojiTabAFragment());
        fragments.add(new EmojiTabBFragment());
        vPager.setOffscreenPageLimit(2);
        vPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rab1.setChecked(true);
                        break;
                    case 1:
                        rab2.setChecked(true);
                        break;
                }

                vPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vPager.setCurrentItem(1);
    }

    @OnClick({R.id.ll_emoji, R.id.ll_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_photo:

                break;
            case R.id.ll_emoji:
                // 显示表情
                if (keyBoard) {
                    InputMethodUtils.toggleSoftInput(getCurrentFocus());
                    ll_face_container.postDelayed(mHideEmotionPanelTask, 500);
                } else {
                    showEmotionPanel();
                }
                break;
        }
    }


    /**
     * 是否显示表情
     *
     * @return
     */
    public boolean isEmotionPanelShowing() {
        boolean b = ll_face_container.getVisibility() == View.VISIBLE;
        return b;
    }

    /**
     * 隐藏表情
     */
    public void hideEmotionPanel() {
        if (ll_face_container.getVisibility() != View.GONE) {
            ll_face_container.setVisibility(View.GONE);
            emoji.setImageResource(R.mipmap.biaoqin_1x);
            keyBoard = false;
            InputMethodUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    /**
     * 更新表情栏高度和键盘高度一样
     */
    public void updateEmotionPanelHeight(int keyboardHeight) {
        ViewGroup.LayoutParams params = ll_face_container.getLayoutParams();
        if (params != null && params.height != keyboardHeight) {
            // 保存键盘高度
            SharedPreferencesUtil.saveData(MainActivity.this, "kh", keyboardHeight);
            params.height = keyboardHeight;
            ll_face_container.setLayoutParams(params);
        }
    }

    /**
     * 显示表情
     */
    public void showEmotionPanel() {
        emoji.setImageResource(R.mipmap.keyboard_1x);
        keyBoard = true;
        ll_face_container.removeCallbacks(mHideEmotionPanelTask);
        InputMethodUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        ll_face_container.setVisibility(View.VISIBLE);
        InputMethodUtils.hideKeyboard(getCurrentFocus());
    }

    private Runnable mHideEmotionPanelTask = new Runnable() {
        @Override
        public void run() {
            hideEmotionPanel();
        }
    };


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rab1:
                index = 0;
                break;
            case R.id.rab2:
                index = 1;
                break;
        }
        vPager.setCurrentItem(index);
    }


    @Override
    public void onEmojiDelete() {
        String text = et_content.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                et_content.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                displayTextView();
                return;
            }
            et_content.getText().delete(index, text.length());
            displayTextView();
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        et_content.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            int index = et_content.getSelectionStart();
            Editable editable = et_content.getEditableText();
            if (index < 0) {
                editable.append(emoji.getContent());
            } else {
                editable.insert(index, emoji.getContent());
            }
        }
        displayTextView();
    }

    private void displayTextView() {
        try {
            et_content.setText(WeiBoContentTextUtil.getWeiBoContent(et_content.getText().toString(), MainActivity.this, et_content));
            et_content.setSelection(et_content.getText().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
