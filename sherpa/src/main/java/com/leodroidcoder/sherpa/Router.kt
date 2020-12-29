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
import com.leodroidcoder.sherpa.commands.BackCommand
import com.leodroidcoder.sherpa.commands.BackOrNavigateCommand
import com.leodroidcoder.sherpa.commands.BackToCommand
import com.leodroidcoder.sherpa.commands.BackWithResultCommand
import com.leodroidcoder.sherpa.commands.NavigateCommand

/**
 * Sherpa router implementation.
 * Converts navigation actions to navigation commands
 * and passes commands to [Sherpa].
 * In general, works as a facade for [Sherpa] navigator.
 *
 * @author Leonid Ustenko Leo.Droidcoder@gmail.com
 * @since 1.0.0
 */
class Router(private val tag: String, private val sherpa: Sherpa) : IRouter {

    override fun navigateTo(
        destinationId: Int,
        args: Bundle?,
        screenName: String?,
        screenArgs: Map<String, Any>?
    ) {
        sherpa.onCommand(NavigateCommand(tag, destinationId, args, screenName, screenArgs))
    }

    override fun backTo(destinationId: Int, inclusive: Boolean) {
        sherpa.onCommand(BackToCommand(tag, destinationId, inclusive))
    }

    override fun back() {
        sherpa.onCommand(BackCommand(tag))
    }

    override fun backOrNavigateTo(destinationId: Int, args: Bundle?) {
        sherpa.onCommand(BackOrNavigateCommand(tag, destinationId, args))
    }

    override fun backWithResult(
        resultCode: String,
        result: Any,
        destinationId: Int?,
        inclusive: Boolean
    ) {
        val command = BackWithResultCommand(tag, resultCode, result, destinationId, inclusive)
        sherpa.onCommand(command)
    }

    override fun addResultListener(resultCode: String, resultListener: ResultListener) {
        sherpa.addResultListener(resultCode, resultListener)
    }

    override fun addResultListener(resultCode: String, onResult: (result: Any?) -> Unit) {
        addResultListener(resultCode, object : ResultListener {
            override fun onResult(result: Any?) {
                onResult(result)
            }
        })
    }

    override fun removeResultListeners(vararg resultCode: String) {
        resultCode.forEach { sherpa.removeResultListeners(it) }
    }

    override fun removeResultListener(resultCode: String, resultListener: ResultListener) {
        sherpa.removeResultListener(resultCode, resultListener)
    }
}