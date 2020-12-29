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

import androidx.annotation.IdRes
import com.leodroidcoder.sherpa.Router

/**
 * A command [BaseNavigationCommand] used to navigate back to a specific destination.
 *
 * @param controllerTag a navigation controller tag, used when initializing [Router]
 * @param destinationId any destination from Google's Navigation Component.
 * Can be a fragment id as well as action id from a navigation graph.
 * @param inclusive removes [destinationId] in case it is 'true' or navigates to it in case of 'false'
 *
 * @author Leonid Ustenko Leo.Droidcoder@gmail.com
 * @since 1.0.0
 */
data class BackToCommand(
    val controllerTag: String,
    @IdRes val destinationId: Int,
    val inclusive: Boolean = false
) : BaseNavigationCommand(controllerTag)