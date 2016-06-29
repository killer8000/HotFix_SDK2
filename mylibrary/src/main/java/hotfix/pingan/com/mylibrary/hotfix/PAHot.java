package hotfix.pingan.com.mylibrary.hotfix;

/**
 * Created by ndh on 16/4/8.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import hotfix.com.pa.DexUtils;


/**
 * Created by jixin.jia on 15/10/31.
 * mdf by ndh on 16/04/12
 */
public class PAHot {

    private static final String TAG = "PAHot";
    private static final String DEX_DIR = "hotfix";
    private static final String DEX_OPT_DIR = "hotfixopt";
    private static final String SP_NAME = "_hotfix_";
    private static final String SP_VERSION = "version";
    private static final String PATCH_DIR = "/hotfixpatch";

    private Context mContext;
    private SharedPreferences sp;
    File patchDir;

    private static class SingletonHolder {
        static final PAHot instance = new PAHot();
    }

    public static PAHot getInstance() {
        return SingletonHolder.instance;
    }

    private PAHot() {
    }

    /**
     *
     * @param context
     * @param version 当前sdk的版本
     */
    public void init(Context context, String version) {
        Log.i("Fix", "init --start--version=" + version);

        if (null == context || null == version) {
            return;
        }
        mContext = context;
        sp = context.getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        String ver = sp.getString(SP_VERSION, null);
        Log.i(TAG, "ver=" + ver);
        Log.i(TAG, "load native patch");
        File tempFile = mContext.getFilesDir();
        if (null != tempFile) {
            patchDir = new File(tempFile + PATCH_DIR);
            if (!patchDir.exists()) {
                patchDir.mkdir();
            }
        }
        if (ver == null || !ver.equalsIgnoreCase(version)) {
            cleanPatch();
            sp.edit().putString(SP_VERSION, version).commit();
        } else {
            //加载本地补丁
           PAHot.getInstance().loadPatch(context, Environment.getExternalStorageDirectory().getAbsolutePath().concat("/patch.jar"));

//            File[] patchFiles = patchDir.listFiles();
//            if (null != patchFiles && patchFiles.length > 0) {
//                for (File file : patchFiles) {
//                    if (null != file) {
//                            PAHot.getInstance().loadPatch(context, file.getAbsolutePath());
//                    }
//                }
//            }
        }


    }

    public File getPatchDir() {
        return patchDir;
    }

    public SharedPreferences getSharedPreferences() {
        return sp;
    }

    public void loadPatch(Context context, String dexPath) {


        if (context == null || dexPath == null) {
            Log.e(TAG, "context is null or dexPath is null");
            return;
        }
        if (!new File(dexPath).exists()) {
            Log.e(TAG, dexPath + " is null");
            return;
        }
        File dexOptDir = new File(context.getFilesDir(), DEX_OPT_DIR);
        dexOptDir.mkdir();
        try {
            DexUtils.injectDexAtFirst(dexPath, dexOptDir.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "inject " + dexPath + " failed");
            e.printStackTrace();
        }
    }


    public void cleanPatch() {
        Log.i(TAG, "cleanPathc-->");
        if (null == mContext) {
            return;
        }
        File dexDir = new File(mContext.getFilesDir(), DEX_DIR);
        if (dexDir.exists()) {
            dexDir.delete();
        }
        File dexOptDir = new File(mContext.getFilesDir(), DEX_OPT_DIR);
        if (dexOptDir.exists()) {
            dexOptDir.delete();
        }
    }
}
