package com.simplecity.amp_library.utils;

import android.content.Context;
import android.graphics.Color;
import com.simplecity.amp_library.ShuttleApplication;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ColorPalette { //NOSONAR

    public static int[] getPrimaryColors(Context context, SettingsManager settingsManager) { //NOSONAR
        return ShuttleUtils.isUpgraded((ShuttleApplication) context.getApplicationContext(), settingsManager) ? PRIMARY_COLORS : PRIMARY_COLORS_FREE; //NOSONAR
    }

    public static int[][] getPrimaryColorsSub(Context context, SettingsManager settingsManager) { //NOSONAR
        return ShuttleUtils.isUpgraded((ShuttleApplication) context.getApplicationContext(), settingsManager) ? PRIMARY_COLORS_SUB : PRIMARY_COLORS_SUB_FREE; //NOSONAR
    }

    private final static int[] PRIMARY_COLORS = new int[] { //NOSONAR
            Color.parseColor("#F44336"), //NOSONAR
            Color.parseColor("#E91E63"), //NOSONAR
            Color.parseColor("#9C27B0"), //NOSONAR
            Color.parseColor("#673AB7"), //NOSONAR
            Color.parseColor("#3F51B5"), //NOSONAR
            Color.parseColor("#2196F3"), //NOSONAR
            Color.parseColor("#03A9F4"), //NOSONAR
            Color.parseColor("#00BCD4"), //NOSONAR
            Color.parseColor("#009688"), //NOSONAR
            Color.parseColor("#4CAF50"), //NOSONAR
            Color.parseColor("#8BC34A"), //NOSONAR
            Color.parseColor("#CDDC39"), //NOSONAR
            Color.parseColor("#FFEB3B"), //NOSONAR
            Color.parseColor("#FFC107"), //NOSONAR
            Color.parseColor("#FF9800"), //NOSONAR
            Color.parseColor("#FF5722"), //NOSONAR
            Color.parseColor("#795548"), //NOSONAR
            Color.parseColor("#9E9E9E"), //NOSONAR
            Color.parseColor("#607D8B") //NOSONAR
    };

    private final static int[][] PRIMARY_COLORS_SUB = new int[][] { //NOSONAR
            new int[] { //NOSONAR
                    Color.parseColor("#FFEBEE"), //NOSONAR
                    Color.parseColor("#FFCDD2"), //NOSONAR
                    Color.parseColor("#EF9A9A"), //NOSONAR
                    Color.parseColor("#E57373"), //NOSONAR
                    Color.parseColor("#EF5350"), //NOSONAR
                    Color.parseColor("#F44336"), //NOSONAR
                    Color.parseColor("#E53935"), //NOSONAR
                    Color.parseColor("#D32F2F"), //NOSONAR
                    Color.parseColor("#C62828"), //NOSONAR
                    Color.parseColor("#B71C1C") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#FCE4EC"), //NOSONAR
                    Color.parseColor("#F8BBD0"), //NOSONAR
                    Color.parseColor("#F48FB1"), //NOSONAR
                    Color.parseColor("#F06292"), //NOSONAR
                    Color.parseColor("#EC407A"), //NOSONAR
                    Color.parseColor("#E91E63"), //NOSONAR
                    Color.parseColor("#D81B60"), //NOSONAR
                    Color.parseColor("#C2185B"), //NOSONAR
                    Color.parseColor("#AD1457"), //NOSONAR
                    Color.parseColor("#880E4F") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#F3E5F5"), //NOSONAR
                    Color.parseColor("#E1BEE7"), //NOSONAR
                    Color.parseColor("#CE93D8"), //NOSONAR
                    Color.parseColor("#BA68C8"), //NOSONAR
                    Color.parseColor("#AB47BC"), //NOSONAR
                    Color.parseColor("#9C27B0"), //NOSONAR
                    Color.parseColor("#8E24AA"), //NOSONAR
                    Color.parseColor("#7B1FA2"), //NOSONAR
                    Color.parseColor("#6A1B9A"), //NOSONAR
                    Color.parseColor("#4A148C") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#EDE7F6"), //NOSONAR
                    Color.parseColor("#D1C4E9"), //NOSONAR
                    Color.parseColor("#B39DDB"), //NOSONAR
                    Color.parseColor("#9575CD"), //NOSONAR
                    Color.parseColor("#7E57C2"), //NOSONAR
                    Color.parseColor("#673AB7"), //NOSONAR
                    Color.parseColor("#5E35B1"), //NOSONAR
                    Color.parseColor("#512DA8"), //NOSONAR
                    Color.parseColor("#4527A0"), //NOSONAR
                    Color.parseColor("#311B92") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E8EAF6"), //NOSONAR
                    Color.parseColor("#C5CAE9"), //NOSONAR
                    Color.parseColor("#9FA8DA"), //NOSONAR
                    Color.parseColor("#7986CB"), //NOSONAR
                    Color.parseColor("#5C6BC0"), //NOSONAR
                    Color.parseColor("#3F51B5"), //NOSONAR
                    Color.parseColor("#3949AB"), //NOSONAR
                    Color.parseColor("#303F9F"), //NOSONAR
                    Color.parseColor("#283593"), //NOSONAR
                    Color.parseColor("#1A237E") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E3F2FD"), //NOSONAR
                    Color.parseColor("#BBDEFB"), //NOSONAR
                    Color.parseColor("#90CAF9"), //NOSONAR
                    Color.parseColor("#64B5F6"), //NOSONAR
                    Color.parseColor("#42A5F5"), //NOSONAR
                    Color.parseColor("#2196F3"), //NOSONAR
                    Color.parseColor("#1E88E5"), //NOSONAR
                    Color.parseColor("#1976D2"), //NOSONAR
                    Color.parseColor("#1565C0"), //NOSONAR
                    Color.parseColor("#0D47A1") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E1F5FE"), //NOSONAR
                    Color.parseColor("#B3E5FC"), //NOSONAR
                    Color.parseColor("#81D4FA"), //NOSONAR
                    Color.parseColor("#4FC3F7"), //NOSONAR
                    Color.parseColor("#29B6F6"), //NOSONAR
                    Color.parseColor("#03A9F4"), //NOSONAR
                    Color.parseColor("#039BE5"), //NOSONAR
                    Color.parseColor("#0288D1"), //NOSONAR
                    Color.parseColor("#0277BD"), //NOSONAR
                    Color.parseColor("#01579B") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E0F7FA"), //NOSONAR
                    Color.parseColor("#B2EBF2"), //NOSONAR
                    Color.parseColor("#80DEEA"), //NOSONAR
                    Color.parseColor("#4DD0E1"), //NOSONAR
                    Color.parseColor("#26C6DA"), //NOSONAR
                    Color.parseColor("#00BCD4"), //NOSONAR
                    Color.parseColor("#00ACC1"), //NOSONAR
                    Color.parseColor("#0097A7"), //NOSONAR
                    Color.parseColor("#00838F"), //NOSONAR
                    Color.parseColor("#006064") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E0F2F1"), //NOSONAR
                    Color.parseColor("#B2DFDB"), //NOSONAR
                    Color.parseColor("#80CBC4"), //NOSONAR
                    Color.parseColor("#4DB6AC"), //NOSONAR
                    Color.parseColor("#26A69A"), //NOSONAR
                    Color.parseColor("#009688"), //NOSONAR
                    Color.parseColor("#00897B"), //NOSONAR
                    Color.parseColor("#00796B"), //NOSONAR
                    Color.parseColor("#00695C"), //NOSONAR
                    Color.parseColor("#004D40") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E8F5E9"), //NOSONAR
                    Color.parseColor("#C8E6C9"), //NOSONAR
                    Color.parseColor("#A5D6A7"), //NOSONAR
                    Color.parseColor("#81C784"), //NOSONAR
                    Color.parseColor("#66BB6A"), //NOSONAR
                    Color.parseColor("#4CAF50"), //NOSONAR
                    Color.parseColor("#43A047"), //NOSONAR
                    Color.parseColor("#388E3C"), //NOSONAR
                    Color.parseColor("#2E7D32"), //NOSONAR
                    Color.parseColor("#1B5E20") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#F1F8E9"), //NOSONAR
                    Color.parseColor("#DCEDC8"), //NOSONAR
                    Color.parseColor("#C5E1A5"), //NOSONAR
                    Color.parseColor("#AED581"), //NOSONAR
                    Color.parseColor("#9CCC65"), //NOSONAR
                    Color.parseColor("#8BC34A"), //NOSONAR
                    Color.parseColor("#7CB342"), //NOSONAR
                    Color.parseColor("#689F38"), //NOSONAR
                    Color.parseColor("#558B2F"), //NOSONAR
                    Color.parseColor("#33691E") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#F9FBE7"), //NOSONAR
                    Color.parseColor("#F0F4C3"), //NOSONAR
                    Color.parseColor("#E6EE9C"), //NOSONAR
                    Color.parseColor("#DCE775"), //NOSONAR
                    Color.parseColor("#D4E157"), //NOSONAR
                    Color.parseColor("#CDDC39"), //NOSONAR
                    Color.parseColor("#C0CA33"), //NOSONAR
                    Color.parseColor("#AFB42B"), //NOSONAR
                    Color.parseColor("#9E9D24"), //NOSONAR
                    Color.parseColor("#827717") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#FFFDE7"), //NOSONAR
                    Color.parseColor("#FFF9C4"), //NOSONAR
                    Color.parseColor("#FFF59D"), //NOSONAR
                    Color.parseColor("#FFF176"), //NOSONAR
                    Color.parseColor("#FFEE58"), //NOSONAR
                    Color.parseColor("#FFEB3B"), //NOSONAR
                    Color.parseColor("#FDD835"), //NOSONAR
                    Color.parseColor("#FBC02D"), //NOSONAR
                    Color.parseColor("#F9A825"), //NOSONAR
                    Color.parseColor("#F57F17") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#FFF8E1"), //NOSONAR
                    Color.parseColor("#FFECB3"), //NOSONAR
                    Color.parseColor("#FFE082"), //NOSONAR
                    Color.parseColor("#FFD54F"), //NOSONAR
                    Color.parseColor("#FFCA28"), //NOSONAR
                    Color.parseColor("#FFC107"), //NOSONAR
                    Color.parseColor("#FFB300"), //NOSONAR
                    Color.parseColor("#FFA000"), //NOSONAR
                    Color.parseColor("#FF8F00"), //NOSONAR
                    Color.parseColor("#FF6F00") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#FFF3E0"), //NOSONAR
                    Color.parseColor("#FFE0B2"), //NOSONAR
                    Color.parseColor("#FFCC80"), //NOSONAR
                    Color.parseColor("#FFB74D"), //NOSONAR
                    Color.parseColor("#FFA726"), //NOSONAR
                    Color.parseColor("#FF9800"), //NOSONAR
                    Color.parseColor("#FB8C00"), //NOSONAR
                    Color.parseColor("#F57C00"), //NOSONAR
                    Color.parseColor("#EF6C00"), //NOSONAR
                    Color.parseColor("#E65100") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#FBE9E7"), //NOSONAR
                    Color.parseColor("#FFCCBC"), //NOSONAR
                    Color.parseColor("#FFAB91"), //NOSONAR
                    Color.parseColor("#FF8A65"), //NOSONAR
                    Color.parseColor("#FF7043"), //NOSONAR
                    Color.parseColor("#FF5722"), //NOSONAR
                    Color.parseColor("#F4511E"), //NOSONAR
                    Color.parseColor("#E64A19"), //NOSONAR
                    Color.parseColor("#D84315"), //NOSONAR
                    Color.parseColor("#BF360C") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#EFEBE9"), //NOSONAR
                    Color.parseColor("#D7CCC8"), //NOSONAR
                    Color.parseColor("#BCAAA4"), //NOSONAR
                    Color.parseColor("#A1887F"), //NOSONAR
                    Color.parseColor("#8D6E63"), //NOSONAR
                    Color.parseColor("#795548"), //NOSONAR
                    Color.parseColor("#6D4C41"), //NOSONAR
                    Color.parseColor("#5D4037"), //NOSONAR
                    Color.parseColor("#4E342E"), //NOSONAR
                    Color.parseColor("#3E2723") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#FAFAFA"), //NOSONAR
                    Color.parseColor("#F5F5F5"), //NOSONAR
                    Color.parseColor("#EEEEEE"), //NOSONAR
                    Color.parseColor("#E0E0E0"), //NOSONAR
                    Color.parseColor("#BDBDBD"), //NOSONAR
                    Color.parseColor("#9E9E9E"), //NOSONAR
                    Color.parseColor("#757575"), //NOSONAR
                    Color.parseColor("#616161"), //NOSONAR
                    Color.parseColor("#424242"), //NOSONAR
                    Color.parseColor("#212121") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#ECEFF1"), //NOSONAR
                    Color.parseColor("#CFD8DC"), //NOSONAR
                    Color.parseColor("#B0BEC5"), //NOSONAR
                    Color.parseColor("#90A4AE"), //NOSONAR
                    Color.parseColor("#78909C"), //NOSONAR
                    Color.parseColor("#607D8B"), //NOSONAR
                    Color.parseColor("#546E7A"), //NOSONAR
                    Color.parseColor("#455A64"), //NOSONAR
                    Color.parseColor("#37474F"), //NOSONAR
                    Color.parseColor("#263238") //NOSONAR
            }
    };

    private final static int[] PRIMARY_COLORS_FREE = new int[] { //NOSONAR
            Color.parseColor("#F44336"), //NOSONAR
            Color.parseColor("#9C27B0"), //NOSONAR
            Color.parseColor("#03A9F4"), //NOSONAR
            Color.parseColor("#009688"), //NOSONAR
            Color.parseColor("#4CAF50"), //NOSONAR
            Color.parseColor("#FFC107"), //NOSONAR
            Color.parseColor("#9E9E9E") //NOSONAR
    };

    private final static int[][] PRIMARY_COLORS_SUB_FREE = new int[][] { //NOSONAR
            new int[] { //NOSONAR
                    Color.parseColor("#FFEBEE"), //NOSONAR
                    Color.parseColor("#FFCDD2"), //NOSONAR
                    Color.parseColor("#EF9A9A"), //NOSONAR
                    Color.parseColor("#E57373"), //NOSONAR
                    Color.parseColor("#EF5350"), //NOSONAR
                    Color.parseColor("#F44336"), //NOSONAR
                    Color.parseColor("#E53935"), //NOSONAR
                    Color.parseColor("#D32F2F"), //NOSONAR
                    Color.parseColor("#C62828"), //NOSONAR
                    Color.parseColor("#B71C1C") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#F3E5F5"), //NOSONAR
                    Color.parseColor("#E1BEE7"), //NOSONAR
                    Color.parseColor("#CE93D8"), //NOSONAR
                    Color.parseColor("#BA68C8"), //NOSONAR
                    Color.parseColor("#AB47BC"), //NOSONAR
                    Color.parseColor("#9C27B0"), //NOSONAR
                    Color.parseColor("#8E24AA"), //NOSONAR
                    Color.parseColor("#7B1FA2"), //NOSONAR
                    Color.parseColor("#6A1B9A"), //NOSONAR
                    Color.parseColor("#4A148C") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E1F5FE"), //NOSONAR
                    Color.parseColor("#B3E5FC"), //NOSONAR
                    Color.parseColor("#81D4FA"), //NOSONAR
                    Color.parseColor("#4FC3F7"), //NOSONAR
                    Color.parseColor("#29B6F6"), //NOSONAR
                    Color.parseColor("#03A9F4"), //NOSONAR
                    Color.parseColor("#039BE5"), //NOSONAR
                    Color.parseColor("#0288D1"), //NOSONAR
                    Color.parseColor("#0277BD"), //NOSONAR
                    Color.parseColor("#01579B") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E0F2F1"), //NOSONAR
                    Color.parseColor("#B2DFDB"), //NOSONAR
                    Color.parseColor("#80CBC4"), //NOSONAR
                    Color.parseColor("#4DB6AC"), //NOSONAR
                    Color.parseColor("#26A69A"), //NOSONAR
                    Color.parseColor("#009688"), //NOSONAR
                    Color.parseColor("#00897B"), //NOSONAR
                    Color.parseColor("#00796B"), //NOSONAR
                    Color.parseColor("#00695C"), //NOSONAR
                    Color.parseColor("#004D40") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#E8F5E9"), //NOSONAR
                    Color.parseColor("#C8E6C9"), //NOSONAR
                    Color.parseColor("#A5D6A7"), //NOSONAR
                    Color.parseColor("#81C784"), //NOSONAR
                    Color.parseColor("#66BB6A"), //NOSONAR
                    Color.parseColor("#4CAF50"), //NOSONAR
                    Color.parseColor("#43A047"), //NOSONAR
                    Color.parseColor("#388E3C"), //NOSONAR
                    Color.parseColor("#2E7D32"), //NOSONAR
                    Color.parseColor("#1B5E20") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#FFF8E1"), //NOSONAR
                    Color.parseColor("#FFECB3"), //NOSONAR
                    Color.parseColor("#FFE082"), //NOSONAR
                    Color.parseColor("#FFD54F"), //NOSONAR
                    Color.parseColor("#FFCA28"), //NOSONAR
                    Color.parseColor("#FFC107"), //NOSONAR
                    Color.parseColor("#FFB300"), //NOSONAR
                    Color.parseColor("#FFA000"), //NOSONAR
                    Color.parseColor("#FF8F00"), //NOSONAR
                    Color.parseColor("#FF6F00") //NOSONAR
            },
            new int[] { //NOSONAR
                    Color.parseColor("#FAFAFA"), //NOSONAR
                    Color.parseColor("#F5F5F5"), //NOSONAR
                    Color.parseColor("#EEEEEE"), //NOSONAR
                    Color.parseColor("#E0E0E0"), //NOSONAR
                    Color.parseColor("#BDBDBD"), //NOSONAR
                    Color.parseColor("#9E9E9E"), //NOSONAR
                    Color.parseColor("#757575"), //NOSONAR
                    Color.parseColor("#616161"), //NOSONAR
                    Color.parseColor("#424242"), //NOSONAR
                    Color.parseColor("#212121") //NOSONAR
            },
    };
}
