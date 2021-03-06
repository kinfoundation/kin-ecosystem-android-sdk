package com.kin.ecosystem.main.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.Guideline
import android.support.v4.app.Fragment
import com.kin.ecosystem.R
import com.kin.ecosystem.base.*
import com.kin.ecosystem.base.widget.onPreDraw
import com.kin.ecosystem.base.widget.withEndAction
import com.kin.ecosystem.core.bi.EventLoggerImpl
import com.kin.ecosystem.core.data.auth.AuthRepository
import com.kin.ecosystem.core.data.blockchain.BlockchainSourceImpl
import com.kin.ecosystem.core.data.settings.SettingsDataSourceImpl
import com.kin.ecosystem.core.data.settings.SettingsDataSourceLocal
import com.kin.ecosystem.core.util.DeviceUtils
import com.kin.ecosystem.history.view.OrderHistoryFragment
import com.kin.ecosystem.main.ScreenId
import com.kin.ecosystem.main.ScreenId.*
import com.kin.ecosystem.main.presenter.EcosystemPresenter
import com.kin.ecosystem.main.presenter.IEcosystemPresenter
import com.kin.ecosystem.marketplace.view.MarketplaceFragment
import com.kin.ecosystem.marketplace.view.NotEnoughKinFragment
import com.kin.ecosystem.onboarding.view.OnboardingFragment
import com.kin.ecosystem.settings.view.SettingsFragment

class EcosystemActivity : KinEcosystemBaseActivity(), IEcosystemView {

	override val layoutRes: Int
		get() = R.layout.kinecosystem_activity_main

	private var ecosystemPresenter: IEcosystemPresenter? = null

	private lateinit var containerFrame: ConstraintLayout
	private lateinit var contentFrame: ConstraintLayout
	private lateinit var ceilingGuideline: Guideline
	private var isClosing = false
	private var endGuidelineAnimValue: Float = -1F

	private val savedMarketplaceFragment: MarketplaceFragment?
		get() = supportFragmentManager
				.findFragmentByTag(ECOSYSTEM_MARKETPLACE_FRAGMENT_TAG) as MarketplaceFragment?

