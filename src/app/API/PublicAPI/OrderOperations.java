package app.API.PublicAPI;

import app.API.JNAenums.OrderType;
import app.API.JNAinterface.NolOrderReportAPI;
import app.API.JNAinterface.NolOrderRequestAPI;

public interface OrderOperations {
    String APIOrderRequest(
            NolOrderRequestAPI nolorderrequest,
            NolOrderReportAPI nolorderreport,
            OrderType Typ);
}
