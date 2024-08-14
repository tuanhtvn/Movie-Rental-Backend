package com.rental.movie.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.RentalType;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.*;
import com.rental.movie.model.entity.*;
import com.rental.movie.repository.*;
import com.rental.movie.util.mapper.FilmMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private SubtitleRepository subtitleRepository;
    @Autowired
    private NarrationRepository narrationRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FilmMapper filmMapper;
    @Autowired
    private GoogleDriveService googleDriveService;
    @Autowired
    private IAuthentication authManager;

    @Override
    public Film getById(String id) {
        // log.info("Get film by id: {}", id);
        return filmRepository.findById(id).orElseThrow(() -> {
            // log.error("Film not found with id: {}", id);
            throw new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value());
        });
    }

    @Override
    public FilmResponseDTO getByIdFilm(String id) {
        return filmMapper.convertToDTO(filmRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value());
        }));
    }

    @Override
    public FilmInfoDTO getFilmInfoById(String id) {
        Film film = filmRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value());
        });

        FilmInfoDTO filmInfoDTO = new FilmInfoDTO();
        filmInfoDTO.setFilmName(film.getFilmName());
        filmInfoDTO.setDescription(film.getDescription());
        filmInfoDTO.setTrailerUrl(film.getTrailerUrl());
        filmInfoDTO.setReleaseDate(film.getReleaseDate());
        filmInfoDTO.setDirector(film.getDirector());
        filmInfoDTO.setLanguage(film.getLanguage());
        filmInfoDTO.setAge(film.getAge());
        filmInfoDTO.setPrice(film.getPrice());

        return filmInfoDTO;
    }

    public FilmResourcesDTO getFilmResourcesById(String id) {
        Film film = filmRepository.findById(id).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value());
        });

        FilmResourcesDTO filmResourcesDTO = new FilmResourcesDTO();
        filmResourcesDTO.setFilmUrl(film.getFilmUrl());

        filmResourcesDTO.setSubtitles(
                film.getSubtitles().stream()
                        .map(this::mapToSubtitleResponseDTO)
                        .collect(Collectors.toList()));

        filmResourcesDTO.setNarrations(
                film.getNarrations().stream()
                        .map(this::mapToNarrationResponseDTO)
                        .collect(Collectors.toList()));

        return filmResourcesDTO;
    }

    private SubtitleResponseDTO mapToSubtitleResponseDTO(Subtitle subtitle) {
        SubtitleResponseDTO dto = new SubtitleResponseDTO();
        dto.setId(subtitle.getId());
        dto.setSubtitleName(subtitle.getSubtitleName());
        dto.setSubtitleUrl(subtitle.getSubtitleUrl());
        return dto;
    }

    private NarrationResponseDTO mapToNarrationResponseDTO(Narration narration) {
        NarrationResponseDTO dto = new NarrationResponseDTO();
        dto.setId(narration.getId());
        dto.setLanguage(narration.getLanguage());
        dto.setNarrationUrl(narration.getNarrationUrl());
        return dto;
    }

    @Override
    public Page<FilmResponseDTO> getAllActivedFilm(Pageable pageable, String search) {
        Page<Film> films = filmRepository.findAllByActived(pageable, search);
        // log.info("Get all actived films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public Page<FilmResponseDTO> getAllNotDeletedFilm(Pageable pageable, String search) {
        Page<Film> films = filmRepository.findAllByNotDeleted(pageable, search);
        // log.info("Get all not deleted films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public Page<FilmResponseDTO> getAllDeletedFilm(Pageable pageable, String search) {
        Page<Film> films = filmRepository.findAllByDeleted(pageable, search);
        // log.info("Get all deleted films: {}", films.getContent().toString());
        if (films.isEmpty()) {
            log.error("No films found");
            throw new CustomException("Không có phim nào", HttpStatus.NOT_FOUND.value());
        }
        return filmMapper.convertToDTO(films);
    }

    @Override
    public List<FilmResponseDTO> searchFilmByName(String keywords) {
        String keyword = removeAccents(keywords).toLowerCase();

        return filmRepository.findByActived().stream()
                .filter(film -> {
                    String filmName = removeAccents(film.getFilmName()).toLowerCase();
                    return filmName.contains(keyword);
                })
                .map(filmMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public static String removeAccents(String text) {
        if (text == null) {
            return null;
        }
        return java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[đĐ]", "d");
    }

    @Override
    public FilmResponseDTO createFilm(FilmRequestDTO filmDTO) {
        Film film = filmMapper.convertToEntity(filmDTO);

        // log.info("Create film {}", film.toString());
        Film savedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(savedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO deleteFilmById(String filmId) {
        Film film = filmRepository.findByIdDefault(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại",
                        HttpStatus.NOT_FOUND.value()));

        film.setIsDeleted(true);

        // log.info("Soft delete film {}", film.toString());
        Film deletedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(deletedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO updateFilmById(String filmId, FilmRequestDTO filmDTO) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại",
                        HttpStatus.NOT_FOUND.value()));

        Film updateFilm = filmMapper.convertToEntity(filmDTO, film);
        return filmMapper.convertToDTO(filmRepository.save(updateFilm));
    }

    @Override
    @Transactional
    public FilmResponseDTO activeFilm(String filmId) {
        Film film = filmRepository.findByIdDefault(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại",
                        HttpStatus.NOT_FOUND.value()));

        film.setIsActive(true);

        // log.info("Active film {}", film.toString());
        Film updatedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(updatedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO deactiveFilm(String filmId) {
        Film film = filmRepository.findByIdDefault(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại",
                        HttpStatus.NOT_FOUND.value()));

        film.setIsActive(false);

        // log.info("Deactive film {}", film.toString());
        Film updatedFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(updatedFilm);
    }

    @Override
    @Transactional
    public FilmResponseDTO restoreFilmById(String filmId) {
        Film film = filmRepository.findByIdDefault(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại",
                        HttpStatus.NOT_FOUND.value()));

        film.setIsDeleted(false);

        // log.info("Restore film {}", film.toString());
        Film restoredFilm = filmRepository.save(film);
        return filmMapper.convertToDTO(restoredFilm);

    }

    @Override
    public InputStream getFileFilm(String filmId) throws GeneralSecurityException, IOException {
        String url = this.checkUserHasPermissionToAccessFile(filmId, false);
        return googleDriveService.getFileAsInputStream(url);
    }

    @Override
    public String checkUserHasPermissionToAccessFile(String filmId, boolean isCheck) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy film", HttpStatus.NOT_FOUND.value());
        });
        if (!isCheck) {
            return film.getFilmUrl();
        }
        User user = authManager.getUserAuthentication();
        if (film.getRentalType() == RentalType.RENTAL) {
            RentedFilm rentedFilm = user.getRentedFilms().stream()
                    .filter(f -> f.getFilm().equals(film))
                    .findFirst()
                    .orElse(null);
            if (rentedFilm == null) {
                throw new CustomException("Bạn chưa thuê phim này", HttpStatus.FORBIDDEN.value());
            } else {
                if (rentedFilm.getMinutesLeft() <= 0) {
                    throw new CustomException("Phim thuê đã hết hạn", HttpStatus.FORBIDDEN.value());
                }
            }
        } else {
            if (user.getRentalPackage() == null) {
                throw new CustomException("Bạn chưa thuê gói nào", HttpStatus.FORBIDDEN.value());
            } else {
                if (user.getRentalPackage().getMinutesLeft() <= 0) {
                    throw new CustomException("Gói thuê đã hết hạn", HttpStatus.FORBIDDEN.value());
                }
            }
        }
        return film.getFilmUrl();
    }

    @Override
    public InputStream getFileSubtitle(String subtitleId) throws GeneralSecurityException, IOException {
        Subtitle subtitle = subtitleRepository.findById(subtitleId).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy phụ đề", HttpStatus.NOT_FOUND.value());
        });
        return googleDriveService.getFileAsInputStream(subtitle.getSubtitleUrl());
    }

    @Override
    public InputStream getFileNarration(String narrationId) throws GeneralSecurityException, IOException {
        Narration narration = narrationRepository.findById(narrationId).orElseThrow(() -> {
            throw new CustomException("Không tìm thấy thuyết minh", HttpStatus.NOT_FOUND.value());
        });
        return googleDriveService.getFileAsInputStream(narration.getNarrationUrl());
    }

    public Double rateFilm(String filmId, RatingRequestDTO ratingRequestDTO) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại",
                        HttpStatus.NOT_FOUND.value()));

        int rating = ratingRequestDTO.getRating();
        if (rating < 1 || rating > 5) {
            throw new CustomException("Điểm đánh giá phải có giá trị từ 1 đến 5",
                    HttpStatus.BAD_REQUEST.value());
        }
        film.getRatings().put(ratingRequestDTO.getIdUser(), rating);
        filmRepository.save(film);

        return getRating(filmId);
    }

    public Double getRating(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Phim với ID " + filmId + " không tồn tại",
                        HttpStatus.NOT_FOUND.value()));

        Map<String, Integer> ratings = film.getRatings();
        if (ratings.isEmpty()) {
            return 0.0;
        }
        double sum = ratings.values().stream().mapToInt(Integer::intValue).sum();
        return sum / ratings.size();
    }

    public List<FilmDTO> getTop5HotFilm() {
        List<Film> top5Films = filmRepository.findTop5Film(PageRequest.of(0, 5));
        return top5Films.stream()
                .map(film -> new FilmDTO(film.getId(), film.getFilmName(), film.getThumbnailUrl()))
                .collect(Collectors.toList());
    }

    public List<String> getGenresOfFilm(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim với ID: " + filmId,
                        HttpStatus.NOT_FOUND.value()));

        return film.getGenres().stream()
                .map(genreId -> genreRepository.findById(genreId)
                        .map(genre -> genre.getGenreName()) // Lấy tên thể loại từ ID
                        .orElse(null)) // Nếu không tìm thấy, trả về null
                .filter(genreName -> genreName != null) // Loại bỏ các giá trị null
                .collect(Collectors.toList());
    }

    public List<String> getActorsOfFilm(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim với ID: " + filmId,
                        HttpStatus.NOT_FOUND.value()));

        // Trả về danh sách diễn viên
        return film.getActors() != null ? film.getActors() : List.of();
    }

    @Transactional
    public void incrementViews(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim với ID: " + filmId,
                        HttpStatus.NOT_FOUND.value()));

        // Tăng số lượt xem
        Integer currentViews = film.getNumberOfViews();
        if (currentViews == null || currentViews == 0) {
            film.setNumberOfViews(1);
        } else {
            film.setNumberOfViews(currentViews + 1);
        }
        filmRepository.save(film);
    }
}
