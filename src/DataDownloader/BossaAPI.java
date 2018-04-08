package datadownloader;

import com.sun.jna.*;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * BossaAPI TODO dodaj to do modelu
 */
@SuppressWarnings({"WeakerAccess", "Convert2Lambda", "unused"})
enum BossaAPI {
    ;
    private static BossaAPIInterface apiInstance = null;

    static {
        Map<String,String> functionNames = new HashMap<>();

        functionNames.put("APIOrderRequest","_APIOrderRequest@12");
        functionNames.put("AddToFilter","_AddToFilter@8");
        functionNames.put("ClearFilter","_ClearFilter@0");
        functionNames.put("GetResultCodeDesc","_GetResultCodeDesc@4");
        functionNames.put("GetTickers","_GetTickers@12");
        functionNames.put("Get_Version","_Get_Version@0");
        functionNames.put("InitListTickers","_InitListTickers@0");
        functionNames.put("Initialize","_Initialize@4");
        functionNames.put("ReleaseTickersList","_ReleaseTickersList@4");
        functionNames.put("RemFromFilter","_RemFromFilter@8");
        functionNames.put("SetCallback","_SetCallback@4");
        functionNames.put("SetCallbackAccount","_SetCallbackAccount@4");
        functionNames.put("SetCallbackDelay","_SetCallbackDelay@4");
        functionNames.put("SetCallbackOrder","_SetCallbackOrder@4");
        functionNames.put("SetCallbackOutlook","_SetCallbackOutlook@4");
        functionNames.put("SetCallbackStatus","_SetCallbackStatus@4");
        functionNames.put("SetTradingSess","_SetTradingSess@4");
        functionNames.put("Shutdown","_Shutdown@0");

        Map<String, Object> options = new HashMap<>();
        options.put(Library.OPTION_FUNCTION_MAPPER, new FunctionMapper() {
            public String getFunctionName(NativeLibrary library, Method method) {
                return functionNames.get(method.getName());
            }
        });
        options.put(Library.OPTION_TYPE_MAPPER, new DefaultTypeMapper() {
            {
                addTypeConverter(JnaEnum.class, new EnumConverter());
            }
        });

        apiInstance = (BossaAPIInterface)
                Native.loadLibrary("nolclientapi",
                        BossaAPIInterface.class,
                        options);
    }

    @Contract(pure = true)
    static BossaAPIInterface getApiInstance() {
        return apiInstance;
    }

}
