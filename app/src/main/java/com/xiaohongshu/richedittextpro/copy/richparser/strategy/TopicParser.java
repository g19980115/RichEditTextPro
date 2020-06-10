package com.xiaohongshu.richedittextpro.copy.richparser.strategy;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.View;

import com.xhs.richparser.base.OnSpannableClickListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopicParser extends NormalRichParser {

    public TopicParser() {
        this(null);
    }

    public TopicParser(OnSpannableClickListener listener) {
        super(listener);
    }

    @Override
    protected int getColor() {
        return Color.parseColor("#ff33b5e5");
    }

    @Override
    protected int getDrawableId(Pair<String, String> info) {
        return 0;
    }

    @Override
    public String getType4Server() {
        return "tp";
    }

    @Override
    public String getPattern4Server() {
        return "#[^#\\s+\\[\\]]+#";
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
        return new Pair<>("", infoArr[0]);
    }

    @Override
    public SpannableStringBuilder parseStr2Spannable(Context context, final String richStr) {
        final String type = getType4Server();
        final Pair<String, String> info = parseInfo4Server(richStr);

        final String str = String.format("#%s#", info.second);

        SpannableStringBuilder spannableStr = new SpannableStringBuilder(str);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getColor());
        spannableStr.setSpan(colorSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                if (null != mOnClickListener) {
                    mOnClickListener.onClick(TopicParser.this, type, info, richStr);
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
