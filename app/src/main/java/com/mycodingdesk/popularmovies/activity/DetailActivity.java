package com.mycodingdesk.popularmovies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.mycodingdesk.popularmovies.R;
import com.mycodingdesk.popularmovies.adapter.MovieReviewAdapter;
import com.mycodingdesk.popularmovies.adapter.MovieTrailerAdapter;
import com.mycodingdesk.popularmovies.adapter.MovieTrailerClickListener;
import com.mycodingdesk.popularmovies.database.AppDatabase;
import com.mycodingdesk.popularmovies.databinding.ActivityDetailBinding;
import com.mycodingdesk.popularmovies.model.Movie;
import com.mycodingdesk.popularmovies.model.MovieReview;
import com.mycodingdesk.popularmovies.model.MovieReviewResult;
import com.mycodingdesk.popularmovies.model.MovieTrailer;
import com.mycodingdesk.popularmovies.model.MovieTrailerResult;
import com.mycodingdesk.popularmovies.network.MovieAPI;
import com.mycodingdesk.popularmovies.network.NetworkUtil;
import com.mycodingdesk.popularmovies.viewmodel.MainViewModel;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity implements
        MovieTrailerClickListener {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private static Movie mMovie;
    private MovieTrailerAdapter mMovieTrailerAdapter;
    private MovieReviewAdapter mMovieReviewAdapter;
    public static final String MOVIE_DATA = "MOVIE_DATA";
    private ActivityDetailBinding mDetailActivityBinding;
    private boolean mIsTablet;
    private static final int MOVIE_TRAILER_LOADER_ID = 5559;
    private static final int MOVIE_REVIEW_LOADER_ID = 5566;
    private AppDatabase mAppDb;
    private boolean mAdded = false;

    private final View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveFavoriteMovie();
        }
    };

    private final LoaderManager.LoaderCallbacks<List<MovieTrailer>> movieTrailerLoaderResult= new
            LoaderManager.LoaderCallbacks<List<MovieTrailer>>() {
            @NonNull
            @Override
            public Loader<List<MovieTrailer>> onCreateLoader(int id, @Nullable Bundle args) {
                return new MovieTrailerTaskLoader(DetailActivity.this);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<MovieTrailer>> loader, List<MovieTrailer> data) {
                mMovieTrailerAdapter.setMovieList(data);
                mDetailActivityBinding.loadingDetailProgress.setVisibility(View.GONE);
                if (null == data || data.size()==0) {
                    showTrailerErrorMessage();
                } else {
                    showMovieTrailerDataView();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<MovieTrailer>> loader) {
            }
    };

    private final LoaderManager.LoaderCallbacks<List<MovieReview>> movieReviewLoaderResult= new
            LoaderManager.LoaderCallbacks<List<MovieReview>>() {
                @NonNull
                @Override
                public Loader<List<MovieReview>> onCreateLoader(int id, @Nullable Bundle args) {
                    return new MovieReviewTaskLoader(DetailActivity.this);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<List<MovieReview>> loader, List<MovieReview> data) {
                    mMovieReviewAdapter.setMovieList(data);
                    mDetailActivityBinding.loadingDetailProgress.setVisibility(View.GONE);
                    if (null == data || data.size()==0) {
                        showReviewErrorMessage();
                    } else {
                        showMovieReviewDataView();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<List<MovieReview>> loader) {
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mDetailActivityBinding.movieTrailerRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mDetailActivityBinding.movieReviewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setSupportActionBar(mDetailActivityBinding.toolbarMovieDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIsTablet = getResources().getBoolean(R.bool.isTablet);
        mDetailActivityBinding.favoriteMovieFab.setOnClickListener(fabClickListener);

        mMovieTrailerAdapter = new MovieTrailerAdapter(this);
        mDetailActivityBinding.movieTrailerRecycler.setAdapter(mMovieTrailerAdapter);

        mMovieReviewAdapter = new MovieReviewAdapter();
        mDetailActivityBinding.movieReviewRecycler.setAdapter(mMovieReviewAdapter);

        mAppDb = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MOVIE_DATA)) {
                mMovie = intent.getParcelableExtra(MOVIE_DATA);
                displayMovieData(mMovie);
            }
        }
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mAdded = false;
                for(int i=0; i<movies.size();i++){
                    if(movies.get(i).getId() == mMovie.getId()){
                        mAdded = true;
                        break;
                    }
                }
            }
        });
    }

    private void showReviewErrorMessage(){
        mDetailActivityBinding.errorReviewMessageTv.setVisibility(View.VISIBLE);
        mDetailActivityBinding.movieReviewRecycler.setVisibility(View.GONE);
    }

    private void showTrailerErrorMessage(){
        mDetailActivityBinding.errorTrailerMessageTv.setVisibility(View.VISIBLE);
        mDetailActivityBinding.movieTrailerRecycler.setVisibility(View.GONE);
    }

    private void displayMovieData(Movie movie) {
        mDetailActivityBinding.movieSynopsisTv.setText(movie.getOverview());
        mDetailActivityBinding.collapsedMovieTitle.setTitle(movie.getTitle());
        mDetailActivityBinding.moviePosterIv.setContentDescription(getString(R.string.movielist_poster_content, movie.getTitle()));
        mDetailActivityBinding.movieRatingValueId.setText(getString(R.string.movie_rating_value, movie.getVoteAverage()));
        mDetailActivityBinding.movieRatingVotesId.setText(getString(R.string.movie_votes_value, movie.getVoteCount()));
        URL urlImage = MovieAPI.buildImageBackdropURL(mIsTablet);
        Picasso.with(this)
                .load(urlImage.toString() + movie.getBackdropPath())
                .into(mDetailActivityBinding.moviePosterIv);
        Picasso.with(this)
                .load(urlImage.toString() + movie.getPosterPath())
                .into(mDetailActivityBinding.movieDetailPosterIv);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        try {
            Date date = format.parse(movie.getReleaseDate());
            mDetailActivityBinding.movieReleasedateTv.setText(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getSupportLoaderManager().initLoader(MOVIE_TRAILER_LOADER_ID, null, movieTrailerLoaderResult);
        getSupportLoaderManager().initLoader(MOVIE_REVIEW_LOADER_ID, null, movieReviewLoaderResult);
    }

    @Override
    public void onClick(MovieTrailer movieTrailer) {
        Intent intent = new Intent(this, YoutubeActivity.class);
        intent.putExtra(YoutubeActivity.MOVIE_TRAILER_DATA, movieTrailer);
        startActivity(intent);
    }

    private void showMovieTrailerDataView() {
        mDetailActivityBinding.errorTrailerMessageTv.setVisibility(View.GONE);
        mDetailActivityBinding.movieTrailerRecycler.setVisibility(View.VISIBLE);
    }

    private void showMovieReviewDataView() {
        mDetailActivityBinding.errorReviewMessageTv.setVisibility(View.GONE);
        mDetailActivityBinding.movieReviewRecycler.setVisibility(View.VISIBLE);
    }

    private void saveFavoriteMovie() {
        if(!mAdded) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    mAppDb.movieDao().insertMovie(mMovie);
                }
            });
            Toast.makeText(DetailActivity.this, getString(R.string.added_favorite), Toast.LENGTH_SHORT).show();
        }else{
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    mAppDb.movieDao().deleteMovie(mMovie.getId());
                }
            });
            Toast.makeText(DetailActivity.this, getString(R.string.removed_favorite), Toast.LENGTH_SHORT).show();
        }
    }

    private static class MovieTrailerTaskLoader extends AsyncTaskLoader<List<MovieTrailer>> {

        private final WeakReference<DetailActivity> detailActivity;

        MovieTrailerTaskLoader(DetailActivity contextActivity) {
            super(contextActivity);
            detailActivity = new WeakReference<>(contextActivity);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            detailActivity.get().mDetailActivityBinding.loadingDetailProgress.setVisibility(View.VISIBLE);
            forceLoad();
        }

        @Nullable
        @Override
        public List<MovieTrailer> loadInBackground() {
            if(NetworkUtil.isConnectionAvailable( detailActivity.get())) {
                try {
                    MovieTrailerResult movieTrailerResult = MovieAPI.getMovieTrailerList(MovieAPI.buildMovieTrailerURL(mMovie.getId()));
                    return movieTrailerResult.getResult();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }else
                return null;
        }
    }

    private static class MovieReviewTaskLoader extends AsyncTaskLoader<List<MovieReview>> {

        private final WeakReference<DetailActivity> detailActivity;

        MovieReviewTaskLoader(DetailActivity contextActivity) {
            super(contextActivity);
            detailActivity = new WeakReference<>(contextActivity);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            detailActivity.get().mDetailActivityBinding.loadingDetailProgress.setVisibility(View.VISIBLE);
            forceLoad();
        }

        @Nullable
        @Override
        public List<MovieReview> loadInBackground() {
            if(NetworkUtil.isConnectionAvailable( detailActivity.get())) {
                try {
                    MovieReviewResult movieReviewResult = MovieAPI.getMovieReviewList(MovieAPI.buildMovieReviewURL(mMovie.getId()));
                    return movieReviewResult.getResult();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }else
                return null;
        }
    }
}
