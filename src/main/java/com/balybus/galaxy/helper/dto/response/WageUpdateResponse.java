package com.balybus.galaxy.helper.dto.response;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WageUpdateResponse {
    private String email;
    private int wage;
    private int wageState;
}
