package com.simplecity.amp_library.interfaces;

import android.support.annotation.IntDef;

@IntDef({ FileType.PARENT, FileType.FOLDER, FileType.FILE }) //NOSONAR
public @interface FileType { //NOSONAR
    int PARENT = 0; //NOSONAR
    int FOLDER = 1; //NOSONAR
    int FILE = 2; //NOSONAR
}
