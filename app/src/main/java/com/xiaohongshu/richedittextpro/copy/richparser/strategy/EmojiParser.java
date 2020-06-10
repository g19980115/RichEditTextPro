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
        return new Pair<>("[" + infoArr[0] + "]", str.replaceFirst("#",""));
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
            ImageSpan imageSpan = new VerticalImageSpan(drawable, richStr, ImageSpan.ALIGN_BASELINE);
            spannableStr.setSpan(imageSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else return spannableStr;


        return spannableStr;
    }

    public static String[] emojiNames = {"[龇牙]", "[调皮]", "[流汗]", "[偷笑]", "[再见]"};

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
