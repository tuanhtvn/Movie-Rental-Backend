package com.rental.movie.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Iterator;

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
import com.rental.movie.model.dto.NarrationRequestDTO;
import com.rental.movie.model.dto.NarrationResponseDTO;
import com.rental.movie.model.entity.Narration;
import com.rental.movie.model.entity.Film;
import com.rental.movie.util.mapper.NarrationMapper;
import com.rental.movie.util.mapper.FilmMapper;
import com.rental.movie.repository.NarrationRepository;
import com.rental.movie.repository.FilmRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NarrationServiceImpl implements NarrationService {

    @Autowired
    private NarrationMapper narrationMapper;
    @Autowired
    private FilmService filmService;
    @Autowired
    private NarrationRepository narrationRepository;
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
    public NarrationResponseDTO createNarration(NarrationRequestDTO narrationDTO) {
        Narration narration = narrationMapper.convertToEntity(narrationDTO);
        Narration savedNarration = narrationRepository.save(narration);
        return narrationMapper.convertToDTO(savedNarration);
    }

    @Override
    @Transactional
    public NarrationResponseDTO updateNarration(String id, NarrationRequestDTO narrationRequestDTO) {
        Narration narration = narrationRepository.findById(id)
                .orElseThrow(() -> new CustomException("Thuyết minh với ID " + id + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        Narration updateNarration = narrationMapper.convertToEntity(narrationRequestDTO, narration);
        return narrationMapper.convertToDTO(narrationRepository.save(updateNarration));
    }

    @Override
    @Transactional
    public void deleteNarration(String narrationId) {
        Narration narrations = narrationRepository.findById(narrationId)
                .orElseThrow(() -> new CustomException("Thuyết minh với ID " + narrationId + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        List<Film> films = filmRepository.findByNarrationsId(narrationId);

        for (Film film : films) {
            Iterator<Narration> iterator = film.getNarrations().iterator();
            while (iterator.hasNext()) {
                Narration narration = iterator.next();
                if (narration.getId().equals(narrationId)) {
                    iterator.remove();
                    break;
                }
            }

            filmRepository.save(film);
        }

        narrationRepository.deleteById(narrationId);
    }

    @Override
    @Transactional
    public FilmResponseDTO addNarration(NarrationRequestDTO narrationDTO, String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        Narration narration = narrationMapper.convertToEntity(narrationDTO);
        film.getNarrations().add(narration);
        filmRepository.save(film);

        return filmMapper.convertToDTO(film);
    }

    @Override
    @Transactional
    public FilmResponseDTO removeNarration(String narrationId, String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        List<Narration> narrations = film.getNarrations();
        narrations.removeIf(narration -> narration.getId().equals(narrationId));
        filmRepository.save(film);

        return filmMapper.convertToDTO(film);
    }

    @Override
    public List<NarrationResponseDTO> getAllNarrations() {
        return narrationRepository.findAll()
                .stream()
                .map(narrationMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NarrationResponseDTO> getAllNarrationsByFilmId(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        return film.getNarrations()
                .stream()
                .map(narrationMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
