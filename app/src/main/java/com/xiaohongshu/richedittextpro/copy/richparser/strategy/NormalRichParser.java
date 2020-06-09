package com.xiaohongshu.richedittextpro.copy.richparser.strategy;

import android.graphics.Color;
import android.util.Pair;

import com.xhs.richparser.base.OnSpannableClickListener;
import com.xiaohongshu.richedittextpro.R;

/**
 * Created by wupengjian on 17/1/17.
 */
public class NormalRichParser extends com.xhs.richparser.strategy.NormalRichParser {

    public NormalRichParser() {
        this(null);
    }

    public NormalRichParser(OnSpannableClickListener listener) {
        super(listener);
    }

    @Override
    protected int getColor() {
        return Color.parseColor("#ffff4444");
    }

    @Override
    protected int getDrawableId(Pair<String, String> info) {
        return R.mipmap.hashtag;
    }
}
