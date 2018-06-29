package com.mycodingdesk.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycodingdesk.popularmovies.R;
import com.mycodingdesk.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by Cesar Egoavil on 5/1/2018.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> mMovieList;
    private URL mImageUrl;
    private final MovieClickListener mMovieClickListener;

    public MovieAdapter(MovieClickListener movieClickListener){
        mMovieClickListener = movieClickListener;
    }


    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view, mMovieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        Movie movie  = mMovieList.get(position);
        Context context = holder.itemView.getContext();
        holder.mMovieTitleTV.setText(movie.getTitle());
        holder.mMoviePosterIV.setContentDescription(context.getString(R.string.movielist_poster_content, movie.getTitle()));
        Picasso.with(holder.itemView.getContext())
                .load(mImageUrl.toString() + movie.getPosterPath())
                .fit()
                .centerInside()
                .into(holder.mMoviePosterIV);
    }

    @Override
    public int getItemCount() {
        return mMovieList==null? 0: mMovieList.size();
    }

    public void setMovieList(List<Movie> mMovieList, URL imageUrl){
        this.mImageUrl = imageUrl;
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mMoviePosterIV;
        private final TextView mMovieTitleTV;
        private final MovieClickListener mMovieClickListener;

        public MovieAdapterViewHolder(View itemView, MovieClickListener clickListener) {
            super(itemView);
            mMoviePosterIV = itemView.findViewById(R.id.movielist_poster_iv);
            mMovieTitleTV = itemView.findViewById(R.id.movie_title_tv);
            mMovieClickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(getAdapterPosition()!=RecyclerView.NO_POSITION){
                Movie movie = mMovieList.get(getAdapterPosition());
                mMovieClickListener.onClick(movie);
            }
        }
    }
}
