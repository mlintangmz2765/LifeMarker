package com.lifemarker.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.lifemarker.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCategories: () -> Unit,
    onNavigateToSettings: () -> Unit,
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

    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                ),
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
            ) {
                uiState.markers.forEach { marker ->
                    val categoryColor = marker.category?.colorHex ?: 0xFF6200EE.toInt()
                    val iconName = marker.category?.iconName ?: "Place"

                    MarkerComposable(
                        keys = arrayOf(marker.id, categoryColor, iconName),
                        state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                        title = marker.category?.systemNameKey?.let { stringResource(getIdentifier(context, it)) } 
                                ?: marker.category?.customName 
                                ?: "Marker",
                        snippet = marker.note,
                        onClick = {
                            viewModel.startEditingMarker(marker)
                            true
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(categoryColor)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = com.lifemarker.util.IconMapper.getIconByName(iconName),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            // Top Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
            )

            SmallFloatingActionButton(
                onClick = onNavigateToSettings,
                modifier = Modifier
                    .padding(top = 48.dp, end = 16.dp)
                    .align(Alignment.TopEnd),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }

            // UI Elements Overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    onClick = { fetchCurrentLocation() },
                    modifier = Modifier.padding(bottom = 16.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "My Location")
                }
                
                ExtendedFloatingActionButton(
                    onClick = { viewModel.startAddingMarker(currentLocation) },
                    icon = { Icon(Icons.Default.AddLocation, contentDescription = "Add Marker") },
                    text = { Text("Catat Aktivitas", fontWeight = FontWeight.Bold) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (uiState.isAddingMarker) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.cancelAddingMarker() },
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                AddMarkerContent(
                    uiState = uiState,
                    onNoteChange = viewModel::updateNewMarkerNote,
                    onPhotoChange = viewModel::updateNewMarkerPhotoUri,
                    onCategorySelect = viewModel::selectCategory,
                    onManageCategoriesClick = {
                        viewModel.cancelAddingMarker()
                        onNavigateToCategories()
                    },
                    onSave = viewModel::saveMarker,
                    onDelete = { viewModel.deleteMarker() },
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
    onPhotoChange: (String?) -> Unit,
    onCategorySelect: (Long) -> Unit,
    onManageCategoriesClick: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                // Important to take persistable URI permission if needed, but for simple gallery selection
                // the content provider typically grants partial temporary access. For long term sync
                // we'd copy it locally. For now, we store the content URI.
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                onPhotoChange(uri.toString())
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.add_marker), style = MaterialTheme.typography.titleLarge)
            if (uiState.editingMarkerTimestamp != null) {
                val dateString = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(uiState.editingMarkerTimestamp))
                Text(dateString, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
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
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(uiState.categories) { category ->
                val isSelected = category.id == uiState.newMarkerCategoryId
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) Color(category.colorHex) else MaterialTheme.colorScheme.surfaceVariant,
                    label = "bgColorTransition"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "textColorTransition"
                )
                val scale by animateDpAsState(
                    targetValue = if (isSelected) 4.dp else 0.dp,
                    label = "elevationTransition"
                )
                
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = bgColor,
                    shadowElevation = scale,
                    modifier = Modifier.clickable { onCategorySelect(category.id) }
                ) {
                    val catName = category.systemNameKey?.let { stringResource(getIdentifier(context, it)) } 
                            ?: category.customName 
                            ?: "Unknown"
                    Text(
                        text = catName, 
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = uiState.newMarkerNote,
            onValueChange = onNoteChange,
            label = { Text(stringResource(R.string.note_optional)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        // Photo Attachment Section
        if (uiState.newMarkerPhotoUri != null) {
            AsyncImage(
                model = uiState.newMarkerPhotoUri,
                contentDescription = "Attached Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { onPhotoChange(null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                 Icon(Icons.Default.Delete, contentDescription = "Remove")
                 Spacer(modifier = Modifier.width(8.dp))
                 Text("Hapus Lampiran")
            }
        } else {
            OutlinedButton(
                onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                 Icon(Icons.Default.Image, contentDescription = "Add Photo")
                 Spacer(modifier = Modifier.width(8.dp))
                 Text("Lampirkan Foto")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (uiState.editingMarkerId != null) {
            val positionContext = uiState.selectedLocation
            if (positionContext != null) {
                OutlinedButton(
                    onClick = {
                        val gmmIntentUri = Uri.parse("google.navigation:q=${positionContext.latitude},${positionContext.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        // Attempt to start maps; if not installed, gracefully do nothing or let system handle
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            // Fallback to generic geo URI
                            val genericUri = Uri.parse("geo:${positionContext.latitude},${positionContext.longitude}")
                            val genericIntent = Intent(Intent.ACTION_VIEW, genericUri)
                            context.startActivity(genericIntent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Directions, contentDescription = "Navigate")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Arahkan ke Sini")
                }
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (uiState.editingMarkerId != null) {
                IconButton(onClick = onDelete, modifier = Modifier.padding(end = 8.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Marker", tint = MaterialTheme.colorScheme.error)
                }
            }
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSave,
                enabled = uiState.newMarkerCategoryId != null
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
