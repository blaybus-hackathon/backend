package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AddressResponseDTO {
    private List<TblAddressFirst> addressFirstList;
}
