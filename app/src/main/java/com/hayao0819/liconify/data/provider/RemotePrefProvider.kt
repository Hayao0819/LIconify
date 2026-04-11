package com.hayao0819.liconify.data.provider

import com.crossbowffs.remotepreferences.RemotePreferenceFile
import com.crossbowffs.remotepreferences.RemotePreferenceProvider
import com.hayao0819.liconify.BuildConfig
import com.hayao0819.liconify.data.common.Resources

class RemotePrefProvider : RemotePreferenceProvider(
    BuildConfig.APPLICATION_ID,
    arrayOf(RemotePreferenceFile(Resources.SHARED_XPREFERENCES, true))
)