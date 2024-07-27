package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.RentalPackageResponseDTO;
import com.rental.movie.model.entity.PackageInfo;
import com.rental.movie.model.entity.RentalPackage;
import com.rental.movie.repository.PackageInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RentalPackageMapper {
    @Autowired
    private PackageInfoRepository packageInfoRepository;

    public RentalPackageResponseDTO convertToDTO(RentalPackage rentalPackage){
        if (rentalPackage == null) {
            return null;
        }
        RentalPackageResponseDTO dto = new RentalPackageResponseDTO();
        dto.setRegistrationDate(rentalPackage.getRegistrationDate());
        dto.setExpirationDate(rentalPackage.getExpirationDate());
        dto.setIsRenewal(rentalPackage.getIsRenewal());
        dto.setMinutesLeft(rentalPackage.getMinutesLeft());

        PackageInfo packageInfo = packageInfoRepository.findById(rentalPackage.getPackageInfo().getId()).orElse(null);
        dto.setPackageInfo(packageInfo);

        return dto;
    }
}
