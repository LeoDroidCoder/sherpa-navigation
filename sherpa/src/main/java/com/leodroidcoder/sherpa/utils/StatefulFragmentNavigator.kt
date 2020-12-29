package com.leodroidcoder.sherpa.utils

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.leodroidcoder.sherpa.logger.ILogger
import java.io.FileDescriptor
import java.io.PrintWriter

@Navigator.Name("fragment")
internal class StatefulFragmentNavigator(
    context: Context,
    fragmentManager: FragmentManager,
    containerId: Int,
    private val logger: ILogger? = null
) : FragmentNavigator(context, StatefulFragmentManager(fragmentManager, logger), containerId) {

    private var lastDestination: Destination? = null
    private var lastDestinationTime: Long = 0

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val prevDestination = lastDestination
        val prevDestinationTime = lastDestinationTime
        val lastDestinationTime = System.currentTimeMillis()
        this.lastDestinationTime = lastDestinationTime
        this.lastDestination = destination

        try {
            if (prevDestination == destination) {
                if (lastDestinationTime - prevDestinationTime > 500) {
                    return super.navigate(destination, args, navOptions, navigatorExtras)
                } else {
                    logger?.d(LOG_TAG, "Prevent double navigation to the destination: $destination")
                }
            } else {
                return super.navigate(destination, args, navOptions, navigatorExtras)
            }
        } catch (e: IllegalArgumentException) {
            logger?.e(LOG_TAG, ":navigate: failed to navigate to the screen", e)
        }

        return null
    }

    private class StatefulFragmentManager(private val fm: FragmentManager, private val logger: ILogger?) :
        FragmentManager() {
        override fun saveFragmentInstanceState(f: Fragment): Fragment.SavedState? =
            fm.saveFragmentInstanceState(f)

        override fun findFragmentById(id: Int): Fragment? = fm.findFragmentById(id)
        override fun getFragments(): MutableList<Fragment> = fm.fragments
        override fun beginTransaction(): FragmentTransaction =
            StatefulFragmentTransaction(fm, fm.beginTransaction(), logger)

        override fun putFragment(bundle: Bundle, key: String, fragment: Fragment) =
            fm.putFragment(bundle, key, fragment)

        override fun removeOnBackStackChangedListener(listener: OnBackStackChangedListener) =
            fm.removeOnBackStackChangedListener(listener)

        override fun getFragment(bundle: Bundle, key: String): Fragment? =
            fm.getFragment(bundle, key)

        override fun unregisterFragmentLifecycleCallbacks(cb: FragmentLifecycleCallbacks) =
            fm.unregisterFragmentLifecycleCallbacks(cb)

        override fun getPrimaryNavigationFragment(): Fragment? = fm.primaryNavigationFragment
        override fun getBackStackEntryCount(): Int = fm.backStackEntryCount
        override fun isDestroyed(): Boolean = fm.isDestroyed
        override fun getBackStackEntryAt(index: Int): BackStackEntry = fm.getBackStackEntryAt(index)
        override fun executePendingTransactions(): Boolean = fm.executePendingTransactions()
        override fun popBackStackImmediate(): Boolean = fm.popBackStackImmediate()
        override fun popBackStackImmediate(name: String?, flags: Int): Boolean =
            fm.popBackStackImmediate(name, flags)

        override fun popBackStackImmediate(id: Int, flags: Int): Boolean =
            fm.popBackStackImmediate(id, flags)

        override fun findFragmentByTag(tag: String?): Fragment? = fm.findFragmentByTag(tag)
        override fun addOnBackStackChangedListener(listener: OnBackStackChangedListener) =
            fm.addOnBackStackChangedListener(listener)

        override fun dump(
            prefix: String,
            fd: FileDescriptor?,
            writer: PrintWriter,
            args: Array<out String>?
        ) = fm.dump(prefix, fd, writer, args)

        override fun isStateSaved(): Boolean = fm.isStateSaved
        override fun popBackStack() = fm.popBackStack()
        override fun popBackStack(name: String?, flags: Int) = fm.popBackStack(name, flags)
        override fun popBackStack(id: Int, flags: Int) = fm.popBackStack()
        override fun registerFragmentLifecycleCallbacks(
            cb: FragmentLifecycleCallbacks,
            recursive: Boolean
        ) = fm.registerFragmentLifecycleCallbacks(cb, recursive)
    }

    private class StatefulFragmentTransaction(val fm: FragmentManager,
                                              val t: FragmentTransaction,
                                              private val logger: ILogger?
    ) :
        FragmentTransaction() {
        override fun setBreadCrumbShortTitle(res: Int): FragmentTransaction =
            t.setBreadCrumbShortTitle(res)

        override fun setBreadCrumbShortTitle(text: CharSequence?): FragmentTransaction =
            t.setBreadCrumbShortTitle(text)

        override fun commit(): Int = t.commit()
        override fun setPrimaryNavigationFragment(fragment: Fragment?): FragmentTransaction =
            t.setPrimaryNavigationFragment(fragment)

        override fun runOnCommit(runnable: Runnable): FragmentTransaction = t.runOnCommit(runnable)
        override fun add(fragment: Fragment, tag: String?): FragmentTransaction =
            t.add(fragment, tag)

        override fun add(containerViewId: Int, fragment: Fragment): FragmentTransaction =
            t.add(containerViewId, fragment)

        override fun add(
            containerViewId: Int,
            fragment: Fragment,
            tag: String?
        ): FragmentTransaction = t.add(containerViewId, fragment, tag)

        override fun hide(fragment: Fragment): FragmentTransaction = t.hide(fragment)
        override fun replace(containerViewId: Int, fragment: Fragment): FragmentTransaction =
            doReplace(containerViewId, fragment)

        override fun replace(
            containerViewId: Int,
            fragment: Fragment,
            tag: String?
        ): FragmentTransaction = doReplace(containerViewId, fragment, tag)

        override fun detach(fragment: Fragment): FragmentTransaction = t.detach(fragment)
        override fun commitAllowingStateLoss(): Int = t.commitAllowingStateLoss()
        override fun setAllowOptimization(allowOptimization: Boolean): FragmentTransaction =
            t.setAllowOptimization(allowOptimization)

        override fun setCustomAnimations(enter: Int, exit: Int): FragmentTransaction =
            t.setCustomAnimations(enter, exit)

        override fun setCustomAnimations(
            enter: Int,
            exit: Int,
            popEnter: Int,
            popExit: Int
        ): FragmentTransaction = t.setCustomAnimations(enter, exit, popEnter, popExit)

        override fun addToBackStack(name: String?): FragmentTransaction = t.addToBackStack(name)
        override fun disallowAddToBackStack(): FragmentTransaction = t.disallowAddToBackStack()
        override fun setTransitionStyle(styleRes: Int): FragmentTransaction =
            t.setTransitionStyle(styleRes)

        override fun setTransition(transit: Int): FragmentTransaction = t.setTransition(transit)
        override fun attach(fragment: Fragment): FragmentTransaction = t.attach(fragment)
        override fun show(fragment: Fragment): FragmentTransaction = t.show(fragment)
        override fun isEmpty(): Boolean = t.isEmpty
        override fun remove(fragment: Fragment): FragmentTransaction = t.remove(fragment)
        override fun isAddToBackStackAllowed(): Boolean = t.isAddToBackStackAllowed
        override fun addSharedElement(sharedElement: View, name: String): FragmentTransaction =
            t.addSharedElement(sharedElement, name)

        override fun commitNow() = t.commitNow()
        override fun setBreadCrumbTitle(res: Int): FragmentTransaction = t.setBreadCrumbTitle(res)
        override fun setBreadCrumbTitle(text: CharSequence?): FragmentTransaction =
            t.setBreadCrumbTitle(text)

        override fun setReorderingAllowed(reorderingAllowed: Boolean): FragmentTransaction =
            t.setReorderingAllowed(reorderingAllowed)

        override fun commitNowAllowingStateLoss() = t.commitNowAllowingStateLoss()

        private fun doReplace(
            containerViewId: Int,
            fragment: Fragment?,
            tag: String? = null
        ): FragmentTransaction {
            fragment!!
            fm.fragments
                .filter { it !is NavHostFragment }
                .filter {
                    it.view?.parent?.let { parent -> (parent as View).id == containerViewId }
                        ?: false
                }
                .forEach {
                    logger?.d(LOG_TAG, "doReplace: check fragment: ${it.javaClass.simpleName}")
                    if (it.javaClass != fragment.javaClass) {
                        logger?.d(LOG_TAG, "hide fragment fragment: ${it.javaClass.simpleName}")
                        hide(it)
                    } else {
                        logger?.d(LOG_TAG, "found existing fragment. removing: ${it.javaClass.simpleName}")
                        remove(it)
                    }
                }
            add(containerViewId, fragment, tag)
            return this
        }
    }

    private companion object {
        /**
         * Logging tag.
         */
        private const val LOG_TAG = "StatefulFragmentNavigator"
    }
}