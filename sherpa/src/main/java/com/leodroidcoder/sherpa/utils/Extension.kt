package com.leodroidcoder.sherpa.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.leodroidcoder.sherpa.logger.ILogger

fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null, navOptions: NavOptions? = null,
                               navigatorExtras: Navigator.Extras? = null,
                               logger: ILogger? = null
) {
    try {
        this.navigate(resId, args, navOptions, navigatorExtras)
    } catch (e: IllegalArgumentException) {
        logger?.e("Sherpa","navigateSafe: failed xto navigate to the screen. resId=$resId", e)
    }
}

fun <T1 : Any, T2 : Any, R : Any> Any.whenNonNull(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, R : Any> Any.whenNonNull(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}