	private val orderHistoryFragment: OrderHistoryFragment?
		get() = supportFragmentManager
				.findFragmentByTag(ECOSYSTEM_ORDER_HISTORY_FRAGMENT_TAG) as OrderHistoryFragment?

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initViews()
		ecosystemPresenter = EcosystemPresenter(AuthRepository.getInstance(),
				SettingsDataSourceImpl(SettingsDataSourceLocal(applicationContext)),
				BlockchainSourceImpl.getInstance(),
				EventLoggerImpl.getInstance(),
				this, savedInstanceState, intent.extras)
		ecosystemPresenter?.onAttach(this@EcosystemActivity)
	}

	override fun initViews() {
		containerFrame = findViewById<ConstraintLayout>(R.id.container).apply {
			setOnClickListener {
				ecosystemPresenter?.touchedOutside()
			}
			onPreDraw {
				runEnterAnimation()
			}
		}
		contentFrame = findViewById<ConstraintLayout>(R.id.screen_content).apply {
			setOnClickListener { }
		}
		ceilingGuideline = findViewById(R.id.guideline)
	}

	override fun onStart() {
		super.onStart()
		ecosystemPresenter?.onStart()
		getCurrentFragment()?.let {
			if (it is KinEcosystemBaseFragment<*, *>) {
				it.navigator = this
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		ecosystemPresenter?.onSaveInstanceState(outState)
		super.onSaveInstanceState(outState)
	}

	private fun replaceFragment(@IdRes containerId: Int, fragment: Fragment, tag: String,
	                            customAnimation: CustomAnimation = CustomAnimation(),
	                            backStackName: String? = null, allowStateLoss: Boolean = false,
	                            ceilingGuideline: Float = GUIDELINE_PERCENTAGE_DEFAULT) {

		val transaction = supportFragmentManager.beginTransaction()
				.setCustomAnimations(customAnimation.enter,
						customAnimation.exit,
						customAnimation.popEnter,
						customAnimation.popExit)

		if (backStackName != null && !backStackName.isEmpty()) {
			transaction.addToBackStack(backStackName)
		}

		transaction.replace(containerId, fragment, tag)

		if (allowStateLoss) transaction.commitAllowingStateLoss() else transaction.commit()


		if (endGuidelineAnimValue != ceilingGuideline) {
			animateGuidelinePercentage(from = getCurrentGuidelinePercentage(), to = ceilingGuideline)
		}
	}

	private fun animateGuidelinePercentage(from: Float, to: Float = GUIDELINE_PERCENTAGE_DEFAULT, halfWayAction: () -> Unit = { }) {
		var isTransitionStarted = false
		endGuidelineAnimValue = to
		ValueAnimator.ofFloat(from, to).apply {
			duration = DURATION_GUIDELINE_PERCENTAGE_ANIM
			// set duration
			interpolator = AnimConsts.Interpolator.ACCELERATE_DECELERATE
			// set interpolator and  updateListener to get the animated value
			addUpdateListener {
				val runningValue = animatedValue as Float
				val newLayoutParams = ceilingGuideline.layoutParams as ConstraintLayout.LayoutParams
				newLayoutParams.guidePercent = runningValue
				ceilingGuideline.layoutParams = newLayoutParams
				if (!isTransitionStarted && currentPlayTime >= duration / 2) {
					isTransitionStarted = true
					halfWayAction.invoke()
				}
			}
			withEndAction {
				endGuidelineAnimValue = -1F
			}
			start()
		}
	}

	override fun showNotEnoughKin(withAnimation: Boolean) {
		if (withAnimation) {
			animateGuidelinePercentage(getCurrentGuidelinePercentage(), GUIDELINE_PERCENTAGE_NOT_ENOUGH_KIN) {
				showNotEnoughKinFragment()
			}
		} else {
			val newLayoutParams = ceilingGuideline.layoutParams as ConstraintLayout.LayoutParams
			newLayoutParams.guidePercent = GUIDELINE_PERCENTAGE_NOT_ENOUGH_KIN
			ceilingGuideline.layoutParams = newLayoutParams
			showNotEnoughKinFragment()
		}
	}

	private fun showNotEnoughKinFragment() {
		NotEnoughKinFragment.newInstance(this@EcosystemActivity).apply {
			replaceFragment(R.id.fragment_frame, this,
					tag = ECOSYSTEM_NOT_ENOUGH_KIN_FRAGMENT_TAG,
					customAnimation = customAnimation {
						enter = R.anim.kinecosystem_fade_in
						exit = R.anim.kinecosystem_fade_out
						popExit = R.anim.kinecosystem_fade_out
						popEnter = R.anim.kinecosystem_fade_in
					}, ceilingGuideline = GUIDELINE_PERCENTAGE_NOT_ENOUGH_KIN)
			setVisibleScreen(NOT_ENOUGH_KIN)
		}
	}

	override fun navigateToOnboarding() {
		OnboardingFragment.getInstance(intent.extras, this).apply {
			replaceFragment(R.id.fragment_frame, this, ECOSYSTEM_ONBOARDING_FRAGMENT_TAG)
			setVisibleScreen(ONBOARDING)
		}
	}

	override fun navigateToMarketplace(customAnimation: CustomAnimation) {
		savedMarketplaceFragment ?: MarketplaceFragment.newInstance(this).apply {
			replaceFragment(R.id.fragment_frame, this,
					tag = ECOSYSTEM_MARKETPLACE_FRAGMENT_TAG,
					customAnimation = customAnimation)
			setVisibleScreen(MARKETPLACE)
		}
	}

	private fun setVisibleScreen(id: ScreenId) {
		ecosystemPresenter?.visibleScreen(id)
	}

	override fun navigateToOrderHistory(customAnimation: CustomAnimation, addToBackStack: Boolean) {
		orderHistoryFragment ?: OrderHistoryFragment.newInstance(this).apply {
			replaceFragment(R.id.fragment_frame, this,
					tag = ECOSYSTEM_ORDER_HISTORY_FRAGMENT_TAG,
					customAnimation = customAnimation,
					backStackName = if (addToBackStack) MARKETPLACE_TO_ORDER_HISTORY else null,
					allowStateLoss = true)
			setVisibleScreen(ORDER_HISTORY)
		}
	}

	override fun navigateToSettings() {
		val settingsFragment: SettingsFragment = supportFragmentManager
				.findFragmentByTag(ECOSYSTEM_SETTINGS_FRAGMENT_TAG) as SettingsFragment?
				?: SettingsFragment.newInstance(this)

		replaceFragment(R.id.fragment_frame, settingsFragment,
				tag = ECOSYSTEM_SETTINGS_FRAGMENT_TAG,
				customAnimation = customAnimation {
					enter = R.anim.kinecosystem_slide_in_right
					exit = R.anim.kinecosystem_slide_out_left
					popEnter = R.anim.kinrecovery_slide_in_left
					popExit = R.anim.kinecosystem_slide_out_right
				},
				backStackName = ORDER_HISTORY_TO_SETTINGS,
				allowStateLoss = true)
		setVisibleScreen(SETTINGS)
	}

	override fun close() {
		if (!isClosing) {
			isClosing = true
			runExitAnimation()
		}
	}

	private fun runEnterAnimation() {
		val animatorSet = AnimatorSet()
		var contentSlide: ValueAnimator? = null
		var backgroundColorFade: ObjectAnimator? = null
		containerFrame.apply {
			backgroundColorFade = ObjectAnimator.ofInt(background, AnimConsts.Property.ALPHA, AnimConsts.Value.BG_COLOR_ALPHA_0, AnimConsts.Value.BG_COLOR_ALPHA_255).apply {
				duration = AnimConsts.Duration.CLOSE_ANIM
			}
		}
		contentFrame.let {
			contentSlide = ValueAnimator.ofInt(DeviceUtils.getScreenHeight(), it.top).apply {
				duration = AnimConsts.Duration.SLIDE_ANIM
				interpolator = AnimConsts.Interpolator.DECELERATE
				addUpdateListener { valueAnimator ->
					it.y = (valueAnimator.animatedValue as Int).toFloat()
				}
			}
		}
		if (contentSlide != null && backgroundColorFade != null) {
			animatorSet.playTogether(contentSlide, backgroundColorFade)
			animatorSet.duration = AnimConsts.Duration.CLOSE_ANIM
			animatorSet.start()
		}
	}

	private fun runExitAnimation() {
		val animatorSet = AnimatorSet()
		var contentSlide: ValueAnimator? = null
		var backgroundColorFade: ObjectAnimator? = null
		contentFrame.let {
			contentSlide = ValueAnimator.ofInt(it.top, DeviceUtils.getScreenHeight()).apply {
				addUpdateListener { valueAnimator ->
					it.y = (valueAnimator.animatedValue as Int).toFloat()
				}
				duration = AnimConsts.Duration.SLIDE_ANIM
			}
		}
		containerFrame.apply {
			backgroundColorFade = ObjectAnimator.ofInt(background, AnimConsts.Property.ALPHA, AnimConsts.Value.BG_COLOR_ALPHA_255, AnimConsts.Value.BG_COLOR_ALPHA_0).apply {
				duration = AnimConsts.Duration.CLOSE_ANIM
				interpolator = AnimConsts.Interpolator.DECELERATE
				withEndAction {
					finish()
				}
			}
		}

		if (contentSlide != null && backgroundColorFade != null) {
			animatorSet.playTogether(contentSlide, backgroundColorFade)
			animatorSet.duration = AnimConsts.Duration.CLOSE_ANIM
			animatorSet.start()
		}
	}

	override fun onBackPressed() {
		ecosystemPresenter?.backButtonPressed()
	}

	override fun navigateBack() {
		val count = supportFragmentManager.backStackEntryCount
		if (count == 0) {
			val currentFragment = getCurrentFragment()
			when (currentFragment) {
				is OrderHistoryFragment -> {
					navigateToMarketplace(customAnimation {
						enter = R.anim.kinecosystem_slide_in_left
						exit = R.anim.kinecosystem_slide_out_right
					})
				}
				is MarketplaceFragment -> {
					close()
				}
				else -> close()
			}
		} else {
			val entry = supportFragmentManager.getBackStackEntryAt(count - 1)
			when (entry.name) {
				MARKETPLACE_TO_ORDER_HISTORY -> {
					// After pressing back from OrderHistory, should put the attrs again.
					// This is the only fragment that should set presenter again on back.
					savedMarketplaceFragment?.let { it.navigator = this }
							?: navigateToMarketplace(customAnimation {
								enter = R.anim.kinecosystem_slide_in_left
								exit = R.anim.kinecosystem_slide_out_right
							})
					supportFragmentManager.popBackStackImmediate()
					setVisibleScreen(MARKETPLACE)
				}
				ORDER_HISTORY_TO_SETTINGS -> {
					// After pressing back from Settings, should put the attrs again.
					// This is the only fragment that should set presenter again on back.
					orderHistoryFragment?.let { it.navigator = this }
							?: navigateToOrderHistory(customAnimation {
								enter = R.anim.kinecosystem_slide_in_left
								exit = R.anim.kinecosystem_slide_out_right
							}, addToBackStack = false)
					supportFragmentManager.popBackStackImmediate()
					setVisibleScreen(ORDER_HISTORY)
				}
			}
		}
	}

	private fun getCurrentFragment() = supportFragmentManager.findFragmentById(R.id.fragment_frame)

	override fun onDestroy() {
		ecosystemPresenter?.onDetach()
		super.onDestroy()
	}

	override fun finish() {
		super.finish()
		overridePendingTransition(0, 0)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		getVisibleFragment()?.onActivityResult(requestCode, resultCode, data)
	}

	private fun getVisibleFragment(): Fragment? {
		supportFragmentManager.fragments?.let { fragments ->
			fragments.forEach { fragment ->
				if (fragment.isVisible)
					return fragment
			}
		}
		return null
	}

	private fun getCurrentGuidelinePercentage() = (ceilingGuideline.layoutParams as ConstraintLayout.LayoutParams).guidePercent

	companion object {
		private const val GUIDELINE_PERCENTAGE_DEFAULT = 0.2F
		private const val GUIDELINE_PERCENTAGE_NOT_ENOUGH_KIN = 0.35F
		private const val DURATION_GUIDELINE_PERCENTAGE_ANIM = 400L

		// Fragments tags
		private const val ECOSYSTEM_ONBOARDING_FRAGMENT_TAG = "ecosystem_onboarding_fragment_tag"
		private const val ECOSYSTEM_MARKETPLACE_FRAGMENT_TAG = "ecosystem_marketplace_fragment_tag"
		private const val ECOSYSTEM_ORDER_HISTORY_FRAGMENT_TAG = "ecosystem_order_history_fragment_tag"
		private const val ECOSYSTEM_SETTINGS_FRAGMENT_TAG = "ecosystem_settings_fragment_tag"
		private const val ECOSYSTEM_NOT_ENOUGH_KIN_FRAGMENT_TAG = "ecosystem_not_enough_kin_fragment_tag"

		// Back stack name
		private const val MARKETPLACE_TO_ORDER_HISTORY = "marketplace_to_order_history"
		private const val ORDER_HISTORY_TO_SETTINGS = "order_history_to_settings"
	}
}
