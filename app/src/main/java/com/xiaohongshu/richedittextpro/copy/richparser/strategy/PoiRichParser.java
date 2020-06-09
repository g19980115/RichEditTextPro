package com.xiaohongshu.richedittextpro.copy.richparser.strategy;

import android.graphics.Color;
import android.util.Pair;

import com.xhs.richparser.base.OnSpannableClickListener;
import com.xiaohongshu.richedittextpro.R;

/**
 * Created by wupengjian on 17/1/17.
 */
public class PoiRichParser extends NormalRichParser {

    public PoiRichParser() {
        this(null);
    }

    public PoiRichParser(OnSpannableClickListener listener) {
        super(listener);
    }

    @Override
    protected int getColor() {
        return Color.parseColor("#ff33b5e5");
    }

    @Override
    protected int getDrawableId(Pair<String, String> info) {
        return R.mipmap.poi;
    }

    @Override
    public String getType4Server() {
        return "位置";
    }
}
