package point.point;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<RecyclerViewData> recyclerViewData ;
    private  OnItemClickListener mClickListener;

    // parent activity will implement this method to respond to click events
    
    public interface OnItemClickListener{

        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    // allows clicks events to be caught

    public void setOnItemClickListener(OnItemClickListener listener){
		
        this.mClickListener =  listener;
    }

    // stores and recycles views as they are scrolled off screen
    
    public  class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView mDeleteImage;
        
        public ViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
			mDeleteImage = itemView.findViewById(R.id.image_delete);
			
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v){
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mClickListener.onItemClick(position);
                        }
                    }
                }
            });


            mDeleteImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mClickListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    // data is passed into the constructor
    
    public RecyclerViewAdapter( ArrayList<RecyclerViewData> data){
        this.recyclerViewData = data;
		// this.mInflater = LayoutInflater.from(context);
    }

    // inflates the row layout from xml when needed
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recyclerview_row,
                parent, false);
       // View view = mInflater.inflate(R.layout.activity_recyclerview_row, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
		RecyclerViewData currentItem = recyclerViewData.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount(){
        return recyclerViewData.size();
    }

}
