package manager;

import utils.TextUtil;

import java.util.HashMap;

public class Env {

    public static HashMap<String, String> ConfigEnv = new HashMap();

    public static void addEnv(String env, String realEnv) {
        ConfigEnv.put(env, realEnv);
    }

    public static String appendEnvParams(String cmd) {
        if (TextUtil.isEmpty(cmd)) {
            return cmd;
        }
        String[] paramsArray = cmd.split(" ");
        String executeEnv = paramsArray[0];
        if (ConfigEnv.containsKey(executeEnv)) {
            return cmd.replace(executeEnv, ConfigEnv.get(executeEnv));
        }
        return cmd;
    }
}
