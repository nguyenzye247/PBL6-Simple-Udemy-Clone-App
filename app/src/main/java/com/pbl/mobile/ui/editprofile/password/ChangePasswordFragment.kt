package com.pbl.mobile.ui.editprofile.password

import android.content.Context
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SUCCESS
import com.pbl.mobile.base.BaseFragment
import com.pbl.mobile.databinding.FragmentChangePasswordBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.setError
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.ui.editprofile.EditProfileViewModel

class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding, EditProfileViewModel>() {
    private lateinit var onChangePasswordSuccessListener: OnChangePasswordSuccessListener

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    override fun getLazyBinding() = lazy { FragmentChangePasswordBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<EditProfileViewModel>()

    override fun setupInit() {
        initViews()
        initListeners()
        observe()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnChangePasswordSuccessListener) {
            onChangePasswordSuccessListener = context
        }
    }

    private fun initViews() {
        binding.apply {

        }
    }

    private fun initListeners() {
        binding.apply {
            etNewPasswordRepeat.doOnTextChanged { text, _, _, _ ->
                val newPasswordText = etNewPassword.text.toString()
                if (isPasswordMatched(text.toString(), newPasswordText)) {
                    txtConfirmPassword.setError(false, null)
                } else {
                    txtConfirmPassword.setError(true, getString(R.string.password_must_be_matched))
                }
            }
            btnSubmit.setOnClickListener {
                validateAndChangePassword()
            }
        }
    }

    private fun observe() {
        viewModel.apply {
            changePasswordResponse().observe(this@ChangePasswordFragment) { response ->
                when (response) {
                    is BaseResponse.Loading -> {
                        binding.progressBarChangePassword.isVisible = true
                    }
                    is BaseResponse.Success -> {
                        response.data?.let { changePasswordResponse ->
                            if (changePasswordResponse.status == SUCCESS) {
                                onChangePasswordSuccessListener.onChangePasswordSuccess()
                            }
                        }
                        binding.progressBarChangePassword.isVisible = false
                    }
                    is BaseResponse.Error -> {

                        binding.progressBarChangePassword.isVisible = false
                    }
                }
            }
        }
    }

    private fun validateAndChangePassword() {
        val oldPasswordText = binding.etOldPassword.text.toString()
        val newPasswordText = binding.etNewPassword.text.toString()
        val newRepeatPasswordText = binding.etNewPasswordRepeat.text.toString()
        if (oldPasswordText.isNotEmpty() &&
            newPasswordText.isNotEmpty() &&
            newRepeatPasswordText.isNotEmpty()
        ) {
            if (newPasswordText != newRepeatPasswordText) {
                requireContext().showToast(getString(R.string.password_must_be_matched))
            } else {
                viewModel.changePassword(oldPasswordText, newPasswordText)
            }
        } else {
            requireContext().showToast(getString(R.string.please_fulfill_all_info))
        }
    }

    private fun isPasswordMatched(oldPassword: String, newPassword: String): Boolean {
        return oldPassword == newPassword
    }

    interface OnChangePasswordSuccessListener {
        fun onChangePasswordSuccess()
    }
}
