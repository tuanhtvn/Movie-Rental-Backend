package com.rental.movie.service;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.PackageInfoRequestDTO;
import com.rental.movie.model.dto.PackageInfoResponseDTO;
import com.rental.movie.model.entity.PackageInfo;
import com.rental.movie.repository.PackageInfoRepository;
import com.rental.movie.util.mapper.PackageInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PackageInfoServiceImpl implements PackageInfoService {
    @Autowired
    private PackageInfoRepository packageInfoRepository;
    @Autowired
    private PackageInfoMapper packageInfoMapper;

    @Override
    public ResponseEntity<BaseResponse> createPackage(PackageInfoRequestDTO packageInfoRequestDTO) {
        PackageInfo packageInfo = packageInfoMapper.convertToEntity(packageInfoRequestDTO);
        packageInfoRepository.save(packageInfo);
        return ResponseEntity.ok().body(
                new BaseResponse("Tạo mới gói thành công.",
                        HttpStatus.OK.value(),
                        packageInfoMapper.convertToDTO(packageInfo))
        );
    }

    @Override
    public ResponseEntity<BaseResponse> getAllPackage(Pageable pageable) {
        Page<PackageInfoResponseDTO> packageInfoResponseDTOPage = packageInfoRepository.findByIsDeletedFalse(pageable)
                .map(packageInfoMapper::convertToDTO);
        if (packageInfoResponseDTOPage.isEmpty()) {
            return ResponseEntity.ok().body(
                    new BaseResponse("Không tìm thấy gói nào.",
                            HttpStatus.NOT_FOUND.value(),
                            null)
            );
        }
        return ResponseEntity.ok().body(
                new BaseResponse("Tìm thấy danh sách gói.",
                        HttpStatus.OK.value(),
                        packageInfoResponseDTOPage.getContent())
        );
    }

    @Override
    public ResponseEntity<BaseResponse> updatePackageById(String id, PackageInfoRequestDTO packageInfoRequestDTO) {
        PackageInfo pkt = packageInfoRepository.findById(id).orElse(null);
        if (pkt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy gói.", HttpStatus.NOT_FOUND.value(), null)
            );
        }
        pkt.setPackageName(packageInfoRequestDTO.getPackageName());
        pkt.setDescription(packageInfoRequestDTO.getDescription());
        pkt.setPrice(packageInfoRequestDTO.getPrice());
        pkt.setTimeDuration(packageInfoRequestDTO.getTimeDuration());
        pkt.setIsActive(packageInfoRequestDTO.getIsActive());
        packageInfoRepository.save(pkt);

        return ResponseEntity.ok().body(
                new BaseResponse("Cập nhật gói thành công.",
                        HttpStatus.OK.value(),
                        packageInfoMapper.convertToDTO(pkt))
        );
    }

    @Override
    public ResponseEntity<BaseResponse> softDeletePackageById(String id) {
        PackageInfo pkt = packageInfoRepository.findById(id).orElse(null);
        if (pkt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy gói.", HttpStatus.NOT_FOUND.value(), null)
            );
        }
        pkt.setIsDeleted(true);
        packageInfoRepository.save(pkt);

        return ResponseEntity.ok().body(
                new BaseResponse("Xoá mềm gói thành công.",
                        HttpStatus.OK.value(),
                        packageInfoMapper.convertToDTO(pkt))
        );
    }

    @Override
    public ResponseEntity<BaseResponse> toggleActiveStatus(String id) {
        Optional<PackageInfo> pkt = packageInfoRepository.findById(id);
        if (pkt.isPresent()) {
            PackageInfo newPkt = pkt.get();
            newPkt.setIsActive(!newPkt.getIsActive());

            String message = "Cập nhật thành công. Trạng thái hiện tại: "
                    + (newPkt.getIsActive() ? "Active" : "Inactive");
            PackageInfoResponseDTO resposePkt = packageInfoMapper.convertToDTO(
                    packageInfoRepository.save(newPkt));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(message, HttpStatus.OK.value(), resposePkt)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy gói.", HttpStatus.NOT_FOUND.value(), null)
            );
        }
    }

    @Override
    public ResponseEntity<BaseResponse> getSoftDeletedPackages(Pageable pageable) {
        Page<PackageInfoResponseDTO> pktDTO = packageInfoRepository.findByIsDeletedTrue(pageable)
                .map(packageInfoMapper::convertToDTO);
        if (pktDTO.isEmpty()) {
            return ResponseEntity.ok(
                    new BaseResponse("Không tìm thấy gói nào.", HttpStatus.OK.value(), null)
            );
        }
        return ResponseEntity.ok(
                new BaseResponse("Tìm thấy danh sách gói.", HttpStatus.OK.value(), pktDTO.getContent())
        );
    }

    @Override
    public ResponseEntity<BaseResponse> restoreById(String id) {
        Optional<PackageInfo> pkt = packageInfoRepository.findById(id);
        if (pkt.isPresent()) {
            PackageInfo newPkt = pkt.get();
            newPkt.setIsDeleted(false);

            PackageInfoResponseDTO responsePkt = packageInfoMapper.convertToDTO(
                    packageInfoRepository.save(newPkt));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse("Khôi phục gói thành công.",
                            HttpStatus.OK.value(),
                            responsePkt)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy gói.", HttpStatus.NOT_FOUND.value(), null)
            );
        }
    }

    @Override
    public ResponseEntity<BaseResponse> getAllActiveOrInactive(Pageable pageable, Boolean flag) {
        Page<PackageInfoResponseDTO> pktDTO = packageInfoRepository.findByIsActiveAndIsDeletedFalse(pageable, flag)
                .map(packageInfoMapper::convertToDTO);
        if (pktDTO.isEmpty()) {
            return ResponseEntity.ok(
                    new BaseResponse("Không tìm thấy gói nào.", HttpStatus.OK.value(), null)
            );
        }
        return ResponseEntity.ok(
                new BaseResponse("Tìm thấy danh sách gói.", HttpStatus.OK.value(), pktDTO.getContent())
        );
    }

    @Override
    public ResponseEntity<BaseResponse> findByNameContaining(Pageable pageable, String pktName) {
        String trimmedInput = pktName.trim();
        Page<PackageInfoResponseDTO> pktDTO = packageInfoRepository
                .findByPackageNameContainingIgnoreCase(pageable, trimmedInput)
                .map(packageInfoMapper::convertToDTO);
        if (pktDTO.isEmpty()){
            return ResponseEntity.ok(
                    new BaseResponse("Không tìm thấy gói nào.", HttpStatus.NOT_FOUND.value(), null)
            );
        }
        return ResponseEntity.ok(
                new BaseResponse("Tìm thấy danh sách gói.", HttpStatus.OK.value(), pktDTO.getContent())
        );
    }

    @Override
    public ResponseEntity<BaseResponse> getActiveAndNotDeletedById(String id) {
        PackageInfo packageInfo = packageInfoRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(id);
        if (packageInfo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse("Không tìm thấy gói.", HttpStatus.NOT_FOUND.value(), null)
            );
        }
        PackageInfoResponseDTO responseDTO = packageInfoMapper.convertToDTO(packageInfo);
        return ResponseEntity.ok(
                new BaseResponse("Tìm thấy gói.", HttpStatus.OK.value(), responseDTO)
        );
    }
}
