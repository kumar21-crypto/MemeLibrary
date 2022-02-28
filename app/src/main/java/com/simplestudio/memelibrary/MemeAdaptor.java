package com.simplestudio.memelibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class MemeAdaptor extends RecyclerView.Adapter<MemeAdaptor.MemeViewHolder> {

    Context context;
    ArrayList<MemeModel> memeModelArrayList;

    public MemeAdaptor(Context context, ArrayList<MemeModel> memeModelArrayList) {
        this.context = context;
        this.memeModelArrayList = memeModelArrayList;
    }

    @NonNull
    @Override
    public MemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meme_layout, parent, false);
        return new MemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemeViewHolder holder, int position) {
        MemeModel memeModel = memeModelArrayList.get(position);
        holder.memeTitle.setText(memeModel.getMemeTitle());
        Glide.with(context).load(memeModel.getMemeImageUrl()).into(holder.memeImage);



        holder.memeShare.setOnClickListener(v -> {
            BitmapDrawable drawable = (BitmapDrawable) holder.memeImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM,getImageUri(context,bitmap));
            sendIntent.setType("image/*");
            context.startActivity(Intent.createChooser(sendIntent,"share meme"));
        });

    }

    @Override
    public int getItemCount() {
        return memeModelArrayList.size();
    }

    public Uri getImageUri(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,"meme_" + Calendar.getInstance().getTime(),null);
        return Uri.parse(path);
    }

    public static class MemeViewHolder extends RecyclerView.ViewHolder {

        ImageView memeImage;
        TextView memeTitle;
        ImageButton memeShare;

        public MemeViewHolder(@NonNull View itemView) {
            super(itemView);

            memeImage = itemView.findViewById(R.id.memeImageView);
            memeTitle = itemView.findViewById(R.id.memeTitle);
            memeShare = itemView.findViewById(R.id.shareButton);

        }
    }
}
