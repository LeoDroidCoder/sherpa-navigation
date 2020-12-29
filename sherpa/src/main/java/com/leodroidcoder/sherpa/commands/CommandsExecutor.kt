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

import androidx.navigation.NavController
import com.leodroidcoder.sherpa.utils.navigateSafe
import com.leodroidcoder.sherpa.logger.ILogger

/**
 * Commands Executor.
 * Executes navigation commands on the navigation controller [NavController].
 *
 * @author Leonid Ustenko Leo.Droidcoder@gmail.com
 * @since 1.0.0
 */
class CommandsExecutor(private val logger: ILogger? = null) {

    fun execute(command: BaseNavigationCommand, controller: NavController) {
        when (command) {
            is NavigateCommand ->
                controller.navigateSafe(command.destinationId, command.args, logger = logger)
            is BackCommand ->
                controller.popBackStack()
            is BackToCommand ->
                controller.popBackStack(command.destinationId, command.inclusive)
            is BackOrNavigateCommand -> {
                if (!controller.popBackStack(command.destinationId, false)) {
                    controller.navigateSafe(command.destinationId, command.args, logger = logger)
                }
            }
            is BackWithResultCommand<*> -> {
                command.destinationId?.let {
                    controller.popBackStack(it, command.inclusive)
                } ?: controller.popBackStack()
            }
        }
    }
}