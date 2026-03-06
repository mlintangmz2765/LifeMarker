package com.lifemarker.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.lifemarker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCategories: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-2.5489, 118.0149), 4f) // Default Indonesia
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    fun fetchCurrentLocation() {
        if (hasLocationPermission) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                    loc?.let {
                        currentLocation = it
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(
                            LatLng(it.latitude, it.longitude), 15f
                        )
                    }
                }
            } catch (e: SecurityException) {
                // Ignore
            }
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            fetchCurrentLocation()
        }
    }

    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = { fetchCurrentLocation() },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "My Location")
                }
                FloatingActionButton(
                    onClick = { viewModel.startAddingMarker(currentLocation) }
                ) {
                    Icon(Icons.Default.AddLocation, contentDescription = "Add Marker")
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
            ) {
                uiState.markers.forEach { marker ->
                    Marker(
                        state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                        title = marker.category?.systemNameKey?.let { stringResource(getIdentifier(context, it)) } 
                                ?: marker.category?.customName 
                                ?: "Marker",
                        snippet = marker.note
                    )
                }
            }
        }

        if (uiState.isAddingMarker) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.cancelAddingMarker() }
            ) {
                AddMarkerContent(
                    uiState = uiState,
                    onNoteChange = viewModel::updateNewMarkerNote,
                    onCategorySelect = viewModel::selectCategory,
                    onManageCategoriesClick = {
                        viewModel.cancelAddingMarker()
                        onNavigateToCategories()
                    },
                    onSave = viewModel::saveMarker,
                    onCancel = viewModel::cancelAddingMarker
                )
            }
        }
    }
}

@Composable
fun AddMarkerContent(
    uiState: MainUiState,
    onNoteChange: (String) -> Unit,
    onCategorySelect: (Long) -> Unit,
    onManageCategoriesClick: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.add_marker), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.select_category), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            TextButton(onClick = onManageCategoriesClick) {
                Text(stringResource(R.string.manage_categories))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.categories) { category ->
                val isSelected = category.id == uiState.newMarkerCategoryId
                val bgColor = if (isSelected) Color(category.colorHex) else Color.LightGray
                val textColor = if (isSelected) Color.White else Color.Black
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(bgColor)
                        .clickable { onCategorySelect(category.id) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val catName = category.systemNameKey?.let { stringResource(getIdentifier(context, it)) } 
                            ?: category.customName 
                            ?: "Unknown"
                    Text(text = catName, color = textColor)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.newMarkerNote,
            onValueChange = onNoteChange,
            label = { Text(stringResource(R.string.note_optional)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSave,
                enabled = uiState.newMarkerCategoryId != null && uiState.selectedLocation != null
            ) {
                Text(stringResource(R.string.save))
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Safe area inside bottom sheet
    }
}

private fun getIdentifier(context: Context, name: String): Int {
    return context.resources.getIdentifier(name, "string", context.packageName)
}
