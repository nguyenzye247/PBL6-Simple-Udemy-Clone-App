package com.pbl.mobile.ui.upload

import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.pbl.mobile.R
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivityUploadBinding
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.ui.upload.fragment.UploadInformationFragment
import com.pbl.mobile.ui.upload.fragment.UploadSectionFragment

class UploadActivity : BaseActivity<ActivityUploadBinding, UploadViewModel>(),
    UploadInformationFragment.OnSubmitListener, UploadSectionFragment.OnPublishListener {
    companion object {
        private const val STEP_NUMBER = 2
        private const val INFO_STEP = 0
        private const val SECTION_STEP = 1
    }

    private lateinit var uploadAdapter: UploadAdapter
    private var currentPage = UploadAdapter.UPLOAD_INFO_POS

    override fun getLazyBinding() = lazy { ActivityUploadBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<UploadViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {
        initViews()
        initListener()
        observe()
    }

    private fun initViews() {
        binding.apply {
            uploadAdapter = UploadAdapter(supportFragmentManager, lifecycle)
            vp2Upload.apply {
                isUserInputEnabled = false
                adapter = uploadAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        when (position) {
                            UploadAdapter.UPLOAD_INFO_POS -> {
                                currentPage = UploadAdapter.UPLOAD_INFO_POS
                            }
                            UploadAdapter.UPLOAD_SECTION_POS -> {
                                currentPage = UploadAdapter.UPLOAD_SECTION_POS
                            }
                        }
                    }
                })
            }
            stepUpload.state
                .steps(
                    arrayListOf(
                        resources.getString(R.string.information),
                        resources.getString(R.string.create_sections)
                    )
                )
                .stepsNumber(STEP_NUMBER)
                .commit()
            stepUpload.setOnStepClickListener { step ->
                when (step) {
                    INFO_STEP -> {
                        goToCreateInfo()
                    }
                    SECTION_STEP -> {
                        goToCreateSection()
                    }
                }
            }
        }
    }

    private fun initListener() {
//        binding.apply {
//            btnUploadCourse.setOnClickListener {
//                if (currentPage == UploadAdapter.UPLOAD_INFO_POS) {
//                    goToCreateSection()
//                } else {
//                    uploadCourse()
//                    binding.apply {
//                        stepUpload.done(true)
//                    }
//                }
//            }
//        }
    }

    private fun observe() {

    }

    private fun goToCreateSection() {
        binding.apply {
            // TODO: check for valid input
            vp2Upload.currentItem = UploadAdapter.UPLOAD_SECTION_POS
//            btnUploadCourse.text = resources.getString(R.string.publish_course)
            stepUpload.go(SECTION_STEP, true)
        }
    }

    private fun goToCreateInfo() {
        binding.apply {
            // TODO: check for valid input
            vp2Upload.currentItem = UploadAdapter.UPLOAD_INFO_POS
//            btnUploadCourse.text = resources.getString(R.string.submit)
            stepUpload.go(INFO_STEP, true)
        }
    }

    private fun uploadCourse() {
        //TODO: check for valid input

        // if true
        //TODO: upload course
    }

    override fun onSubmitListener() {
        goToCreateSection()
        showToast("Upload information")
    }

    override fun onPublishListener() {
        uploadCourse()
        showToast("Publish course")
    }
}
