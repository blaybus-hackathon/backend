package com.balybus.galaxy.center.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TblCenterManaegerUpdateResponseDTO {
    String cmName;
    String cmEmail;
}
