package app.API.JNAinterface;

import app.API.enums.EnumConverter;
import app.API.enums.JnaEnum;
import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

class BossaAPIInstance {
    static final BossaAPIInterface INSTANCE; //TODO make private
    private static final Logger logger =
            Logger.getLogger(BossaAPIInstance.class.getName());
    static {
        logger.finest("Initializing static");

        Map<String, String> functionNames = new HashMap<>();

        //map method names to mangled dll library function names
        functionNames.put("APIOrderRequest", "_APIOrderRequest@12");
        functionNames.put("AddToFilter", "_AddToFilter@8");
        functionNames.put("ClearFilter", "_ClearFilter@0");
        functionNames.put("GetResultCodeDesc", "_GetResultCodeDesc@4");
        functionNames.put("GetTickers", "_GetTickers@12");
        functionNames.put("Get_Version", "_Get_Version@0");
        functionNames.put("InitListTickers", "_InitListTickers@0");
        functionNames.put("Initialize", "_Initialize@4");
        functionNames.put("ReleaseTickersList", "_ReleaseTickersList@4");
        functionNames.put("RemFromFilter", "_RemFromFilter@8");
        functionNames.put("SetCallback", "_SetCallback@4");
        functionNames.put("SetCallbackAccount", "_SetCallbackAccount@4");
        functionNames.put("SetCallbackDelay", "_SetCallbackDelay@4");
        functionNames.put("SetCallbackOrder", "_SetCallbackOrder@4");
        functionNames.put("SetCallbackOutlook", "_SetCallbackOutlook@4");
        functionNames.put("SetCallbackStatus", "_SetCallbackStatus@4");
        functionNames.put("SetTradingSess", "_SetTradingSess@4");
        functionNames.put("Shutdown", "_Shutdown@0");

        Map<String, Object> options = new HashMap<>();
        options.put(Library.OPTION_FUNCTION_MAPPER,
                (FunctionMapper) (library, method) -> functionNames.get(method.getName()));

        options.put(Library.OPTION_TYPE_MAPPER, new DefaultTypeMapper() {
            {
                addTypeConverter(JnaEnum.class, new EnumConverter());
            }
        });
        try {
            INSTANCE = (BossaAPIInterface)
                    Native.loadLibrary("nolclientapi",
                            BossaAPIInterface.class,
                            options);
        } catch (UnsatisfiedLinkError e) {
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTrace.append(element).append(System.lineSeparator());
            }
            logger.severe(e.getMessage()
                    + "\n please put nolclientapi.dll to your windows/SysWOW64 folder and use x86 Java runtime \n"
                    + stackTrace);
            throw e;
        }

        logger.finest("Finished initializing static");
    }

}
