package com.example.altruisticwebapp.Components;

public enum LOA { //Level Of Altruism
    SFavg, ETavg, ATavg,
    SFmin, ETmin, ATmin;
    //Selfish First, Equal Treatment, Altruistic Treatment

    public static LOA stringToEnum(String str1, String str2){
        /*
         * levelOfAltruism is returned as either "average" or "minimum"
         * treatment is returned as either "selfish_first","equal_treatment" or
         * "altruistic_treatment"
         */
        if (str1.equals("average")){
            if (str2.equals("selfish_first")) return SFavg;
            if (str2.equals("equal_treatment")) return ETavg;
            if (str2.equals("altruistic_treatment")) return ATavg;
        }
        if (str1.equals("minimum")){
            if (str2.equals("selfish_first")) return SFmin;
            if (str2.equals("equal_treatment")) return ETmin;
            if (str2.equals("altruistic_treatment")) return ATmin;
        }
        return null;
    }
}
