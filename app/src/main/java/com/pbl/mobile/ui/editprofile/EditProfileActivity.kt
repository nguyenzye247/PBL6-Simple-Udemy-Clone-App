package com.pbl.mobile.ui.editprofile

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.provider.Settings
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.pbl.mobile.R
import com.pbl.mobile.api.BaseResponse
import com.pbl.mobile.api.SUCCESS
import com.pbl.mobile.base.BaseActivity
import com.pbl.mobile.base.BaseInput
import com.pbl.mobile.base.ViewModelProviderFactory
import com.pbl.mobile.databinding.ActivityEditProfileBinding
import com.pbl.mobile.extension.getBaseConfig
import com.pbl.mobile.extension.showToast
import com.pbl.mobile.helper.RxBus
import com.pbl.mobile.helper.RxEvent
import com.pbl.mobile.ui.editprofile.password.ChangePasswordFragment
import com.pbl.mobile.ui.editprofile.profile.EditProfileFragment
import com.pbl.mobile.ui.editprofile.profile.GetImageBottomSheetFragment
import com.pbl.mobile.ui.signin.SignInActivity
import com.pbl.mobile.util.CameraUtils
import com.pbl.mobile.widget.PermissionDeniedBadDialog
import com.pbl.mobile.widget.PermissionDeniedGoodDialog
import java.io.FileNotFoundException

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>(),
    EditProfileFragment.OnPickImageListener,
    ChangePasswordFragment.OnChangePasswordSuccessListener {
    private lateinit var editProfileAdapter: EditProfilePagerAdapter

    private var currentIdentitySide: Int = IDENTITY_FRONT_SIDE
    private var imageWidth: Int = 0
    private var imageHeight: Int = 0

    private val captureImageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            setCapturedPic()
        }
    }

    private val getGalleryImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        getResultFrom(it)
    }

    private lateinit var takePicturePermissionResultLauncher: ActivityResultLauncher<String>

    companion object {
        const val IDENTITY_FRONT_SIDE = 0
        const val IDENTITY_BACK_SIDE = 1
        const val AVATAR = 2
    }

    override fun getLazyBinding() = lazy { ActivityEditProfileBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<EditProfileViewModel> {
        ViewModelProviderFactory(
            BaseInput.EditProFileInput(
                application
            )
        )
    }

    override fun setupInit() {
        initViews()
        initListeners()
        observe()
        initLauncher()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        val tabTitles =
            arrayListOf(getString(R.string.update_information), getString(R.string.change_password))
        binding.apply {
            val avatarUrl = getBaseConfig().myAvatar
            val role = getBaseConfig().myRole
            val name = getBaseConfig().fullName
            val id = getBaseConfig().myId

            Glide.with(this@EditProfileActivity)
                .load(avatarUrl)
                .placeholder(R.drawable.avatar_holder_person)
                .into(ivUserAvatar)
            tvUserFullName.text = name

            vp2Main.apply {
                editProfileAdapter = EditProfilePagerAdapter(this@EditProfileActivity)
                adapter = editProfileAdapter
                TabLayoutMediator(tabLayoutMain, this) { tab, position ->
                    tab.text = tabTitles[position]
                }.attach()
            }
            selectTabPosition()
        }
    }

    private fun initListeners() {
        initActionBar()
        binding.apply {
            ivUserAvatar.setOnClickListener {
                currentIdentitySide = AVATAR
                imageWidth = ivUserAvatar.width
                imageHeight = ivUserAvatar.height
                openSelectImageDialog()
            }

            tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    ResourcesCompat.getFont(tab.view.context, R.font.rubik_medium)?.let {
                        setTabTypeface(tab, it)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    ResourcesCompat.getFont(tab.view.context, R.font.rubik_regular)?.let {
                        setTabTypeface(tab, it)
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //no-ops
                }
            })
        }
    }

    private fun observe() {
        viewModel.apply {
            uploadAvatarResponse().observe(this@EditProfileActivity) { response ->
                response?.let {
                    when (response) {
                        is BaseResponse.Loading -> {
                            binding.progressBarAvatar.isVisible = true
                        }
                        is BaseResponse.Success -> {
                            response.data?.let { frontSizeImageResponse ->
                                val url = frontSizeImageResponse.data.imageUrl
                                this@EditProfileActivity.getBaseConfig().myAvatar = url
                                //TODO: upload avatar url to server
                                viewModel.updateUserAvatar(url)
                            }
                            binding.progressBarAvatar.isVisible = false
                            viewModel.resetUploadedImageValue()
                        }
                        is BaseResponse.Error -> {
                            binding.progressBarAvatar.isVisible = false
                            viewModel.resetUploadedImageValue()
                        }
                    }
                }
            }
            updateUserAvatarResponse().observe(this@EditProfileActivity) { response ->
                when (response) {
                    is BaseResponse.Loading -> {

                    }
                    is BaseResponse.Success -> {
                        response.data?.let { updateUserAvatarResponse ->
                            if (updateUserAvatarResponse.status == SUCCESS) {
                                this@EditProfileActivity.showToast("User avatar updated successfully")
                            }
                        }
                    }
                    is BaseResponse.Error -> {
                        this@EditProfileActivity.showToast("Failed to update user avatar")
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbarEditProfile)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    private fun selectTabPosition(pos: Int = 0) {
        val initSelectedTab = binding.tabLayoutMain.getTabAt(pos)
        initSelectedTab?.let { tab ->
            tab.select()
            ResourcesCompat.getFont(initSelectedTab.view.context, R.font.rubik_medium)?.let {
                setTabTypeface(initSelectedTab, it)
            }
        }
    }

    private fun setTabTypeface(tab: TabLayout.Tab, typeface: Typeface) {
        for (i in 0 until tab.view.childCount) {
            val tabViewChild = tab.view.getChildAt(i)
            if (tabViewChild is TextView) tabViewChild.typeface = typeface
        }
    }

    private fun openSelectImageDialog() {
        val selectImageBottomSheetFragment = GetImageBottomSheetFragment.newInstance(
            onTakePictureItemClickCallback = {
                openCamera()
            },
            onOpenGalleryItemClickCallback = {
                openGallery()
            }
        )
        selectImageBottomSheetFragment.show(supportFragmentManager, GetImageBottomSheetFragment.TAG)
    }

    private fun setCapturedPic() {
        // Get the dimensions of the View
        val targetW: Int = imageWidth
        val targetH: Int = imageHeight
        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(CameraUtils.currentPhotoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight
        // Determine how much to scale down the image
        val scaleFactor = (photoW / targetW).coerceAtMost(photoH / targetH)
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        // Set views
        val bitmap = BitmapFactory.decodeFile(CameraUtils.currentPhotoPath, bmOptions)
        setImage(bitmap)
    }

    private fun getResultFrom(result: ActivityResult) {
        if (result.resultCode == -1) {
            try {
                val imageUri = result.data?.data
                imageUri?.let {
                    val imageStream = this.contentResolver.openInputStream(it)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    setImage(selectedImage)
                    imageStream?.close()
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "No picture selected", Toast.LENGTH_LONG).show()
        }
    }

    private fun initLauncher() {
        takePicturePermissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                CameraUtils.openCamera(this, captureImageActivityResultLauncher)
            } else if (CameraUtils.shouldAskPermission() && !shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA
                )
            ) {
                val permissionDeniedBadDialog = PermissionDeniedBadDialog.newInstance(
                    onYesClickCallback = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts(
                            "package",
                            packageName, null
                        )
                        intent.data = uri
                        startActivity(intent)
                    }
                )
                permissionDeniedBadDialog.show(
                    supportFragmentManager,
                    PermissionDeniedBadDialog.TAG
                )
            } else {
                val permissionDeniedGoodDialog = PermissionDeniedGoodDialog.newInstance(
                    onYesClickCallback = {
                        takePicturePermissionResultLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                )
                permissionDeniedGoodDialog.show(
                    supportFragmentManager,
                    PermissionDeniedGoodDialog.TAG
                )
            }
        }
    }

    private fun openCamera() {
        CameraUtils.requestOpenCamera(
            this@EditProfileActivity,
            takePicturePermissionResultLauncher,
            captureImageActivityResultLauncher
        )
    }

    private fun setImage(bitmap: Bitmap) {
        when (currentIdentitySide) {
            IDENTITY_FRONT_SIDE -> {
                RxBus.publish(RxEvent.EventSetIdentityFrontSide(bitmap))
            }
            IDENTITY_BACK_SIDE -> {
                RxBus.publish(RxEvent.EventSetIdentityBackSide(bitmap))
            }
            AVATAR -> {
                setAvatar(bitmap)
            }
        }
    }

    private fun setAvatar(bitmap: Bitmap) {
        viewModel.uploadAvatar(bitmap)
        Glide.with(this@EditProfileActivity)
            .asBitmap()
            .load(bitmap)
            .into(binding.ivUserAvatar)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        getGalleryImageResultLauncher.launch(intent)
    }

    override fun onOpenCamera(imageSide: Int, width: Int, height: Int) {
        currentIdentitySide = imageSide
        imageWidth = width
        imageHeight = height
        openSelectImageDialog()
    }

    override fun onOpenGallery(imageSide: Int, width: Int, height: Int) {
        currentIdentitySide = imageSide
        imageWidth = width
        imageHeight = height
        openSelectImageDialog()
    }

    private fun goToSignIn() {
        startActivity(
            Intent(
                this@EditProfileActivity,
                SignInActivity::class.java
            )
        )
        finish()
    }

    override fun onChangePasswordSuccess() {
        showToast(getString(R.string.change_password_success))
        getBaseConfig().clearAll()
        goToSignIn()
    }
}
