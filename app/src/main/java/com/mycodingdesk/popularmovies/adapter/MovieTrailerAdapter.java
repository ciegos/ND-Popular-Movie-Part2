package com.mycodingdesk.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mycodingdesk.popularmovies.R;
import com.mycodingdesk.popularmovies.model.MovieTrailer;
import com.mycodingdesk.popularmovies.network.MovieAPI;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Cesar Egoavil on 6/21/2018.
 */
public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerAdapterViewHolder> {

    private List<MovieTrailer> mMovieTrailerList;
    private final MovieTrailerClickListener mMovieTrailerClickListener;

    public MovieTrailerAdapter(MovieTrailerClickListener movieClickListener){
        mMovieTrailerClickListener = movieClickListener;
    }

    @NonNull
    @Override
    public MovieTrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_trailer_item_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieTrailerAdapterViewHolder(view, mMovieTrailerClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerAdapterViewHolder holder, int position) {
        MovieTrailer movieTrailer  = mMovieTrailerList.get(position);
        Context context = holder.itemView.getContext();
        String imageRoute = String.format(MovieAPI.BASE_TRAILER_IMAGE_URL, movieTrailer.getKey());
        holder.mMovieTrailerIV.setContentDescription(context.getString(R.string.movielist_trailer_content, movieTrailer.getName()));
        Picasso.with(context)
                .load(imageRoute)
                .into(holder.mMovieTrailerIV);
    }

    @Override
    public int getItemCount() {
        return mMovieTrailerList==null? 0: mMovieTrailerList.size();
    }

    public void setMovieList(List<MovieTrailer> mMovieTrailerList){
        this.mMovieTrailerList = mMovieTrailerList;
        notifyDataSetChanged();
    }

    public class MovieTrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mMovieTrailerIV;

        public MovieTrailerAdapterViewHolder(View itemView, MovieTrailerClickListener clickListener) {
            super(itemView);
            mMovieTrailerIV = itemView.findViewById(R.id.movie_trailer_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(getAdapterPosition()!=RecyclerView.NO_POSITION){
                MovieTrailer movieTrailer = mMovieTrailerList.get(getAdapterPosition());
                mMovieTrailerClickListener.onClick(movieTrailer);
            }
        }
    }
}
