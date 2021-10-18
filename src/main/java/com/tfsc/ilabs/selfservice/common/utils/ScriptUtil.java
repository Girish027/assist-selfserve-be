package com.tfsc.ilabs.selfservice.common.utils;

import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by ravi.b on 24-07-2019.
 */
public class ScriptUtil {

    private ScriptUtil() {
        // hide implicit public constructor
    }

    public static String invokeFunction(String javascriptCode, String methodName, Object... args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(Constants.JAVASCRIPT);
        String res = "";

        try {
            engine.eval(javascriptCode);
            if (!BaseUtil.isNullOrBlank(methodName) && engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;
                res = (String) invoke.invokeFunction(methodName, args);
            }
        } catch (NoSuchMethodException ex) {
            throw new SelfServeException("No method with name " + methodName + " found in javascript code :" + ex.getMessage());
        } catch (Exception ex) {
            throw new SelfServeException("Got exception while running javascript code :" + ex.getMessage() + "\n" + ex.toString());
        }

        return res;
    }

    /**
     * This method translates apiResponse based on javascript fucntion and converts to Required Java Object based on classType
     * @param javascriptCode
     * @param methodName
     * @param classType
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T invokeFunction(String javascriptCode, String methodName, Class<T> classType, Object... args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(Constants.JAVASCRIPT);
        T response = null;
        try {
            engine.eval(javascriptCode);
            if (!BaseUtil.isNullOrBlank(methodName) && engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;
                Object scriptResponse = invoke.invokeFunction(methodName, args);
                response = StringUtils.valueAsObject(StringUtils.valueAsString(scriptResponse), classType);
            }
        } catch (NoSuchMethodException ex) {
            throw new SelfServeException("No method with name " + methodName + " found in javascript code :" + ex.getMessage());
        } catch (Exception ex) {
            throw new SelfServeException("Got exception while running javascript code :" + ex.getMessage() + "\n" + ex.toString());
        }

        return response;
    }
}
