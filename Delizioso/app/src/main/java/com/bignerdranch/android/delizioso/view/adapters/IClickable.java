package com.bignerdranch.android.delizioso.view.adapters;

import android.view.View;

public interface IClickable {
    default void onClick(int position){}
    default void onLongClick(int position){}
}
