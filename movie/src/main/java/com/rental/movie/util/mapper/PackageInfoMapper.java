package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.PackageInfoRequestDTO;
import com.rental.movie.model.dto.PackageInfoResponseDTO;
import com.rental.movie.model.entity.PackageInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackageInfoMapper {
    @Autowired
    private ModelMapper modelMapper;
    public PackageInfo convertToEntity(PackageInfoRequestDTO packageInfoRequestDTO){
        return packageInfoRequestDTO == null ? null : modelMapper.map(packageInfoRequestDTO, PackageInfo.class);
    }
    public PackageInfoResponseDTO convertToDTO(PackageInfo packageInfo){
        if (packageInfo == null)
            return null;
        PackageInfoResponseDTO pktResponse = modelMapper.map(packageInfo, PackageInfoResponseDTO.class);
        pktResponse.setCreatedAt(packageInfo.getCreatedAt().toInstant());
        pktResponse.setUpdatedAt(packageInfo.getUpdatedAt().toInstant());
        return pktResponse;
    }
}
