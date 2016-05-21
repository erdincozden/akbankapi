package app.akbank.com.akbankapi.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import app.akbank.com.akbankapi.R;

/**
 * Created by erdinc on 5/21/16.
 */
public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable drawable;

    public SimpleDividerItemDecoration(Context context){
        drawable= ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state){
        int left=parent.getPaddingLeft();
        int right=parent.getWidth()-parent.getPaddingRight();

        int childCount=parent.getChildCount();
        for(int i=0;i<childCount;i++){
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params=(RecyclerView.LayoutParams) child.getLayoutParams();

            int top=child.getBottom();
            int bottom=top+drawable.getIntrinsicHeight();

            drawable.setBounds(left,top,right,bottom);
            drawable.draw(c);
        }
    }
}
