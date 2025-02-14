package com.balybus.galaxy.global.config.jwt;

import com.balybus.galaxy.domain.tblImg.TblImgServiceImpl;
import com.balybus.galaxy.domain.tblImg.dto.ImgRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/protected")
public class ProtectedController {
    private final TblImgServiceImpl imgService;

    @GetMapping("/user")
    public String getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return "Authenticated user: " + userDetails.getUsername();
    }

    @PostMapping("/upload-img")
    public ResponseEntity<?> uploadImg(@AuthenticationPrincipal UserDetails userDetails,
                                       ImgRequestDto.UploadImg dto) {
        return ResponseEntity.ok().body(imgService.uploadImg(userDetails, dto));
    }
}
