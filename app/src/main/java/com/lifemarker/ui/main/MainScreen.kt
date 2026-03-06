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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
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
import com.google.maps.android.compose.clustering.Clustering
import com.lifemarker.R
import com.lifemarker.domain.model.MarkerDetails
import com.lifemarker.ui.components.CategoryIcon
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
        position = CameraPosition.fromLatLngZoom(LatLng(-2.5489, 118.0149), 4f)
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
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                onMapLongClick = { latLng ->
                    val location = Location("").apply {
                        latitude = latLng.latitude
                        longitude = latLng.longitude
                    }
                    viewModel.startAddingMarker(location)
                }
            ) {
                if (uiState.isAddingMarker && uiState.selectedLocation != null) {
                    Marker(
                        state = MarkerState(position = LatLng(uiState.selectedLocation!!.latitude, uiState.selectedLocation!!.longitude)),
                        title = stringResource(R.string.add_marker),
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                }
                Clustering(
                    items = uiState.filteredMarkers.map { MarkerClusterItem(it) },
                    onClusterItemClick = { clusterItem: MarkerClusterItem ->
                        viewModel.startEditingMarker(clusterItem.markerDetails)
                        true
                    },
                    clusterItemContent = { clusterItem: MarkerClusterItem ->
                        val marker = clusterItem.markerDetails
                        val categoryColor = marker.category?.colorHex ?: 0xFF6200EE.toInt()
                        val iconName = marker.category?.iconName ?: "Place"
                        
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(categoryColor)),
                            contentAlignment = Alignment.Center
                        ) {
                            CategoryIcon(
                                iconName = iconName,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Column {
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        tonalElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Row(
                             verticalAlignment = Alignment.CenterVertically,
                             modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                             Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                             Spacer(modifier = Modifier.width(12.dp))
                             BasicTextField(
                                 value = uiState.searchQuery,
                                 onValueChange = viewModel::updateSearchQuery,
                                 modifier = Modifier.weight(1f),
                                 textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                                 decorationBox = { innerTextField ->
                                     if (uiState.searchQuery.isEmpty()) {
                                         Text(stringResource(R.string.search_placeholder), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                     }
                                     innerTextField()
                                 }
                             )
                             if (uiState.searchQuery.isNotEmpty()) {
                                 IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                     Icon(Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.primary)
                                 }
                             }
                             Spacer(modifier = Modifier.width(8.dp))
                             IconButton(onClick = onNavigateToSettings) {
                                 Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.primary)
                             }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = uiState.selectedFilterCategoryId == null,
                                onClick = { viewModel.clearCategoryFilter() },
                                label = { Text(stringResource(R.string.filter_all)) },
                                leadingIcon = { Icon(Icons.Default.FilterList, "All", modifier = Modifier.size(18.dp)) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                        items(uiState.categories) { category ->
                            val isSelected = uiState.selectedFilterCategoryId == category.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.toggleCategoryFilter(category.id) },
                                label = { 
                                     val name = category.systemNameKey?.let { stringResource(getIdentifier(context, it)) } ?: (category.customName ?: "Cat")
                                     Text(name) 
                                },
                                leadingIcon = {
                                     CategoryIcon(
                                         iconName = category.iconName,
                                         modifier = Modifier.size(18.dp),
                                         tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
                                     )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
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
                    text = { Text(stringResource(R.string.record_activity), fontWeight = FontWeight.Bold) },
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
                    shape = RoundedCornerShape(24.dp),
                    color = bgColor,
                    shadowElevation = scale,
                    modifier = Modifier.clickable { onCategorySelect(category.id) }
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryIcon(
                            iconName = category.iconName,
                            tint = textColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
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
                 Text(stringResource(R.string.remove_attachment))
            }
        } else {
            OutlinedButton(
                onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                 Icon(Icons.Default.Image, contentDescription = "Add Photo")
                 Spacer(modifier = Modifier.width(8.dp))
                 Text(stringResource(R.string.attach_photo))
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
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
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
                    Text(stringResource(R.string.navigate_here))
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
    }
}

private fun getIdentifier(context: Context, name: String): Int {
    return context.resources.getIdentifier(name, "string", context.packageName)
}

