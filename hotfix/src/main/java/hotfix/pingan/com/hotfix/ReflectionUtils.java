package hotfix.com.pa;

/**
 * Created by ndh on 16/4/8.
 */

import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by jixin.jia on 15/10/31.
 * modify by ndh
 */
public class ReflectionUtils {
    public static Object getField(Object obj, Class<?> cl, String field)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        Log.i("Fix", "getField obj=" + obj.getClass().getName() + ",obj=" + obj.toString() + "cl=" + cl.getName() + "filed=" + field + "ret=" + localField.get(obj).getClass().getName());
        return localField.get(obj);
    }

    public static void setField(Object obj, Class<?> cl, String field, Object value)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        Log.i("Fix", "setField obj=" + obj.getClass().getName() + ",obj=" + obj.toString() + "cl=" + cl.getName() + "filed=" + field + "ret=" + localField);

        localField.set(obj, value);
    }
}
