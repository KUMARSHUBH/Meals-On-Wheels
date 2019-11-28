package com.krshubham.mealsonwheels.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.krshubham.mealsonwheels.R


class TitleBehavior(
    context: Context,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<TextView?>() {
    // calculated from given layout
    private var startXPositionImage = 0
    private var startYPositionImage = 0
    private var startHeight = 0
    private var startToolbarHeight = 0
    private var initialised = false
    private var amountOfToolbarToMove = 0f
    private var amountOfImageToReduce = 0f
    private var amountToMoveXPosition = 0f
    private var amountToMoveYPosition = 0f
    // user configured params
    private var finalToolbarHeight = 0f
    private var finalXPosition = 0f
    private var finalYPosition = 0f
    private var finalHeight = 0f
    fun layoutDependsOn(
        parent: CoordinatorLayout?,
        child: TextView?,
        dependency: View?
    ): Boolean {
        return dependency is AppBarLayout // change if you want another sibling to depend on
    }

    fun onDependentViewChanged(
        parent: CoordinatorLayout?,
        child: TextView,
        dependency: View
    ): Boolean { // make child (avatar) change in relation to dependency (toolbar) in both size and position, init with properties from layout
        initProperties(child, dependency)
        // calculate progress of movement of dependency
        var currentToolbarHeight: Float =
            startToolbarHeight + dependency.y // current expanded height of toolbar
        // don't go below configured min height for calculations (it does go passed the toolbar)
        currentToolbarHeight =
            if (currentToolbarHeight < finalToolbarHeight) finalToolbarHeight else currentToolbarHeight
        val amountAlreadyMoved = startToolbarHeight - currentToolbarHeight
        val progress =
            100 * amountAlreadyMoved / amountOfToolbarToMove // how much % of expand we reached
        // update image size
        val heightToSubtract = progress * amountOfImageToReduce / 100
        val lp =
            child.layoutParams as CoordinatorLayout.LayoutParams
        lp.width = (startHeight - heightToSubtract).toInt()
        lp.height = (startHeight - heightToSubtract).toInt()
        child.layoutParams = lp
        // update image position
        val distanceXToSubtract = progress * amountToMoveXPosition / 100
        val distanceYToSubtract = progress * amountToMoveYPosition / 100
        val newXPosition = startXPositionImage - distanceXToSubtract
        //newXPosition = newXPosition < endXPosition ? endXPosition : newXPosition; // don't go passed end position
        child.x = newXPosition
        child.y = startYPositionImage - distanceYToSubtract
        return true
    }

    private fun initProperties(
        child: TextView,
        dependency: View
    ) {
        if (!initialised) { // form initial layout
            startHeight = child.height
            startXPositionImage = child.x.toInt()
            startYPositionImage = child.y.toInt()
            startToolbarHeight = dependency.height
            // some calculated fields
            amountOfToolbarToMove = startToolbarHeight - finalToolbarHeight
            amountOfImageToReduce = startHeight - finalHeight
            amountToMoveXPosition = startXPositionImage - finalXPosition
            amountToMoveYPosition = startYPositionImage - finalYPosition
            initialised = true
        }
    }

    init {
        if (attrs != null) {
            val a: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.TitleBehavior)
            finalXPosition = a.getDimension(R.styleable.TitleBehavior_finalXPosition, 0f)
            finalYPosition = a.getDimension(R.styleable.TitleBehavior_finalYPosition, 0f)
            finalHeight = a.getDimension(R.styleable.TitleBehavior_finalHeight, 0f)
            finalToolbarHeight =
                a.getDimension(R.styleable.TitleBehavior_finalToolbarHeight, 0f)
            a.recycle()
        }
    }
}