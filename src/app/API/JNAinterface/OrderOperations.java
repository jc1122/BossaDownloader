package app.API.JNAinterface;

import app.API.enums.OrderType;

public interface OrderOperations {
    String APIOrderRequest(
            NolOrderRequestAPI nolorderrequest,
            NolOrderReportAPI nolorderreport,
            OrderType Typ);
}
