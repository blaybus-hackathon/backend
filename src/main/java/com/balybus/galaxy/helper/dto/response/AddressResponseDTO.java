package com.balybus.galaxy.helper.dto.response;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AddressResponseDTO {
    private List<TblAddressFirst> addressFirstList;
    private List<TblAddressSecond> addressSecondList;
    private List<TblAddressThird> addressThirdList;
}
