package com.rental.movie.service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rental.movie.common.IAuthentication;
import com.rental.movie.config.AppConfig;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.DeviceResponseDTO;
import com.rental.movie.model.entity.Device;
import com.rental.movie.model.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private UserService userService;
    @Autowired
    private IAuthentication authManager;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void add(String userId, String token, HttpServletRequest request, Instant expireAt, Instant lastLoggedIn) {
        log.info("Adding device for user id: {}", userId);
        User user = userService.getById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    throw new CustomException("Tài khoản của bạn không tồn tại",
                            HttpStatus.NOT_FOUND.value());
                });
        String info = getDeviceDetails(request.getHeader("user-agent"));
        String ip = extractIp(request);
        // check device info
        Device existingDevice = findExistingDevice(user.getDevices(), info, ip);
        //
        if (existingDevice == null) {
            existingDevice = Device.builder()
                    .id(new ObjectId().toString())
                    .token(token)
                    .expireAt(expireAt.atZone(ZoneId.systemDefault()))
                    .info(info)
                    .ip(BCrypt.hashpw(ip, BCrypt.gensalt(appConfig.getLogRounds())))
                    .lastLoggedIn(lastLoggedIn.atZone(ZoneId.systemDefault()))
                    .build();
            user.getDevices().add(existingDevice);
        } else {
            existingDevice.setToken(token);
            existingDevice.setExpireAt(expireAt.atZone(ZoneId.systemDefault()));
            existingDevice.setLastLoggedIn(lastLoggedIn.atZone(ZoneId.systemDefault()));
        }
        userService.save(user);
        log.info("Add new device for user id: {} successfully", userId);
    }

    @Override
    public Page<DeviceResponseDTO> getAll(HttpServletRequest request, Pageable pageable) {
        User user = authManager.getUserAuthentication();
        log.info("Get all devices for user id: {}", user.getId());
        List<Device> devices = user.getDevices();
        String info = getDeviceDetails(request.getHeader("user-agent"));
        String ip = extractIp(request);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), devices.size());
        List<Device> deviceSubList = devices.subList(start, end);
        Page<Device> pageFilm = new PageImpl<>(deviceSubList, pageable, devices.size());

        return pageFilm.map(device -> {
            DeviceResponseDTO deviceResponseDTO = modelMapper.map(device, DeviceResponseDTO.class);
            if (Objects.equals(device.getInfo(), info) && BCrypt.checkpw(ip, device.getIp())) {
                deviceResponseDTO.setIsCurrentDevice(true);
            }
            return deviceResponseDTO;
        });
    }

    @Override
    public void delete(String id, HttpServletRequest request) {
        User user = authManager.getUserAuthentication();
        log.info("Removing device {} for user id: {}", id, user.getId());
        Device device = user.getDevices().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Device not found: {}", id);
                    throw new CustomException("Thiết bị không tồn tại",
                            HttpStatus.NOT_FOUND.value());
                });
        String info = getDeviceDetails(request.getHeader("user-agent"));
        String ip = extractIp(request);
        if (Objects.equals(device.getInfo(), info) && BCrypt.checkpw(ip, device.getIp())) {
            log.error("Cannot remove current device");
            throw new CustomException("Không thể xóa thiết bị hiện tại",
                    HttpStatus.BAD_REQUEST.value());
        }
        user.getDevices().remove(device);
        userService.save(user);
        log.info("Remove device {} for user id: {} successfully", id, user.getId());
    }

    private Device findExistingDevice(List<Device> devices, String info, String ip) {
        return devices.stream()
                .filter(device -> Objects.equals(device.getInfo(), info) && BCrypt.checkpw(ip, device.getIp()))
                .findFirst()
                .orElse(null);
    }

    // Reference:
    // https://github.com/Baeldung/spring-security-registration/blob/master/src/main/java/com/baeldung/service/DeviceService.java

    private String getDeviceDetails(String userAgent) {
        String deviceDetails = "UNKNOWN";
        Parser parser = new Parser();
        Client client = parser.parse(userAgent);
        if (Objects.nonNull(client)) {
            deviceDetails = client.userAgent.family
                    + " " + client.userAgent.major + "."
                    + client.userAgent.minor + " - "
                    + client.os.family + " " + client.os.major
                    + "." + client.os.minor;
        }
        return deviceDetails;
    }

    private String extractIp(HttpServletRequest request) {
        String clientIp;
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");
        if (clientXForwardedForIp != null) {
            clientIp = parseXForwardedHeader(clientXForwardedForIp);
        } else {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }

    private String parseXForwardedHeader(String header) {
        return header.split(" *, *")[0];
    }
}
