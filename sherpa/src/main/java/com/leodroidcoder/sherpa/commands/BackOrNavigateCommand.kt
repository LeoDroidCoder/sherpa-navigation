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
package com.leodroidcoder.sherpa.commands

import android.os.Bundle
import androidx.annotation.IdRes
import com.leodroidcoder.sherpa.Router

/**
 * A command [BaseNavigationCommand] used to navigate back.
 * Same as [BackCommand], but it navigates to a screen in case cannot find it in the back stack.
 *
 * @param controllerTag a navigation controller tag, used when initializing [Router]
 * @param destinationId any destination from Google's Navigation Component.
 * Can be a fragment id as well as action id from a navigation graph.
 * @param args a [Bundle] which to pass to the destination.
 *
 * @author Leonid Ustenko Leo.Droidcoder@gmail.com
 * @since 1.0.0
 */
data class BackOrNavigateCommand(
    val controllerTag: String,
    @IdRes val destinationId: Int,
    val args: Bundle?
) : BaseNavigationCommand(controllerTag)