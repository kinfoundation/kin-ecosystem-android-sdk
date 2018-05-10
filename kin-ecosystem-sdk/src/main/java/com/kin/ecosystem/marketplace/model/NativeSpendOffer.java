package com.kin.ecosystem.marketplace.model;

import com.kin.ecosystem.network.model.BlockchainData;
import com.kin.ecosystem.network.model.Offer;


public class NativeSpendOffer extends NativeOffer {

    public NativeSpendOffer(String id) {
        super(id);
    }

    public NativeSpendOffer title(String title) {
        this.setTitle(title);
        return this;
    }

    public NativeSpendOffer description(String description) {
        this.setDescription(description);
        return this;
    }

    public NativeSpendOffer amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public NativeSpendOffer image(String image) {
        this.setImage(image);
        return this;
    }

    public NativeSpendOffer recipientAddress(String recipientAddress) {
        this.setBlockchainData(new BlockchainData().recipientAddress(recipientAddress));
        return this;
    }

    @Override
    public Offer offerType(OfferType offerType) {
        return super.offerType(OfferType.SPEND);
    }

    @Override
    public OfferType getOfferType() {
        return OfferType.SPEND;
    }

    @Override
    public void setOfferType(OfferType offerType) {
        super.setOfferType(OfferType.SPEND);
    }
}