package com.luck.keyboard.emoji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.luck.keyboard.util.DensityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenmingvs on 16/4/16.
 */
public class WeiBoContentTextUtil {
    public static final String AT_NAME = "\t[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";// @人
    public static final String AT = "@[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^:]]+:";// @人
    public static final String REGEX = "=[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^=]]+=";// 截取==
    public static final String TOPIC = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";// ##话题
    public static final String URL = "http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";// url
    public static final String URL2 = "https://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";// url
    public static final String EMOJI = "\\[(\\S+?)\\]";//emoji 表情
    public static final String ALL = "(" + AT_NAME + ")" + "|" + "(" + AT + ")" + "|" + "(" + TOPIC + ")" + "|" + "(" + URL + ")" + "|" + "(" + URL2 + ")" + "|" + "(" + EMOJI + ")";

    public static SpannableStringBuilder getWeiBoContent(String source, final Context context, TextView textView) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);
        //设置正则
        Pattern pattern = Pattern.compile(ALL);
        Matcher matcher = pattern.matcher(spannableStringBuilder);


        if (matcher.find()) {
            if (!(textView instanceof EditText)) {
                textView.setFocusable(false);
                textView.setClickable(false);
                textView.setLongClickable(false);
            }
            matcher.reset();
        }


        while (matcher.find()) {
            final String at_name = matcher.group(1);
            final String at = matcher.group(2);
            final String topic = matcher.group(3);
            final String url = matcher.group(4);
            final String urls = matcher.group(5);
            final String emoji = matcher.group(6);

            //emoji
            if (emoji != null) {
                int start = matcher.start(6);
                int end = start + emoji.length();
                String imgName = Emoticons.getImgName(emoji);
                if (!TextUtils.isEmpty(imgName)) {
                    int resId = context.getResources().getIdentifier(imgName, "mipmap", context.getPackageName());
                    Drawable emojiDrawable = ContextCompat.getDrawable(context, resId);
                    if (emojiDrawable != null) {
                        emojiDrawable.setBounds(0, 0, DensityUtils.sp2px(context, 17), DensityUtils.sp2px(context, 17));
                        ImageSpan imageSpan = new ImageSpan(emojiDrawable, ImageSpan.ALIGN_BOTTOM) {
                            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                                Drawable b = getDrawable();
                                canvas.save();
                                int transY = bottom - b.getBounds().bottom;
                                transY -= paint.getFontMetricsInt().descent / 2;
                                canvas.translate(x, transY);
                                b.draw(canvas);
                                canvas.restore();
                            }
                        };
                        spannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

        }
        return spannableStringBuilder;
    }

}
