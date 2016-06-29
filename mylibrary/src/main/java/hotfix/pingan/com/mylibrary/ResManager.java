package hotfix.pingan.com.mylibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by ndh on 16/5/5.
 */
public class ResManager {
    private static Resources res;
    private static String jarDir = Environment.getExternalStorageDirectory() + "/";
    private static String jarPath = jarDir + "mm.jar";

    private ResManager() {
    }

    public static class SingleHolder {
        private static final ResManager instance = new ResManager();
    }

    public static ResManager getInstance() {
        return SingleHolder.instance;
    }

    public void init(Context context, String resPatch) {
        Log.i("SSLoad", "ll=" + copyJarToSD(context).exists());
        initResources(context, getAssetManager(copyJarToSD(context).getAbsolutePath()));
    }

    /**
     * @param resPatch the res you need to added
     * @return
     */
    private AssetManager getAssetManager(String resPatch) {
        AssetManager instance = null;
        try {
            Class<?> cls = Class.forName("android.content.res.AssetManager");
            Constructor<?> assetMagCt = cls.getConstructor((Class[]) null);
            instance = (AssetManager) assetMagCt.newInstance((Object[]) null);

            Method addAssetPathMethod = cls.getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(instance, resPatch);
        } catch (Exception e) {
            Log.e("", e.toString());
        }

        return instance;
    }

    public Resources getResources() {
        return res;
    }

    private void initResources(Context context, AssetManager assetManager) {
        Resources ress = null;
        if (null != context) {
            ress = context.getResources();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.setToDefaults();
        if (null != ress || assetManager != null) {
            Configuration cfg = ress.getConfiguration();

            res = new Resources(assetManager, displayMetrics, cfg);

        }
    }

    public View inflate(Context context, int resource, ViewGroup root) {
        if (null != res) {
            View view = LayoutInflater.from(context).inflate(res.getXml(resource), root);
//            view.setBackgroundColor(Color.parseColor("#ffff00"));
            return view;
        }
        return null;
    }

    public int getRid(String rStrnig) {
        int value = -1;
        try {
            int rindex = rStrnig.indexOf(".R.");
            String Rpath = rStrnig.substring(0, rindex + 2);
            int fieldIndex = rStrnig.lastIndexOf(".");
            String fieldName = rStrnig.substring(fieldIndex + 1, rStrnig.length());
            rStrnig = rStrnig.substring(0, fieldIndex);
            String type = rStrnig.substring(rStrnig.lastIndexOf(".") + 1, rStrnig.length());
            String className = Rpath + "$" + type;

            Class<?> cls = Class.forName(className);
            value = cls.getDeclaredField(fieldName).getInt(null);

        } catch (Exception e) {
            Log.e("", e.toString());
        }
        Log.i("ll", "ID=" + value);
        return value;
    }


    public static int getLayoutId(Context paramContext, String paramString) {
        return res.getIdentifier(paramString, "layout",
                paramContext.getPackageName());
    }

    public int getStringId(Context paramContext, String paramString) {
        return res.getIdentifier(paramString, "string",
                paramContext.getPackageName());
    }

    public int getDrawableId(Context paramContext, String paramString) {
        return res.getIdentifier(paramString,
                "drawable", paramContext.getPackageName());
    }

    public int getStyleId(Context paramContext, String paramString) {
        return res.getIdentifier(paramString,
                "style", paramContext.getPackageName());
    }

    public int getId(Context paramContext, String paramString) {
        return res.getIdentifier(paramString,
                "id", paramContext.getPackageName());
    }

    public int getColorId(Context paramContext, String paramString) {
        return res.getIdentifier(paramString,
                "color", paramContext.getPackageName());
    }

    private File copyJarToSD(Context ctx) {

        if (ctx == null) {
            return null;
        }

        InputStream is = null;
        OutputStream os = null;
        File out = null;
        try {
            is = ctx.getResources().getAssets().open("mm.jar");
            File file = new File(jarPath);
            long fileLen = file.length();
            int assetLen = is.available();
            if (file.exists() && fileLen == assetLen) {
                is.close();
                return file;
            }

            out = new File(ctx.getFilesDir(), "mm.jar");

            os = new FileOutputStream(out);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
        } catch (Exception e) {
            Log.e("", e.toString());
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    os = null;
                }
            }

        }
        return out;
    }

}
