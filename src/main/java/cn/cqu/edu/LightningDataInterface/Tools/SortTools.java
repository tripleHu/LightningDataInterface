package cn.cqu.edu.LightningDataInterface.Tools;

import java.util.List;

import cn.cqu.edu.LightningDataInterface.entity.SimpleLightingInfo;

public class SortTools {
	private static int partition(List<SimpleLightingInfo> lightingInfos,int lo,int hi){
        //固定的切分方式
        long key=lightingInfos.get(lo).getDatetime();
        int keyIndex=lo;
        while(lo<hi){
            while(lightingInfos.get(hi).getDatetime()>=key&&hi>lo){//从后半部分向前扫描
                hi--;
            }
            lightingInfos.set(lo, lightingInfos.get(hi)) ;
            while(lightingInfos.get(lo).getDatetime()<=key&&hi>lo){//从前半部分向后扫描
                lo++;
            }
            lightingInfos.set(hi, lightingInfos.get(lo)) ;
           
        }
        lightingInfos.set(hi, lightingInfos.get(keyIndex)) ;
       
        return hi;
    }
    
    public static List<SimpleLightingInfo> quicksort(List<SimpleLightingInfo> lightingInfos,int lo ,int hi)
    {
        if(lo>=hi)
        {
            return lightingInfos;
        }
        int index=partition(lightingInfos,lo,hi);
        quicksort(lightingInfos,lo,index-1);
        quicksort(lightingInfos,index+1,hi); 
        return lightingInfos;
    }
}
