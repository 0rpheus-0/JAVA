package com.vitek.javalabs.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vitek.javalabs.model.Genre;
import com.vitek.javalabs.model.Movie;
import com.vitek.javalabs.model.Year;
import com.vitek.javalabs.payload.MovieAdv;
import com.vitek.javalabs.repository.GenreRepository;
import com.vitek.javalabs.repository.MovieRepository;
import com.vitek.javalabs.repository.YearRepository;
import com.vitek.javalabs.service.MovieAdvService;
import com.vitek.javalabs.service.MovieService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movies;
    private YearRepository years;
    private GenreRepository ganres;
    private MovieAdvService movieAdvService;

    public List<Movie> getAllMovies() {
        return movies.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movies.findById(id);
    }

    public Movie createMovie(Movie movie) {
        movie.setYear(years.findByYearRel(movie.getYear().getYearRel()).orElse(movie.getYear()));
        movie.setGenres(
                movie.getGenres()
                        .stream()
                        .map(x -> ganres.findByName(x.getName()).orElse(x))
                        .collect(Collectors.toSet()));
        return movies.save(movie);
    }

    public Movie createMovieByName(String name) {
        Movie movie = new Movie();
        MovieAdv movieAdv = movieAdvService.getInfotm(name);
        movie.setTitle(movieAdv.getTitle());
        movie.setDirector(movieAdv.getDirector());
        movie.setActors(movieAdv.getActors());
        movie.setLanguage(movieAdv.getLanguage());

        Year year = new Year();
        year.setYearRel(movieAdv.getYear());
        movie.setYear(year);

        String genreStr = movieAdv.getGenre();
        Set<Genre> setGenre = new HashSet<>();

        String[] words = genreStr.split("[,\\s]+");
        for (String word : words) {
            Genre genre = new Genre();
            genre.setName(word.trim());
            setGenre.add(genre);
        }
        movie.setGenres(setGenre);

        createMovie(movie);

        return movie;
    }

    public Movie updateMovie(Long id, Movie movie) {
        movie.setId(id);
        movie.setYear(years.findByYearRel(movie.getYear().getYearRel()).orElse(movie.getYear()));
        movie.setGenres(
                movie.getGenres()
                        .stream()
                        .map(x -> ganres.findByName(x.getName()).orElse(x))
                        .collect(Collectors.toSet()));
        return movies.save(movie);
    }

    public Void deleteMovieBuId(Long id) {
        movies.deleteById(id);
        return null;
    }

}
