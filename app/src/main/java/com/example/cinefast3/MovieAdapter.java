package com.example.cinefast3;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onBookClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.txtMovieName.setText(movie.getName());
        holder.txtMovieGenre.setText(movie.getGenre());
        holder.imgMoviePoster.setImageResource(movie.getPosterResource());

        if (!movie.isNowShowing()) {
            holder.btnBook.setText("Coming Soon");
            holder.btnBook.setEnabled(false);
        } else {
            holder.btnBook.setText("Book Seats");
            holder.btnBook.setEnabled(true);
        }

        holder.btnBook.setOnClickListener(v -> listener.onBookClick(movie));

        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMoviePoster;
        TextView txtMovieName, txtMovieGenre;
        MaterialButton btnBook, btnTrailer;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMoviePoster = itemView.findViewById(R.id.imgMoviePoster);
            txtMovieName = itemView.findViewById(R.id.txtMovieName);
            txtMovieGenre = itemView.findViewById(R.id.txtMovieGenre);
            btnBook = itemView.findViewById(R.id.btnBook);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
        }
    }
}
