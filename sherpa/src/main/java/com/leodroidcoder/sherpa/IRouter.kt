/*
 * Copyright (C) 2017 Leonid Ustenko Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leodroidcoder.sherpa

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavAction
import androidx.navigation.NavDestination
import com.leodroidcoder.sherpa.ResultListener

/**
 * Base router interface.
 *
 * @author Leonid Ustenko Leo.Droidcoder@gmail.com
 * @since 1.0.0
 */
interface IRouter {

    /**
     * Navigates to the specified [destinationId] with or without bundle arguments
     *
     * @param destinationId - destination id. Can be a destination [NavDestination]
     * as well as an action [NavAction]
     * @param args Bundle arguments
     * @param screenName optional name of destination. Can be used to pass its value as a logging event
     * @param screenArgs optional argumens of destination. Can be used to pass its values as logging arguments
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun navigateTo(
        @IdRes destinationId: Int,
        args: Bundle? = null,
        screenName: String? = null,
        screenArgs: Map<String, Any>? = null
    )

    /**
     * Navigate to the previous destination in navigation graph.
     */
    fun back()

    /**
     * Navigates back to the specified [destinationId].
     *
     * @param destinationId - destination id. Can be a destination [NavDestination]
     * as well as an action [NavAction]
     * @param inclusive `true` for going back inclusive destination and `false` - exclusive correspondingly
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun backTo(@IdRes destinationId: Int, inclusive: Boolean = false)

    /**
     * Tries to pop back stack down to [destinationId].
     * In case it is not popped (there was no this destination in the back stack),
     * performs [navigateTo] to the [destinationId]
     *
     * @param destinationId - destination id. Can be a destination [NavDestination]
     * as well as an action [NavAction]
     * @param args Bundle arguments
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun backOrNavigateTo(@IdRes destinationId: Int, args: Bundle? = null)

    /**
     * Navigates back to the specified [destinationId] and passes a result to a [ResultListener].
     * The result listener must be previously set with [addResultListener].
     *
     * @param destinationId - destination id. Can be a destination [NavDestination]
     * as well as an action [NavAction]
     * @param inclusive `true` for going back inclusive destination and `false` - exclusive correspondingly
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun backWithResult(
        resultCode: String,
        result: Any,
        destinationId: Int? = null,
        inclusive: Boolean = false
    )

    /**
     * Adds a result listener.
     * In order to avoid memory leaks, make sure to remove it when it is not needed anymore by calling
     * a [removeResultListener] or [removeResultListeners] with the corresponding [resultCode].
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun addResultListener(resultCode: String, resultListener: ResultListener)

    fun addResultListener(resultCode: String, onResult: (result: Any?) -> Unit)

    /**
     * Remove a specific instance of result listener.
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun removeResultListener(resultCode: String, resultListener: ResultListener)

    /**
     * Remove all the result listeners matching result codes [resultCode]
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun removeResultListeners(vararg resultCode: String)

}
