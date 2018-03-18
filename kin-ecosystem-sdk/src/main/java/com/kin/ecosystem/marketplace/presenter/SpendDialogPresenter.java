package com.kin.ecosystem.marketplace.presenter;

import android.os.Handler;
import android.util.Log;
import com.kin.ecosystem.Callback;
import com.kin.ecosystem.base.BasePresenter;
import com.kin.ecosystem.data.blockchain.BlockchainSource;
import com.kin.ecosystem.data.blockchain.IBlockchainSource;
import com.kin.ecosystem.data.order.OrderDataSource;
import com.kin.ecosystem.marketplace.view.ISpendDialog;
import com.kin.ecosystem.network.model.Offer;
import com.kin.ecosystem.network.model.OfferInfo;
import com.kin.ecosystem.network.model.OfferInfo.Confirmation;
import com.kin.ecosystem.network.model.OpenOrder;
import com.kin.ecosystem.network.model.Order;
import java.math.BigDecimal;


public class SpendDialogPresenter extends BasePresenter<ISpendDialog> implements ISpendDialogPresenter {

    private static final String TAG = SpendDialogPresenter.class.getSimpleName();

    private final OrderDataSource orderRepository;
    private final IBlockchainSource blockchainSource;

    private final Handler handler = new Handler();

    private final OfferInfo offerInfo;
    private final Offer offer;
    private OpenOrder openOrder;
    private boolean isDismissed;

    private static final int CLOSE_DELAY = 300;

    public SpendDialogPresenter(OfferInfo offerInfo, Offer offer, BlockchainSource blockchainSource,
        OrderDataSource orderRepository) {
        this.offerInfo = offerInfo;
        this.offer = offer;
        this.orderRepository = orderRepository;
        this.blockchainSource = blockchainSource;
    }

    @Override
    public void onAttach(final ISpendDialog view) {
        super.onAttach(view);
        isDismissed = false;
        createOrder();
        loadInfo();
    }

    private void createOrder() {
        orderRepository.createOrder(offer.getId(), new Callback<OpenOrder>() {
            @Override
            public void onResponse(OpenOrder response) {
                openOrder = response;
                showToast("OpenOrder: " + openOrder.getId());
            }

            @Override
            public void onFailure(Throwable t) {
                showToast("Oops something went wrong...");
            }
        });
    }

    private void loadInfo() {
        if (view != null) {
            view.setupImage(offerInfo.getImage());
            view.setupTitle(offerInfo.getTitle(), offerInfo.getAmount());
            view.setupDescription(offerInfo.getDescription());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isDismissed = true;
    }

    @Override
    public void closeClicked() {
        closeDialog();
    }

    private void closeDialog() {
        if (view != null && !isDismissed) {
            isDismissed = true;
            view.closeDialog();
        }
    }

    @Override
    public void confirmClicked() {
        if (view != null) {

            if (openOrder != null) {
                final String addressee = offer.getBlockchainData().getRecipientAddress();
                final BigDecimal amount = new BigDecimal(offer.getAmount());
                final String orderID = openOrder.getId();

                sendTransaction(addressee, amount, orderID);
                submitOrder(offer.getId(), orderID);
            }

            Confirmation confirmation = offerInfo.getConfirmation();
            view.showThankYouLayout(confirmation.getTitle(), confirmation.getDescription());
            closeDialogWithDelay(CLOSE_DELAY);
        }
    }

    private void closeDialogWithDelay(int delayMilliseconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeDialog();
            }
        }, delayMilliseconds);
    }

    private void sendTransaction(String addressee, BigDecimal amount, String orderID) {
        blockchainSource.sendTransaction(addressee, amount, orderID);
    }

    private void submitOrder(String offerID, String orderID) {
        orderRepository.submitOrder(offerID, null, orderID, new Callback<Order>() {
            @Override
            public void onResponse(Order response) {
                Log.i(TAG, "onResponse: " + response);
            }

            @Override
            public void onFailure(Throwable t) {
                //TODO handle failure
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void showToast(String msg) {
        if (view != null) {
            view.showToast(msg);
        }
    }
}