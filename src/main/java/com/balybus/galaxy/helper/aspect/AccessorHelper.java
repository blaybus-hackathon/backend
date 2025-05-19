package com.balybus.galaxy.helper.aspect;

import com.balybus.galaxy.helper.domain.TblHelper;

public class AccessorHelper {
    private final TblHelper helper;

    public AccessorHelper(TblHelper helper) {
        this.helper = helper;
    }

    public TblHelper getHelper() {
        return helper;
    }
}