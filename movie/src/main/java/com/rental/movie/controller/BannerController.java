package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.BannerCreationDTO;
import com.rental.movie.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @Operation(summary = "Get all banners")
    @ApiResponse(responseCode = "200", description = "OK.")
    @GetMapping("/getAll")
    public ResponseEntity<BaseResponse> getAllBanners() {
        return bannerService.getAllBanners();
    }

    @Operation(summary = "Create a new Banner", description = "Required inputs: imageUrl (String), idFilm (String)")
    @ApiResponse(responseCode = "201", description = "Create a new Banner successfully.")
    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createBanner(@RequestBody @Valid BannerCreationDTO bannerCreationDTO){
        return bannerService.createBanner(bannerCreationDTO);
    }

    @Operation(summary = "Update a banner by id", description = "Just update 3 fields: imageUrl (String), idFilm (String), isActive (boolean)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Banner not found!"),
            @ApiResponse(responseCode = "200", description = "Updated banner successfully.")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse> updateById(@PathVariable String id,
                                                   @RequestBody @Valid BannerCreationDTO newBanner){
        return bannerService.updateBanner(id, newBanner);
    }

    @Operation(summary = "Soft delete a banner by id", description = "Updated field: isDeleted = true")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Banner not found."),
            @ApiResponse(responseCode = "200", description = "Soft deleted banner successfully.")
    })
    @PatchMapping("/softDelete/{id}")
    public ResponseEntity<BaseResponse> softDeleteById(@PathVariable String id) {
        return bannerService.softDeleteById(id);
    }

    @Operation(summary = "Find banner by input (Film name or Film id)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Failed! Banners not found!"),
            @ApiResponse(responseCode = "200", description = "OK! Banners found!")
    })
    @GetMapping("/findBanner")
    public ResponseEntity<BaseResponse> findBannerByFilmName_orByFilmId(@RequestParam String input) {
        return bannerService.findByFilmNameOrFilmId(input);
    }

    @Operation(summary = "Get Soft Deleted Banners")
    @ApiResponse(responseCode = "200", description = "OK.")
    @GetMapping("/getSoftDeletedBanners")
    public ResponseEntity<BaseResponse> getSoftDeletedBanners() {
        return bannerService.getSoftDeletedBanners();
    }

    @Operation(summary = "Restore a banner by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Failed! Banner not found."),
            @ApiResponse(responseCode = "200", description = "OK! Restore successfully.")
    })
    @PutMapping("/restore/{id}")
    public ResponseEntity<BaseResponse> restoreById(@PathVariable String id) {
        return bannerService.restoreBannerById(id);
    }

}
