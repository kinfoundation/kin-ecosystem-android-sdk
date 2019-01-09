package com.kin.ecosystem.transfer.view;

import com.kin.ecosystem.base.IBaseView;
import com.kin.ecosystem.transfer.presenter.AccountInfoPresenter;

public interface IAccountInfoView extends IBaseView<AccountInfoPresenter> {
    void enabledAgreeButton();
    void closeActivity();
}
