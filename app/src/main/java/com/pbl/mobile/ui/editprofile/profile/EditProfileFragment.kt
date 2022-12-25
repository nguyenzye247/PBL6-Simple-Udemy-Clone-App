package com.pbl.mobile.ui.editprofile.profile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SUCCESS
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.common.EMPTY_TEXT
import com.pbl.mobile.databinding.FragmentEditProfileBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.observeOnUiThread
import com.pbl.mobile.extension.setError
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.helper.RxBus
import com.pbl.mobile.helper.RxEvent
import com.pbl.mobile.model.remote.user.UpdateUserProfileBody
import com.pbl.mobile.ui.editprofile.EditProfileActivity
import com.pbl.mobile.ui.editprofile.EditProfileViewModel
import com.pbl.mobile.util.DateFormatUtils
import com.pbl.mobile.util.StringUtils
import com.pbl.mobile.util.ValidationUtils
import java.util.*

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, EditProfileViewModel>() {
    private lateinit var onPickImageListener: OnPickImageListener

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    override fun getLazyBinding() = lazy { FragmentEditProfileBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<EditProfileViewModel>()

    override fun setupInit() {
        initViews()
        initListeners()
        observe()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPickImageListener) {
            onPickImageListener = context
        }
    }

    private fun initViews() {
        binding.apply {
            context?.let { context ->
                val name = context.getBaseConfig().fullName
                val email = context.getBaseConfig().myEmail
                etFullName.setText(name)
                etEmail.setText(email)
                etEmail.isEnabled = false
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            txtBirthday.setEndIconOnClickListener {
                showDatePicker()
            }
            txtFrontSideUrl.setEndIconOnClickListener {
                etFrontSideUrl.setText(EMPTY_TEXT)
            }
            txtBackSideUrl.setEndIconOnClickListener {
                etBackSideUrl.setText(EMPTY_TEXT)
            }
            llUploadFrontSide.setOnClickListener {
                onPickImageListener.onOpenCamera(
                    EditProfileActivity.IDENTITY_FRONT_SIDE,
                    llUploadFrontSide.width,
                    llUploadFrontSide.height
                )
            }
            llUploadBackSide.setOnClickListener {
                onPickImageListener.onOpenGallery(
                    EditProfileActivity.IDENTITY_BACK_SIDE,
                    llUploadFrontSide.width,
                    llUploadFrontSide.height
                )
            }
            ivIdentityFront.setOnClickListener {
                llUploadFrontSide.performClick()
            }
            ivIdentityBack.setOnClickListener {
                llUploadBackSide.performClick()
            }
            btnSubmit.setOnClickListener {
                validateUserInput()
            }
            etPhoneNumber.doOnTextChanged { text, _, _, _ ->
                if (ValidationUtils.isPhoneNumberValid(text.toString())) {
                    txtPhoneNumber.setError(false, null)
                }
            }
        }
    }

    private fun observe() {
        viewModel.apply {
            uploadFrontImageResponse().observe(this@EditProfileFragment) { response ->
                response?.let {
                    when (response) {
                        is BaseResponse.Loading -> {
                            binding.progressBarFront.isVisible = true
                        }
                        is BaseResponse.Success -> {
                            response.data?.let { frontSizeImageResponse ->
                                val url = frontSizeImageResponse.data.imageUrl
                                if (url.isNotEmpty()) {
                                    binding.apply {
                                        etFrontSideUrl.setText(url)
                                        etFrontSideUrl.isEnabled = false
                                    }
                                }
                            }
                            viewModel.resetUploadedImageValue()
                            binding.progressBarFront.isVisible = false
                        }
                        is BaseResponse.Error -> {
                            binding.etFrontSideUrl.setText(EMPTY_TEXT)
                            binding.progressBarFront.isVisible = false
                        }
                    }
                }
            }
            uploadBackImageResponse().observe(this@EditProfileFragment) { response ->
                response?.let {
                    when (response) {
                        is BaseResponse.Loading -> {
                            binding.progressBarBack.isVisible = true
                        }
                        is BaseResponse.Success -> {
                            response.data?.let { backSizeImageResponse ->
                                val url = backSizeImageResponse.data.imageUrl
                                if (url.isNotEmpty()) {
                                    binding.apply {
                                        etBackSideUrl.setText(url)
                                        etBackSideUrl.isEnabled = false
                                    }
                                }
                            }
                            viewModel.resetUploadedImageValue()
                            binding.progressBarBack.isVisible = false
                        }
                        is BaseResponse.Error -> {
                            binding.progressBarBack.isVisible = false
                            binding.etFrontSideUrl.setText(EMPTY_TEXT)
                        }
                    }
                }
            }
            updateUserProfileResponse().observe(this@EditProfileFragment) { response ->
                when (response) {
                    is BaseResponse.Loading -> {
                        binding.progressBarUpdateProfile.isVisible = true
                    }
                    is BaseResponse.Success -> {
                        response.data?.let { updateUserAvatarResponse ->
                            if (updateUserAvatarResponse.status == SUCCESS) {
                                requireContext().showToast("User profile updated successfully")
                            }
                        }
                        binding.progressBarUpdateProfile.isVisible = false
                    }
                    is BaseResponse.Error -> {
                        requireContext().showToast("Failed to update user profile")
                        binding.progressBarUpdateProfile.isVisible = false
                    }
                    else -> {}
                }
            }
            userDetailResponse().observe(this@EditProfileFragment) { response ->
                when (response) {
                    is BaseResponse.Loading -> {

                    }
                    is BaseResponse.Success -> {
                        response.data?.let { userDetailResponse ->
                            val userDetail = userDetailResponse.data
                            binding.apply {
                                etFrontSideUrl.setText(userDetail.fullName)
                                etPhoneNumber.setText(userDetail.phone)
                                etAddress.setText(userDetail.address)
                                etOccupation.setText(userDetail.occupation)
                                val birthday =
                                    DateFormatUtils.getDateFromTimeZoneDate(userDetail.dateOfBirth)
                                etBirthday.setText(birthday)
                                setIdentityImage(userDetail.identityImageUrl)
                            }
                        }
                    }
                    is BaseResponse.Error -> {

                    }
                }
            }
            getUserDetail()
        }
        subscription.addAll(
            RxBus.listen(RxEvent.EventSetIdentityFrontSide::class.java)
                .observeOnUiThread()
                .subscribe {
                    binding.ivIdentityFront.setImageBitmap(it.bitmap)
                    binding.cvIdentityFront.visibility = View.VISIBLE
                    binding.llUploadFrontSide.visibility = View.INVISIBLE
                    uploadImage(it.bitmap, EditProfileActivity.IDENTITY_FRONT_SIDE)
                },
            RxBus.listen(RxEvent.EventSetIdentityBackSide::class.java)
                .observeOnUiThread()
                .subscribe {
                    binding.ivIdentityBack.setImageBitmap(it.bitmap)
                    binding.cvIdentityBack.visibility = View.VISIBLE
                    binding.llUploadBackSide.visibility = View.INVISIBLE
                    uploadImage(it.bitmap, EditProfileActivity.IDENTITY_BACK_SIDE)
                },
        )
    }

    @SuppressLint("NewApi")
    private fun showDatePicker() {
        context?.let { context ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val a = DatePickerDialog(context)
            a.datePicker.init(year, month, day) { _, year1, month1, day1 ->
                val dateText = "$year1-${month1 + 1}-$day1"
                binding.etBirthday.setText(dateText)
            }
            a.show()
        }
    }

    private fun uploadImage(bitmap: Bitmap, imageSide: Int) {
        if (imageSide == EditProfileActivity.IDENTITY_FRONT_SIDE) {
            viewModel.uploadFrontImage(bitmap)
        } else if (imageSide == EditProfileActivity.IDENTITY_BACK_SIDE) {
            viewModel.uploadBackImage(bitmap)
        }
    }

    private fun validateUserInput() {
        binding.apply {
            val fullName = etFullName.text.toString()
            val phoneNumber = etPhoneNumber.text.toString()
            val address = etAddress.text.toString()
            val occupation = etOccupation.text.toString()
            val dateOfBirth = etBirthday.text.toString()
            val identityFrontSide = etFrontSideUrl.text.toString()
            val identityBackSide = etBackSideUrl.text.toString()
            if (!ValidationUtils.isPhoneNumberValid(phoneNumber)) {
                txtPhoneNumber.setError(true, getString(R.string.invalid_phone))
            }
            if (fullName.isEmpty() ||
                address.isEmpty() ||
                occupation.isEmpty() ||
                dateOfBirth.isEmpty() ||
                identityFrontSide.isEmpty() ||
                identityBackSide.isEmpty()
            ) {
                requireContext().showToast(getString(R.string.please_fulfill_all_info))
            }
            viewModel.updateUserProfile(
                UpdateUserProfileBody(
                    address,
                    occupation,
                    identityImageUrl = "$identityFrontSide - $identityBackSide",
                    fullName,
                    phoneNumber,
                    dateOfBirth
                )
            )
        }
    }

    private fun setIdentityImage(url: String) {
        val identityUrls =
            StringUtils.separateIdentityUrl(url)
        if (identityUrls.first.isNotEmpty() && identityUrls.second.isNotEmpty()) {
            binding.apply {
                cvIdentityFront.visibility = View.VISIBLE
                cvIdentityBack.visibility = View.VISIBLE
                llUploadFrontSide.visibility = View.INVISIBLE
                llUploadBackSide.visibility = View.INVISIBLE
                etFrontSideUrl.setText(identityUrls.first)
                etBackSideUrl.setText(identityUrls.second)
                Glide.with(this@EditProfileFragment)
                    .load(identityUrls.first)
                    .placeholder(R.drawable.image_identity_holder)
                    .centerCrop()
                    .into(ivIdentityFront)
                Glide.with(this@EditProfileFragment)
                    .load(identityUrls.second)
                    .placeholder(R.drawable.image_identity_holder)
                    .centerCrop()
                    .into(ivIdentityBack)
            }
        }
    }

    interface OnPickImageListener {
        fun onOpenCamera(imageSide: Int, width: Int, height: Int)
        fun onOpenGallery(imageSide: Int, width: Int, height: Int)
    }
}
