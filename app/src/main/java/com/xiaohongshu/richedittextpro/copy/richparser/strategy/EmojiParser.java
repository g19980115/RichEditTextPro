package com.xiaohongshu.richedittextpro.copy.richparser.strategy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Pair;

import com.xhs.richparser.VerticalImageSpan;
import com.xhs.richparser.base.OnSpannableClickListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wupengjian on 17/1/17.
 */
public class EmojiParser extends NormalRichParser {
    private Context context;

    public EmojiParser(Context context) {
        this(context, null);
    }

    public EmojiParser(Context context, OnSpannableClickListener listener) {
        super(listener);
        this.context = context.getApplicationContext();
    }

    @Override
    protected int getColor() {
        return Color.parseColor("#ff33b5e5");
    }

    @Override
    protected int getDrawableId(Pair<String, String> info) {
        String content = info.first;
        int size = emojiNames.length;
        for (int i = 0; i < size; i++) {
            if (content.equals(emojiNames[i])) {
                return getMipmapRes(context, "expression_" + (i + 1));
            }
        }
        return 0;
    }

    @Override
    public String getPattern4Server() {
        //return String.format("#\\[%s\\][^#\\[\\]]+#", getType4Server());
        return "#\\[[\u4e00-\u9fa5\\w]+\\]#";
    }

    @Override
    public String getType4Server() {
        return "e";
    }

    @Override
    public Pair<String, String> parseInfo4Server(String str) {
        String extraPattern = "[^#\\[\\]]+"; // [^#\[\]]+
        Pattern pattern = Pattern.compile(extraPattern);
        Matcher matcher = pattern.matcher(str);
        String[] infoArr = new String[3];
        int i = 0;
        while (matcher.find()) {
            infoArr[i++] = matcher.group();
        }
        return new Pair<>("[" + infoArr[0] + "]", "");
    }

    @Override
    public SpannableStringBuilder parseStr2Spannable(Context context, final String richStr) {

        //final String type = getType4Server();
        final Pair<String, String> info = parseInfo4Server(richStr);

        final String str = String.format("#%s", info.second);

        SpannableStringBuilder spannableStr = new SpannableStringBuilder(str);

        int drawableId = getDrawableId(info);
        if (drawableId != 0) {
            Drawable drawable = getDrawable(context, drawableId);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan imageSpan = new VerticalImageSpan(drawable, richStr, ImageSpan.ALIGN_BOTTOM);
            spannableStr.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else return spannableStr;

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getColor());
        spannableStr.setSpan(colorSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableStr;
    }

    public static String[] emojiNames = {"[龇牙]", "[调皮]", "[流汗]", "[偷笑]", "[再见]",
            "[敲打]", "[擦汗]", "[猪头]", "[玫瑰]", "[流泪]", "[大哭]", "[嘘]", "[酷]", "[抓狂]",
            "[委屈]", "[便便]", "[炸弹]", "[菜刀]", "[可爱]", "[色]", "[害羞]", "[得意]", "[吐]",
            "[微笑]", "[怒]", "[尴尬]", "[惊恐]", "[冷汗]", "[爱心]", "[示爱]", "[白眼]", "[傲慢]",
            "[难过]", "[惊讶]", "[疑问]", "[困]", "[么么哒]", "[憨笑]", "[爱情]", "[衰]", "[撇嘴]",
            "[阴险]", "[奋斗]", "[发呆]", "[右哼哼]", "[抱抱]", "[坏笑]", "[飞吻]", "[鄙视]",
            "[晕]", "[大兵]", "[可怜]", "[强]", "[弱]", "[握手]", "[胜利]", "[抱拳]", "[凋谢]",
            "[米饭]", "[蛋糕]", "[西瓜]", "[啤酒]", "[瓢虫]", "[勾引]", "[OK]", "[爱你]", "[咖啡]",
            "[月亮]", "[刀]", "[发抖]", "[差劲]", "[拳头]", "[心碎了]", "[太阳]", "[礼物]",
            "[皮球]", "[骷髅]", "[挥手]", "[闪电]", "[饥饿]", "[困]", "[咒骂]", "[折磨]", "[抠鼻]",
            "[鼓掌]", "[糗大了]", "[左哼哼]", "[打哈欠]", "[快哭了]", "[吓]", "[篮球]", "[乒乓]",
            "[NO]", "[跳跳]", "[怄火]", "[转圈]", "[磕头]", "[回头]", "[跳绳]", "[激动]", "[街舞]",
            "[献吻]", "[左太极]", "[右太极]", "[闭嘴]", "[猫咪]", "[红双喜]", "[鞭炮]", "[红灯笼]",
            "[麻将]", "[麦克风]", "[礼品袋]", "[信封]", "[象棋]", "[彩带]", "[蜡烛]", "[爆筋]",
            "[棒棒糖]", "[奶瓶]", "[面条]", "[香蕉]", "[飞机]", "[左车头]", "[车厢]", "[右车头]",
            "[多云]", "[下雨]", "[钞票]", "[熊猫]", "[灯泡]", "[风车]", "[闹钟]", "[彩球]",
            "[钻戒]", "[沙发]", "[纸巾]", "[手枪]", "[青蛙]"};

    private static int getMipmapRes(Context context, String resName) {
        return getResId(context, "mipmap", resName);
    }

    private static int getResId(Context context, String resType, String resName) {
        int resId = 0;
        if (context != null && !TextUtils.isEmpty(resType) && !TextUtils.isEmpty(resName)) {

            String pck1 = context.getPackageName();
            if (TextUtils.isEmpty(pck1)) {
                return resId;
            }

            resId = context.getResources().getIdentifier(resName, resType, pck1);
            if (resId <= 0) {
                resId = context.getResources().getIdentifier(resName.toLowerCase(), resType, pck1);
            }

            if (resId <= 0) {
                System.err.println("failed to parse " + resType + " resource \"" + resName + "\"");
            }

            return resId;
        } else {
            return resId;
        }
    }
}
