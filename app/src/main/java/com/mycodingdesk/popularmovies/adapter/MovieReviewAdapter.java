package com.mycodingdesk.popularmovies.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.mycodingdesk.popularmovies.R;
import com.mycodingdesk.popularmovies.model.MovieResult;
import com.mycodingdesk.popularmovies.model.MovieReview;
import com.mycodingdesk.popularmovies.model.MovieTrailer;
import com.mycodingdesk.popularmovies.network.MovieAPI;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Cesar Egoavil on 6/21/2018.
 */
public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewAdapterViewHolder> {

    private List<MovieReview> mMovieReviewList;

    public MovieReviewAdapter(){
    }

    @NonNull
    @Override
    public MovieReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_review_item_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapterViewHolder holder, int position) {
        MovieReview movieReview  = mMovieReviewList.get(position);
        Context context = holder.itemView.getContext();
        holder.mReviewAuthorTV.setText(movieReview.getAuthor());
        holder.mReviewContentTV.setText(movieReview.getContent());
    }

    @Override
    public int getItemCount() {
        return mMovieReviewList==null? 0: mMovieReviewList.size();
    }

    public void setMovieList(List<MovieReview> mMovieReviewList){
        this.mMovieReviewList = mMovieReviewList;
        notifyDataSetChanged();
    }

    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder  {

        private final TextView mReviewAuthorTV;
        private final ExpandableTextView mReviewContentTV;

        public MovieReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewAuthorTV = itemView.findViewById(R.id.movie_review_author_tv);
            mReviewContentTV = (ExpandableTextView)itemView.findViewById(R.id.movie_review_content_expandable_tv);
        }
    }
}
