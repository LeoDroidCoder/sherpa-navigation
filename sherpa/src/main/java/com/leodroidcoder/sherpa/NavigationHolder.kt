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

import androidx.navigation.NavController

interface NavigationHolder {

    /**
     * Should be called to set [NavController] in onResume method of the Activity.
     * Navigation becomes available since this time.
     * All commands, send before this methods DO persist (such as whhen you navigated and the Activity was paused due to an incoming call)
     * and will be executed at hte very beginning.
     * The main point to use it that you can set different NavControllers for different screens.
     * @param navController Navigation controller from AndroidX navigation.
     * @param tag Navigation controller tag. Needed in order to distinguis navigation controllers if you have multiple ones
     * (i.e. on different Activities or Fragments). Each controller will have it's own graph and stack.
     * @see NavController
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun setNavController(navController: NavController, tag: String)

    /**
     * Retrieves [NavController].
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun getNavController(tag: String): NavController?

    /**
     * Should be called to set [NavController] in onPause method of the Activity.
     * @param tag Navigation controller tag. Needed in order to distinguis navigation controllers if you have multiple ones
     * (i.e. on different Activities or Fragments). Each controller will have it's own graph and stack.
     *
     * @author Leonid Ustenko Leo.Droidcoder@gmail.com
     * @since 1.0.0
     */
    fun removeNavController(tag: String)
}