package com.luck.keyboard.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.luck.keyboard.MainActivity;

/**
 * @author luck
 */
public class InputMethodUtils {

    /**
     * 监听软键盘弹出/关闭接口
     */
    public interface OnKeyboardEventListener {
        /**
         * 软键盘弹出
         */
        public void onSoftKeyboardOpened();

        /**
         * 软键盘关闭
         */
        public void onSoftKeyboardClosed();
    }

    private static boolean sIsKeyboardShowing;
    private static int sKeyBoardHeight = DisplayUtils.getDefaultKeyboardHeight();

    public static boolean isKeyboardShowing() {
        return sIsKeyboardShowing;
    }

    public static void setKeyboardShowing(boolean show) {
        sIsKeyboardShowing = show;
    }

    public static int getKeyBoardHeight() {
        return sKeyBoardHeight;
    }

    public static void setKeyBoardHeight(int keyBoardHeight) {
        sKeyBoardHeight = keyBoardHeight;
    }

    /**
     * 隐藏输入法
     *
     * @param currentFocusView 当前焦点view
     */
    public static void hideKeyboard(View currentFocusView) {
        if (currentFocusView != null) {
            IBinder token = currentFocusView.getWindowToken();
            if (token != null) {
                InputMethodManager im = (InputMethodManager) currentFocusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, 0);
            }
        }
    }

    /**
     * 开关输入法
     *
     * @param currentFocusView 当前焦点view
     */
    public static void toggleSoftInput(View currentFocusView) {
        InputMethodManager imm = (InputMethodManager) currentFocusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(currentFocusView, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 是否点击软键盘和输入法外面区域
     *
     * @param activity 当前activity
     * @param touchY   点击y坐标
     */
    public static boolean isTouchKeyboardOutside(Activity activity, int touchY) {
        View editText = activity.getCurrentFocus();
        if (editText == null) {
            return false;
        }
        int[] location = new int[2];
        editText.getLocationOnScreen(location);
        int editY = location[1] - DisplayUtils.getStatusBarHeight();
        int offset = touchY - editY;
        if (offset > 0 && offset < editText.getMeasuredHeight()) {
            return false;
        }
        return true;
    }

    public static void enableCloseKeyboardOnTouchOutside(Activity activity) {
        CloseKeyboardOnOutsideContainer frameLayout = new CloseKeyboardOnOutsideContainer(activity);
        activity.addContentView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public static void detectKeyboard(MainActivity activity) {
        detectKeyboard(activity, null);
    }

    /**
     * 只有当Activity的windowSoftInputMode设置为adjustResize时才有效
     */
    public static void detectKeyboard(final MainActivity activity, final OnKeyboardEventListener listener) {
        final View activityRootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        if (activityRootView != null) {
            ViewTreeObserver viewTreeObserver = activityRootView.getViewTreeObserver();
            if (viewTreeObserver != null) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final Rect r = new Rect();
                        activityRootView.getWindowVisibleDisplayFrame(r);
                        int heightDiff = DisplayUtils.getScreenHeight() - (r.bottom - r.top);
                        boolean show = heightDiff >= sKeyBoardHeight / 2;
                        if (show ^ sIsKeyboardShowing) {
                            sIsKeyboardShowing = show;
                            if (show) {
                                int keyboardHeight = heightDiff - DisplayUtils.getStatusBarHeight();
                                if (keyboardHeight != sKeyBoardHeight) {
                                    sKeyBoardHeight = keyboardHeight;
                                    activity.updateEmotionPanelHeight(keyboardHeight);
                                }
                                if (listener != null) {
                                    listener.onSoftKeyboardOpened();
                                }
                            } else {
                                if (listener != null) {
                                    listener.onSoftKeyboardClosed();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public static void updateSoftInputMethod(Activity activity, int softInputMode) {
        if (!activity.isFinishing()) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            if (params.softInputMode != softInputMode) {
                params.softInputMode = softInputMode;
                activity.getWindow().setAttributes(params);
            }
        }
    }


}
