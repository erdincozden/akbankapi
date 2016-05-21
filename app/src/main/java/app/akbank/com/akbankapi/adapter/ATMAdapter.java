package app.akbank.com.akbankapi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.TextView;
import app.akbank.com.akbankapi.R;
import app.akbank.com.akbankapi.model.ATM;

import java.util.ArrayList;

/**
 * Created by erdinc on 5/21/16.
 */
public class ATMAdapter  extends RecyclerView.Adapter<ATMAdapter.ViewHolder>{

    private Context context;
    private ArrayList<ATM> atmArrayList;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView city,address;
        public ViewHolder(View itemView) {
            super(itemView);
            address=(TextView)itemView.findViewById(R.id.address);
            city=(TextView)itemView.findViewById(R.id.city);
        }
    }

    public ATMAdapter(Context context,ArrayList<ATM> atmArrayList){
        this.context=context;
        this.atmArrayList=atmArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.atm_list_row,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ATM atm=atmArrayList.get(position);
        holder.city.setText(atm.getCity());
        holder.address.setText(atm.getAddress());



    }

    @Override
    public int getItemCount() {
        return atmArrayList.size();
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnTouchListener, RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ATMAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ATMAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener !=null){
                        clickListener.onLongClick(child,recyclerView.getChildPosition(child));
                    }
                }

            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }


}
