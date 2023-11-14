package free.servpp.client.app;

import free.servpp.client.Role;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidong@date 2023-11-07@version 1.0
 */
public class AsynServiceKeeper {
    private Map<String, ICallbackListener> serviceMap = new ConcurrentHashMap<>();

    private static AsynServiceKeeper asynServiceKeeper = new AsynServiceKeeper();

    public static AsynServiceKeeper getInstance(){
        return asynServiceKeeper;
    }
    private AsynServiceKeeper(){};

    private String genUUID(){
        return UUID.randomUUID().toString();
    }

    public String register(ICallbackListener listener){
        String uuid = genUUID();
        serviceMap.put(uuid,listener);
        return uuid;
    }

    public boolean serviceReturned(String uuid, Role[] roles){
        boolean ret = false;
        ICallbackListener listener = serviceMap.remove(uuid);
        if(listener != null){
            listener.serviceCallback(roles);
            ret = true;
        }
        return ret;
    }
}

