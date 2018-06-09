package app.API.JNAinterface;

import app.API.JNAenums.OrderType;

interface OrderOperations {
    String APIOrderRequest(
            NolOrderRequestAPI nolorderrequest,
            NolOrderReportAPI nolorderreport,
            OrderType Typ);
}
