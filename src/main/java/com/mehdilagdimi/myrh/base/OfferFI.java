package com.mehdilagdimi.myrh.base;

import com.mehdilagdimi.myrh.model.entity.Offer;

@FunctionalInterface
public interface OfferFI {
    void update(Offer offer);
}
