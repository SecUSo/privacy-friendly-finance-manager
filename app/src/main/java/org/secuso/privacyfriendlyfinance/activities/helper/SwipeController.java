/*
 Privacy Friendly Finance Manager is licensed under the GPLv3.
 Copyright (C) 2019 Leonard Otto, Felix Hofmann

 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.

 Additionally icons from Google Design Material Icons are used that are licensed under Apache
 License Version 2.0.
 */

package org.secuso.privacyfriendlyfinance.activities.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;


/**
 * Swipe controller. Manages swipe gestures.
 *
 * @author Felix Hofmann
 * @author Leonard Otto
 */
public class SwipeController extends Callback {

    private Context context;


    public enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    private SwipeControllerAction leftAction;
    private SwipeControllerAction rightAction;

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private RectF buttonInstance = null;

    private RecyclerView.ViewHolder currentItemViewHolder = null;


    public SwipeController(Context context, SwipeControllerAction leftAction, SwipeControllerAction rightAction) {
        this.context = context;
        this.leftAction = leftAction;
        this.rightAction = rightAction;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int flags = 0;
        if (rightAction != null) flags |= LEFT;
        if (leftAction != null) flags |= RIGHT;
        return makeMovementFlags(0, flags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, leftAction.getTotalWidth());
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -rightAction.getTotalWidth());
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (rightAction != null && dX < -rightAction.getTotalWidth()) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if (leftAction != null && dX > leftAction.getTotalWidth()) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    if (buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE && leftAction != null) {
                            leftAction.onClick(viewHolder.getAdapterPosition());
                        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE && rightAction != null) {
                            rightAction.onClick(viewHolder.getAdapterPosition());
                        }
                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            Rect targetspace = new Rect(
                    itemView.getLeft() + leftAction.getHPadding(),
                    itemView.getTop() + leftAction.getVPadding(),
                    itemView.getLeft() + leftAction.getTotalWidth() - leftAction.getHPadding(),
                    itemView.getBottom() - leftAction.getVPadding());
            drawIcon(c, leftAction.getIcon(), targetspace);
            buttonInstance = new RectF(targetspace);
        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            Rect targetspace = new Rect(
                    itemView.getRight() - rightAction.getTotalWidth() + rightAction.getHPadding(),
                    itemView.getTop() + rightAction.getVPadding(),
                    itemView.getRight() - rightAction.getHPadding(),
                    itemView.getBottom() - rightAction.getVPadding());
            drawIcon(c, rightAction.getIcon(), targetspace);
            buttonInstance = new RectF(targetspace);
        } else {
            buttonInstance = null;
        }
    }

    private void drawIcon(Canvas c, Drawable icon, Rect target) {
        Log.d("Target", "" + icon.getIntrinsicWidth() + " : " + icon.getIntrinsicHeight());
        Log.d("Target", target.toString());
        if (target.width() < target.height()) {
            int sub = (target.height() - target.width()) / 2;
            target = new Rect(target.left, target.top + sub, target.right, target.bottom - sub);
        } else if (target.width() > target.height()) {
            int sub = (target.width() - target.height()) / 2;
            target = new Rect(target.left + sub, target.top, target.right - sub, target.bottom);

        }
        icon.setBounds(target);
        icon.draw(c);
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }

    public abstract static class SwipeControllerAction {
        public Drawable getIcon() {
            return null;
        }
        public void onClick(int position) {}

        public int getWidth() {
            return 120;
        }
        public int getHPadding() {
            return 10;
        }
        public int getVPadding() {
            return 5;
        }
        public int getTotalWidth() {
            if (getIcon() == null) return 0;
            return getHPadding() * 2 + getWidth();
        }
    }

}
