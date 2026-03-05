package com.civicreport.ui.screens;

import android.Manifest;
import android.net.Uri;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.hapticfeedback.HapticFeedbackType;
import androidx.compose.ui.text.font.FontWeight;
import androidx.core.content.FileProvider;
import com.civicreport.data.api.ApiConstants;
import com.civicreport.ui.theme.*;
import com.civicreport.viewmodel.ReportViewModel;
import com.google.accompanist.permissions.ExperimentalPermissionsApi;
import com.google.android.gms.location.LocationServices;
import java.io.File;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a&\u0010\u0000\u001a\u00020\u00012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2 = {"ReportScreen", "", "onReportCreated", "Lkotlin/Function1;", "", "viewModel", "Lcom/civicreport/viewmodel/ReportViewModel;", "app_debug"})
public final class ReportScreenKt {
    
    @kotlin.OptIn(markerClass = {com.google.accompanist.permissions.ExperimentalPermissionsApi.class, androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ReportScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onReportCreated, @org.jetbrains.annotations.NotNull()
    com.civicreport.viewmodel.ReportViewModel viewModel) {
    }
}