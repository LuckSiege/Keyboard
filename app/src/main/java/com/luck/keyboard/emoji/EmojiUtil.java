package com.luck.keyboard.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.luck.keyboard.R;
import com.luck.keyboard.entity.Emoji;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiUtil {
    private static ArrayList<Emoji> emojiList;

    public static ArrayList<Emoji> getEmojiList() {
        if (emojiList == null) {
            emojiList = generateEmojis();
        }
        return emojiList;
    }

    private static ArrayList<Emoji> generateEmojis() {
        ArrayList<Emoji> list = new ArrayList<>();
        for (int i = 0; i < EmojiResArray.length; i++) {
            Emoji emoji = new Emoji();
            emoji.setImageUri(EmojiResArray[i]);
            emoji.setContent(EmojiTextArray[i]);
            list.add(emoji);
        }
        return list;
    }


    public static final int[] EmojiResArray = {
            R.mipmap.d_aini,
            R.mipmap.d_aoteman,
            R.mipmap.d_baibai,
            R.mipmap.d_beishang,
            R.mipmap.d_bishi,
            R.mipmap.d_bizui,
            R.mipmap.d_chanzui,
            R.mipmap.d_chijing,
            R.mipmap.d_dahaqi,
            R.mipmap.d_dalian,
            R.mipmap.d_ding,
            R.mipmap.d_doge,
            R.mipmap.d_feizao,
            R.mipmap.d_ganmao,
            R.mipmap.d_guzhang,
            R.mipmap.d_haha,
            R.mipmap.d_haixiu,
            R.mipmap.d_han,
            R.mipmap.d_hehe,
            R.mipmap.d_heixian,
            R.mipmap.d_heng,
            R.mipmap.d_huaxin,
            R.mipmap.d_jiyan,
            R.mipmap.d_keai,
            R.mipmap.d_kelian,
            R.mipmap.d_ku,
            R.mipmap.d_kun,
            R.mipmap.d_landelini,
            R.mipmap.d_lei,
            R.mipmap.d_madaochenggong,
            R.mipmap.d_miao,
            R.mipmap.d_nanhaier,
            R.mipmap.d_nu,
            R.mipmap.d_numa,
            R.mipmap.d_numa,
            R.mipmap.d_qian,
            R.mipmap.d_qinqin,
            R.mipmap.d_shayan,
            R.mipmap.d_shengbing,
            R.mipmap.d_shenshou,
            R.mipmap.d_shiwang,
            R.mipmap.d_shuai,
            R.mipmap.d_shuijiao,
            R.mipmap.d_sikao,
            R.mipmap.d_taikaixin,
            R.mipmap.d_touxiao,
            R.mipmap.d_tu,
            R.mipmap.d_tuzi,
            R.mipmap.d_wabishi,
            R.mipmap.d_weiqu,
            R.mipmap.d_xiaoku,
            R.mipmap.d_xiongmao,
            R.mipmap.d_xixi,
            R.mipmap.d_xu,
            R.mipmap.d_yinxian,
            R.mipmap.d_yiwen,
            R.mipmap.d_youhengheng,
            R.mipmap.d_yun,
            R.mipmap.d_zhajipijiu,
            R.mipmap.d_zhuakuang,
            R.mipmap.d_zhutou,
            R.mipmap.d_zuiyou,
            R.mipmap.d_zuohengheng,
            R.mipmap.f_geili,
            R.mipmap.f_hufen,
            R.mipmap.f_jiong,
            R.mipmap.f_meng,
            R.mipmap.f_shenma,
            R.mipmap.f_v5,
            R.mipmap.f_xi,
            R.mipmap.f_zhi,
            R.mipmap.h_buyao,
            R.mipmap.h_good,
            R.mipmap.h_haha,
            R.mipmap.h_lai,
            R.mipmap.h_ok,
            R.mipmap.h_quantou,
            R.mipmap.h_ruo,
            R.mipmap.h_woshou,
            R.mipmap.h_ye,
            R.mipmap.h_zan,
            R.mipmap.h_zuoyi,
            R.mipmap.l_shangxin,
            R.mipmap.l_xin,
            R.mipmap.o_dangao,
            R.mipmap.o_feiji,
            R.mipmap.o_ganbei,
            R.mipmap.o_huatong,
            R.mipmap.o_lazhu,
            R.mipmap.o_liwu,
            R.mipmap.o_lvsidai,
            R.mipmap.o_weibo,
            R.mipmap.o_weiguan,
            R.mipmap.o_yinyue,
            R.mipmap.o_zhaoxiangji,
            R.mipmap.o_zhong,
            R.mipmap.w_fuyun,
            R.mipmap.w_shachenbao,
            R.mipmap.w_taiyang,
            R.mipmap.w_weifeng,
            R.mipmap.w_xianhua,
            R.mipmap.w_xiayu,
            R.mipmap.w_yueliang,
    };

    public static final String[] EmojiTextArray = {
            "[爱你]",
            "[奥特曼]",
            "[拜拜]",
            "[悲伤]",
            "[鄙视]",
            "[闭嘴]",
            "[馋嘴]",
            "[吃惊]",
            "[哈欠]",
            "[打脸]",
            "[顶]",
            "[doge]",
            "[肥皂]",
            "[感冒]",
            "[鼓掌]",
            "[哈哈]",
            "[害羞]",
            "[汗]",
            "[微笑]",
            "[黑线]",
            "[哼]",
            "[色]",
            "[挤眼]",
            "[可爱]",
            "[可怜]",
            "[酷]",
            "[困]",
            "[白眼]",
            "[泪]",
            "[马到成功]",
            "[喵喵]",
            "[男孩儿]",
            "[怒]",
            "[怒骂]",
            "[女孩儿]",
            "[钱]",
            "[亲亲]",
            "[傻眼]",
            "[生病]",
            "[草泥马]",
            "[失望]",
            "[衰]",
            "[睡]",
            "[思考]",
            "[太开心]",
            "[偷笑]",
            "[吐]",
            "[兔子]",
            "[挖鼻]",
            "[委屈]",
            "[笑cry]",
            "[熊猫]",
            "[嘻嘻]",
            "[嘘]",
            "[阴险]",
            "[疑问]",
            "[右哼哼]",
            "[晕]",
            "[炸鸡啤酒]",
            "[抓狂]",
            "[猪头]",
            "[最右]",
            "[左哼哼]",
            "[给力]",
            "[互粉]",
            "[囧]",
            "[萌]",
            "[神马]",
            "[威武]",
            "[喜]",
            "[织]",
            "[NO]",
            "[good]",
            "[haha]",
            "[来]",
            "[OK]",
            "[拳头]",
            "[弱]",
            "[握手]",
            "[耶]",
            "[赞]",
            "[作揖]",
            "[伤心]",
            "[心]",
            "[蛋糕]",
            "[飞机]",
            "[干杯]",
            "[话筒]",
            "[蜡烛]",
            "[礼物]",
            "[绿丝带]",
            "[围脖]",
            "[围观]",
            "[音乐]",
            "[照相机]",
            "[钟]",
            "[浮云]",
            "[沙尘暴]",
            "[太阳]",
            "[微风]",
            "[鲜花]",
            "[下雨]",
            "[月亮]",
    };

    static {
        emojiList = generateEmojis();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void handlerEmojiText(TextView comment, String content, Context context) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        if (m.find()) {
            if (!(comment instanceof EditText)) {
                comment.setMovementMethod(ClickableMovementMethod.getInstance());
                comment.setFocusable(false);
                comment.setClickable(false);
                comment.setLongClickable(false);
            }
            m.reset();
        }

        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    int start = m.start();
                    int end = m.end();
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri()
                            , dip2px(context, 18), dip2px(context, 18))),
                            start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);
    }
}
