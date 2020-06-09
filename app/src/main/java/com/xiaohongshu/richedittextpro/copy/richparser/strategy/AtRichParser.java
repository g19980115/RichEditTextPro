package com.xiaohongshu.richedittextpro.copy.richparser.strategy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Pair;
import android.view.View;

import com.xhs.richparser.VerticalImageSpan;
import com.xhs.richparser.base.OnSpannableClickListener;
import com.xiaohongshu.richedittextpro.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wupengjian on 17/1/17.
 */
public class AtRichParser extends NormalRichParser {

    public AtRichParser() {
        this(null);
    }

    public AtRichParser(OnSpannableClickListener listener) {
        super(listener);
    }

    @Override
    protected int getColor() {
        return Color.parseColor("#ff33b5e5");
    }

    @Override
    protected int getDrawableId(Pair<String, String> info) {
        return R.drawable.ic_at_tag;
    }

    @Override
    public String getType4Server() {
        return "@";
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
        return new Pair<>(infoArr[1], "@" + infoArr[2]);
    }

    @Override
    public SpannableStringBuilder parseStr2Spannable(Context context, final String richStr) {
        final String type = getType4Server();
        final Pair<String, String> info = parseInfo4Server(richStr);

        final String str = String.format("#%s", info.second);

        SpannableStringBuilder spannableStr = new SpannableStringBuilder(str);

        int drawableId = getDrawableId(info);
        if (drawableId != 0) {
            Drawable drawable = getDrawable(context, drawableId);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan imageSpan = new VerticalImageSpan(drawable, richStr, ImageSpan.ALIGN_BOTTOM);
            spannableStr.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getColor());
        spannableStr.setSpan(colorSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                if (null != mOnClickListener) {
                    mOnClickListener.onClick(AtRichParser.this, type, info, richStr);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getColor());
            }
        };
        spannableStr.setSpan(clickableSpan, 0, spannableStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableStr;
    }

}
