package com.gaonic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import nmvictors.myapplynx.ui.dialogs.MyAppDialog;

public class GaonicDialog<K> extends MyAppDialog<K>{

	
	public GaonicDialog(
			Context context,
			nmvictors.myapplynx.ui.dialogs.MyAppDialog.ResultListener<K> callback) {
		super(context,callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getDefaultTextColor() {
		// TODO Auto-generated method stub
		return Color.BLACK;
	}

	@Override
	protected Drawable getTitleBgDrawable() {
		// TODO Auto-generated method stub
		return new ColorDrawable(getContext().getResources().getColor(R.color.pressed_gaonic_abs));
	}

	@Override
	protected int getTitleAndButtonsTextColor() {
		// TODO Auto-generated method stub
		return Color.WHITE;
	}

	@Override
	protected int getThemeBackgroundColor() {
		// TODO Auto-generated method stub
		return Color.WHITE;
	}

	@Override
	protected int getThemeForegroundColor() {
		// TODO Auto-generated method stub
		return Color.WHITE;
	}
}
