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

import android.os.Handler
import com.leodroidcoder.sherpa.Router
import com.leodroidcoder.sherpa.Sherpa
import java.util.concurrent.BlockingQueue

/**
 * A consumer used to consume commands from the command queue [queue].
 *
 * @author Leonid Ustenko Leo.Droidcoder@gmail.com
 * @since 1.0.0
 */
class CommandConsumer(
    private val queue: BlockingQueue<BaseNavigationCommand>,
    private val sherpa: Sherpa,
    private val handler: Handler
) : Runnable {
    override fun run() {
        try {
            while (true) {
                val command = queue.take()
                if (command == PoisonPillCommand) {
                    return
                }
                handler.post { sherpa.processCommand(command) }
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}