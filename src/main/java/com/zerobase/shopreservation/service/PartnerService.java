package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.SignUpPartnerInput;

public interface PartnerService {
    ServiceResult signUp(SignUpPartnerInput signUpPartnerInput);
}
