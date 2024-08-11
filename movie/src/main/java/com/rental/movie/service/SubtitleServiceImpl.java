package com.rental.movie.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.SubtitleRequestDTO;
import com.rental.movie.model.dto.SubtitleResponseDTO;
import com.rental.movie.model.entity.Subtitle;
import com.rental.movie.model.entity.Film;
import com.rental.movie.util.mapper.SubtitleMapper;
import com.rental.movie.util.mapper.FilmMapper;
import com.rental.movie.repository.SubtitleRepository;
import com.rental.movie.repository.FilmRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubtitleServiceImpl implements SubtitleService {

    @Autowired
    private SubtitleMapper subtitleMapper;
    @Autowired
    private FilmService filmService;
    @Autowired
    private SubtitleRepository subtitleRepository;
    @Autowired
    private FilmMapper filmMapper;
    @Autowired
    private FilmRepository filmRepository;

    @Override
    public FilmResponseDTO getFilmById(String id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));
        return filmMapper.convertToDTO(film);
    }

    @Override
    public Page<FilmResponseDTO> getAllActivedFilm(Pageable pageable, String search) {
        Page<Film> films = filmRepository.findAllByActived(pageable, search);
        if (films.isEmpty()) {
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    @Transactional
    public SubtitleResponseDTO createSubtitle(SubtitleRequestDTO subtitleDTO) {
        Subtitle subtitle = subtitleMapper.convertToEntity(subtitleDTO);
        Subtitle savedSubtitle = subtitleRepository.save(subtitle);
        return subtitleMapper.convertToDTO(savedSubtitle);
    }

    @Override
    @Transactional
    public SubtitleResponseDTO updateSubtitle(String id, SubtitleRequestDTO subtitleRequestDTO) {
        Subtitle subtitle = subtitleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Phụ đề với ID " + id + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        Subtitle updateSubtitle = subtitleMapper.convertToEntity(subtitleRequestDTO, subtitle);
        return subtitleMapper.convertToDTO(subtitleRepository.save(updateSubtitle));
    }

    @Override
    @Transactional
    public void deleteSubtitle(String subtitleId) {
        Subtitle subtitles = subtitleRepository.findById(subtitleId)
                .orElseThrow(() -> new CustomException("Phụ đề với ID " + subtitleId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        List<Film> films = filmRepository.findBySubtitlesId(subtitleId);

        for (Film film : films) {
            Iterator<Subtitle> iterator = film.getSubtitles().iterator();
            while (iterator.hasNext()) {
                Subtitle subtitle = iterator.next();
                if (subtitle.getId().equals(subtitleId)) {
                    iterator.remove();
                    break;
                }
            }

            filmRepository.save(film);
        }

        subtitleRepository.deleteById(subtitleId);
    }

    @Override
    @Transactional
    public FilmResponseDTO addSubtitle(String subtitleId, String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        Subtitle subtitle = subtitleRepository.findById(subtitleId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phụ đề", HttpStatus.NOT_FOUND.value()));

        boolean exists = film.getSubtitles().stream()
                .anyMatch(n -> n.getId().equals(subtitleId));

        if (exists) {
            throw new CustomException("Phụ đề đã tồn tại", HttpStatus.BAD_REQUEST.value());
        }

        film.getSubtitles().add(subtitle);
        filmRepository.save(film);

        return filmMapper.convertToDTO(film);
    }

    @Override
    @Transactional
    public FilmResponseDTO removeSubtitle(String subtitleId, String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        List<Subtitle> subtitles = film.getSubtitles();
        subtitles.removeIf(subtitle -> subtitle.getId().equals(subtitleId));
        filmRepository.save(film);

        return filmMapper.convertToDTO(film);
    }

    @Override
    public List<SubtitleResponseDTO> getAllSubtitles() {
        return subtitleRepository.findAll()
                .stream()
                .map(subtitleMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubtitleResponseDTO> getAllSubtitlesByFilmId(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        return film.getSubtitles()
                .stream()
                .map(subtitleMapper::convertToDTO)
                .collect(Collectors.toList());
    }

//    public List<Subtitle> findByFilmId(String filmId) {
//        return subtitleRepository.findByFilmId(filmId);
//    }

}
