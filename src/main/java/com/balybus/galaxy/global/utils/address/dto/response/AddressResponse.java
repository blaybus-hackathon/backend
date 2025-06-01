package com.balybus.galaxy.global.utils.address.dto.response;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AddressResponse {
    private List<TblAddressFirst> addressFirstList;
}
