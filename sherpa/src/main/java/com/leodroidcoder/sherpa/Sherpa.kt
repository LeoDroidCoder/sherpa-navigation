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
 */package com.leodroidcoder.sherpa

import android.os.Handler
import android.os.Looper
import androidx.navigation.NavController
import com.leodroidcoder.sherpa.commands.*
import com.leodroidcoder.sherpa.utils.whenNonNull
import com.leodroidcoder.sherpa.logger.ILogger
import java.util.concurrent.LinkedBlockingQueue

/**
 * Handles navigation commands and performs all the navigation logic.
 *
 * @author Leonid Ustenko Leo.Droidcoder@gmail.com
 * @since 1.0.0
 */
open class Sherpa(private val logger: ILogger? = null) : NavigationHolder {

    private val controllersHolder = hashMapOf<String, NavController>()
    private val commandsQueue = LinkedBlockingQueue<BaseNavigationCommand>()
    private val pendingCommands = hashMapOf<String, MutableList<BaseNavigationCommand>>()
    private val executor = CommandsExecutor()
    private val resultListeners = hashMapOf<String, HashSet<ResultListener>>()

    init {
        startConsumer()
    }

    /**
     * Starts a consummer thread which handles incoming commands.
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    private fun startConsumer() {
        val consumer = CommandConsumer(commandsQueue, this, Handler(Looper.getMainLooper()))
        Thread(consumer).start()
    }

    override fun setNavController(navController: NavController, tag: String) {
        logger?.d(LOG_TAG, "setNavController: tag=$tag")
        controllersHolder[tag] = navController
        addPendingCommands(tag)
    }

    override fun getNavController(tag: String): NavController? {
        logger?.d(LOG_TAG, "getNavController: tag=$tag")
        return controllersHolder[tag]
    }

    override fun removeNavController(tag: String) {
        logger?.d(LOG_TAG, "removeNavController: tag=$tag")
        controllersHolder.remove(tag)
    }

    /**
     * Processes incoming navigation commands.
     * Ads them to the commands queue [commandsQueue].
     * @see [BaseNavigationCommand]
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun onCommand(command: BaseNavigationCommand) {
        logger?.d(LOG_TAG, "onCommand: $command")
        commandsQueue.add(command)
    }

    /**
     * Add pending commands to the queue.
     * Should be called once a [NavController] becomes active.
     * Will proceed awaiting commands for this specific nav controller (which corresponds to [controllerTag])
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    private fun addPendingCommands(controllerTag: String) {
        logger?.d(LOG_TAG, "addPendingCommands: $controllerTag")
        pendingCommands[controllerTag]?.let {
            commandsQueue.addAll(it)
            pendingCommands.remove(controllerTag)
        }
    }

    /**
     * Process navigation command.
     * Executes command [executeCommand] if navigation controller (corresponding to [BaseNavigationCommand.controllerTag]) is available
     * or saves it for execution in future [savePendingCommand] once the nav controller is available.
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun processCommand(command: BaseNavigationCommand) {
        logger?.d(LOG_TAG, "processCommand: $command")
        onProcessCommand(command)
        getNavController(command.navControllerTag)?.let { executeCommand(command, it) }
            ?: savePendingCommand(command, command.navControllerTag)
    }

    /**
     * Used to stop the commands executor.
     * Call it if you are not going to use Sherpa navigation any longer.
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun destroy() {
        processCommand(PoisonPillCommand)
    }

    /**
     * Called on each command procession.
     * Empty by default.
     * You may override it in order to intercept each command.
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    protected open fun onProcessCommand(command: BaseNavigationCommand) {
        // empty
    }

    internal fun addResultListener(requestCode: String, listener: ResultListener) {
        logger?.d(LOG_TAG, "setResultListener: requestCode=$requestCode")
        val listeners = resultListeners.getOrPut(requestCode) { hashSetOf() }
        listeners.add(listener)
    }

    internal fun removeResultListeners(requestCode: String): Boolean {
        logger?.d(LOG_TAG, "removeResultListeners: requestCode=$requestCode")
        return !resultListeners.remove(requestCode).isNullOrEmpty()
    }

    internal fun removeResultListener(requestCode: String, listener: ResultListener): Boolean {
        logger?.d(LOG_TAG, "removeResultListener: requestCode=$requestCode")
        resultListeners[requestCode]?.run {
            return remove(listener)
        }
        return false
    }

    /**
     * Executes navigation commands.
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    private fun executeCommand(command: BaseNavigationCommand, navController: NavController) {
        logger?.d(LOG_TAG, "executeCommand: $command")
        executor.execute(command, navController)
        if (command is BackWithResultCommand<*>) {
            whenNonNull(resultListeners[command.requestCode], command.result) { listeners, result ->
                listeners.forEach { it.onResult(result) }
            } ?: run {
                logger?.e(
                    LOG_TAG,
                    "executeCommand: cannot execute BackWithResultCommand. Result requestCode=${command.requestCode}, result=${command.result}"
                )
            }
        }
    }

    /**
     * Saves the command [command] to be executed in future,
     * once [NavController] which corresponds to [controllerTag] is available.
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    private fun savePendingCommand(command: BaseNavigationCommand, controllerTag: String) {
        logger?.d(LOG_TAG, "savePendingCommand: $command, tag=$controllerTag")
        val thisControllerCommands = pendingCommands[controllerTag]
            ?: mutableListOf<BaseNavigationCommand>().also { pendingCommands[controllerTag] = it }
        thisControllerCommands.add(command)
    }

    private companion object {
        /**
         * Logging tag.
         */
        private const val LOG_TAG = "Sherpa"
    }
}
