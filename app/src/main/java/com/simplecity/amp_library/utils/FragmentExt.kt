@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package com.simplecity.amp_library.utils

import android.os.Bundle
import android.support.v4.app.Fragment

inline fun <T : Fragment> T.withArgs( //NOSONAR
    argsBuilder: Bundle.() -> Unit //NOSONAR
): T = this.apply { arguments = Bundle().apply(argsBuilder) } //NOSONAR
