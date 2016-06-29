package hotfix.com.pa;

/**
 * Created by ndh on 16/4/8.
 */

import android.os.Build;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by jixin.jia
 * modify by ndh
 */
public class DexUtils {
    /**
     *
     * @param dexPath 待加载的dex文件
     * @param defaultDexOptPath dex文件在系统转odex时需要存放的文件夹
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static final boolean API_LEVER_FLAG= Build.VERSION.SDK_INT >= 14 ;
    public static void injectDexAtFirst(String dexPath, String defaultDexOptPath) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        if(API_LEVER_FLAG){
            injectDexAPI14_(dexPath, defaultDexOptPath);
        }else{
            injectDexAPI_14(dexPath, defaultDexOptPath);
        }

    }
    public static void injectDexAPI14_(String dexPath, String defaultDexOptPath) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, defaultDexOptPath, dexPath, getPathClassLoader());

        Object baseDexElements = getDexElements(getPathList(getPathClassLoader()));

        Object newDexElements = getDexElements(getPathList(dexClassLoader));

        Object allDexElements = combineArray(newDexElements, baseDexElements);

        Object pathList = getPathList(getPathClassLoader());
        ReflectionUtils.setField(pathList, pathList.getClass(), "dexElements", allDexElements);
    }
    public static void injectDexAPI_14(String dexPath, String defaultDexOptPath) throws NoSuchFieldException, IllegalAccessException {
        DexClassLoader dexClassLoader=new DexClassLoader(dexPath,defaultDexOptPath,dexPath,getPathClassLoader());
        //拿取patchClassLoader的mDexs字段
        Object baseMdexs= getFieldmDexs(getPathClassLoader());
        //拿取dexClassLoader的mDexs字段
        Object newMdexs= getFieldmDexs(dexClassLoader);
        //将补丁dex文件插入到数组前端
        Object allMdexs=combineArray(newMdexs,baseMdexs);
        //将合并后的mDesx赋值给patchClassLoader
        hotfix.com.pa.ReflectionUtils.setField(getPathClassLoader(), getPathClassLoader().getClass(), "mDexs", allMdexs);
    }
//获取dalvik.system.PathClassLoader
    private static PathClassLoader getPathClassLoader() {
        PathClassLoader pathClassLoader = (PathClassLoader)hotfix.com.pa.DexUtils.class.getClassLoader();
        return pathClassLoader;
    }
//dalvik.system.DexPathList
    private static Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
      //拿取Element字段
        return hotfix.com.pa.ReflectionUtils.getField(paramObject, paramObject.getClass(), "dexElements");
    }
//dalvik.system.DexClassLoader
    private static Object getPathList(Object baseDexClassLoader)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
//拿到baseDexClassLoader 的DexPatchList字段//因为所有findClass由此提供
        return ReflectionUtils.getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object combineArray(Object firstArray, Object secondArray) {
        Class<?> localClass = firstArray.getClass().getComponentType();
        int firstArrayLength = Array.getLength(firstArray);
        int allLength = firstArrayLength + Array.getLength(secondArray);
        Object result = Array.newInstance(localClass, allLength);
        for (int k = 0; k < allLength; ++k) {
            if (k < firstArrayLength) {
                Array.set(result, k, Array.get(firstArray, k));
            } else {
                Array.set(result, k, Array.get(secondArray, k - firstArrayLength));
            }
        }
        return result;
    }
//api14以下
    private static Object getFieldmDexs(Object paramObject) throws NoSuchFieldException, IllegalAccessException {
        return hotfix.com.pa.ReflectionUtils.getField(paramObject, paramObject.getClass(), "mDexs");
    }

}
