package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.PaymentInfoRepuestDTO;
import com.rental.movie.model.dto.PaymentInfoResponseDTO;
import com.rental.movie.model.entity.PaymentInfo;
import com.rental.movie.model.entity.User;
import com.rental.movie.util.mapper.PaymentInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class PaymentInfoServiceImpl implements PaymentInfoService{
    @Autowired
    private UserService userService;
    @Autowired
    private IAuthentication authManager;
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Override
    public List<PaymentInfoResponseDTO> getAll() {
        User user = authManager.getUserAuthentication();
        log.info("Get all paymentInfos for user id: {}",user.getId());
        return user.getPaymentInfos().stream()
                .map(paymentInfo -> paymentInfoMapper.convertToDTO(paymentInfo))
                .toList();
    }

    @Override
    public PaymentInfoResponseDTO get(String paymentInfoId) {
        User user = authManager.getUserAuthentication();
        log.info("Get paymentInfo id: {} for user id: {}",paymentInfoId,user.getId());
        PaymentInfo paymentInfo = getById(paymentInfoId,user);
        return paymentInfoMapper.convertToDTO(paymentInfo);
    }

    @Override
    public List<PaymentInfoResponseDTO> getAllSoftDelete() {
        User user = authManager.getUserAuthentication();
        log.info("Get all soft delete paymentInfos for user id: {}", user.getId());

        return user.getPaymentInfos().stream()
                .filter(PaymentInfo::getIsDeleted) // Chỉ lấy các PaymentInfo đã bị xóa mềm
                .map(paymentInfoMapper::convertToDTO)
                .toList();
    }

    @Override
    public List<PaymentInfoResponseDTO> getAllActive() {
        User user = authManager.getUserAuthentication();
        log.info("Get all active paymentInfos for user id: {}", user.getId());

        return user.getPaymentInfos().stream()
                .filter(paymentInfo -> paymentInfo.getIsActive() && !paymentInfo.getIsDeleted())
                .map(paymentInfoMapper::convertToDTO)
                .toList();
    }

    @Override
    public List<PaymentInfoResponseDTO> getAllInActive() {
        User user = authManager.getUserAuthentication();
        log.info("Get all inactive paymentInfos for user id: {}", user.getId());

        return user.getPaymentInfos().stream()
                .filter(paymentInfo -> !paymentInfo.getIsActive() && !paymentInfo.getIsDeleted())
                .map(paymentInfoMapper::convertToDTO)
                .toList();
    }

    @Override
    public PaymentInfoResponseDTO addPaymentInfo(PaymentInfoRepuestDTO paymentInfoRepuestDTO) {
        User user = authManager.getUserAuthentication();
        log.info("Add new paymentInfo for user id:{}",user.getId());
        PaymentInfo paymentInfo = paymentInfoMapper.convertToEntity(paymentInfoRepuestDTO);
        paymentInfo.setId(new ObjectId().toString());
        user.getPaymentInfos().add(paymentInfo);
        userService.save(user);
        log.info("Add new paymentInfo for user id:{} successfully",user.getId());
        return paymentInfoMapper.convertToDTO(paymentInfo);
    }

    @Override
    public PaymentInfoResponseDTO update(String paymentInfoId, PaymentInfoRepuestDTO paymentInfoRepuestDTO) {
        User user = authManager.getUserAuthentication();
        log.info("Update paymentInfo id: {} for user id: {}",paymentInfoId,user.getId());
        PaymentInfo paymentInfo = getById(paymentInfoId,user);
        paymentInfo = paymentInfoMapper.convertToEntity(paymentInfoRepuestDTO,paymentInfo);
        paymentInfo.setUpdatedAt(Instant.now());
        userService.save(user);
        log.info("Update paymentInfo id:{} for user id:{} successfully",paymentInfo,user.getId());
        return paymentInfoMapper.convertToDTO(paymentInfo);
    }

    @Override
    public void softDeletePaymentInfo(String paymentInfoId) {
        User user = authManager.getUserAuthentication();
        PaymentInfo paymentInfo = getById(paymentInfoId,user);
        paymentInfo.setIsDeleted(true);
        userService.save(user);
        log.info("Soft delete paymentInfo successfully");
    }

    @Override
    public void restorePaymentInfo(String paymentInfoId) {
        User user = authManager.getUserAuthentication();
        PaymentInfo paymentInfo = getById(paymentInfoId,user);
        paymentInfo.setIsDeleted(false);
        userService.save(user);
        log.info("Restored paymentInfo with id: {}", paymentInfoId);
    }

    @Override
    public void activatePaymentInfo(String paymentInfoId) {
        User user = authManager.getUserAuthentication();
        PaymentInfo paymentInfo = getById(paymentInfoId,user);
        paymentInfo.setIsActive(true);
        userService.save(user);
        log.info("Activated paymentInfo with id: {}", paymentInfoId);
    }

    @Override
    public void deAvtivatePaymentInfo(String paymentInfoId) {
        User user = authManager.getUserAuthentication();
        PaymentInfo paymentInfo = getById(paymentInfoId,user);
        paymentInfo.setIsActive(false);
        userService.save(user);
        log.info("Deactivated paymentInfo with id: {}", paymentInfoId);
    }

    private PaymentInfo getById(String paymentInfoId,User user){
        return user.getPaymentInfos().stream()
                .filter(paymentInfo -> paymentInfo.getId().equals(paymentInfoId))
                .findFirst()
                .orElseThrow(()->{
                    log.error("PaymentInfoId: {} not found",paymentInfoId);
                    throw new CustomException("Không tìm thấy thông tin thanh toán.", HttpStatus.NOT_FOUND.value());
                });
    }
}
