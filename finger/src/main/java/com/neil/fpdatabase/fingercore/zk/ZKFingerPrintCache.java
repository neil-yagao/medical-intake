package com.neil.fpdatabase.fingercore.zk;

import com.neil.fpdatabase.fingercore.FingerPrintCache;
import com.neil.fpdatabase.fingercore.fingerprint.CachedFingerPrint;
import com.zkteco.biometric.ZKFPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nhu on 4/20/2017.
 */

@Component
public class ZKFingerPrintCache implements FingerPrintCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKFingerPrintCache.class);
    private Map<Integer, CachedFingerPrint> cachePool = new HashMap<>();

    private int nextAvailableCachingFingerPrintIndex = 1;

    @Override
    public int cacheFingerPrint(CachedFingerPrint cachedFingerPrint) {
        int proposalCacheIndex = nextAvailableCachingFingerPrintIndex;
        nextAvailableCachingFingerPrintIndex++;
        //int existing =
        int ret = ZKFPService.DBAdd(proposalCacheIndex, cachedFingerPrint.getCombinedTemplate());
        if (ret != 0) {
            nextAvailableCachingFingerPrintIndex--;
            throw new RuntimeException("fail to cache finger print for index " + nextAvailableCachingFingerPrintIndex);
        }
        cachePool.put(proposalCacheIndex, cachedFingerPrint);
        return proposalCacheIndex;
    }

    @Override
    public CachedFingerPrint getFingerPrint(int index) {
        return cachePool.get(index);
    }

    @Override
    public void deleteRegisteredFingerPrint(String identityCode) {
        int index = getCachedFingerPrintIndex(identityCode);
        if (index == -1) {
            return;
        }
        int ret = ZKFPService.DBDel(index);
        if (ret < 0) {
            LOGGER.error("unable to remove finder print from mem:", ret);
            return;
        }
        cachePool.remove(index);
    }

    private int getCachedFingerPrintIndex(String identityCode) {
        for (Integer index : cachePool.keySet()) {
            CachedFingerPrint examineFingerPrint = cachePool.get(index);
            if (examineFingerPrint.getIdentityCode().equalsIgnoreCase(identityCode)) {
                return index;
            }
        }
        return -1;
    }
}
