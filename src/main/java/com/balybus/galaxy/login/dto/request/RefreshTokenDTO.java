package com.balybus.galaxy.login.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RefreshTokenDTO {
    private String refreshToken;
}
