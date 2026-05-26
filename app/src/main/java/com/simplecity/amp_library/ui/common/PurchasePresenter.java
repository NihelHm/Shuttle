package com.simplecity.amp_library.ui.common;

import com.simplecity.amp_library.ui.common.Presenter;
import com.simplecity.amp_library.ui.views.PurchaseView;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class PurchasePresenter<V extends PurchaseView> extends Presenter<V> { //NOSONAR

    public void upgradeClicked() { //NOSONAR
        PurchaseView purchaseView = getView(); //NOSONAR
        if (purchaseView != null) { //NOSONAR
            purchaseView.showUpgradeDialog(); //NOSONAR
        }
    }
}
