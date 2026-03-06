package com.lifemarker.ui.categories

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifemarker.R
import com.lifemarker.domain.model.Category
import com.lifemarker.util.IconMapper
import com.lifemarker.ui.components.CategoryIcon
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.manage_categories)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.startAddingCategory() },
                icon = { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_category)) },
                text = { Text(stringResource(R.string.add_category)) },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.categories) { category ->
                CategoryItem(
                    category = category,
                    context = context,
                    onEdit = { viewModel.startEditingCategory(category) },
                    onDelete = { viewModel.deleteCategory(category) }
                )
            }
        }

        if (uiState.isAddingCategory) {
            ModalBottomSheet(onDismissRequest = { viewModel.cancelAddingCategory() }) {
                AddCategoryContent(
                    uiState = uiState,
                    onNameChange = viewModel::updateNewCategoryName,
                    onColorChange = viewModel::updateNewCategoryColor,
                    onIconChange = viewModel::updateNewCategoryIcon,
                    onSave = viewModel::saveCategory,
                    onCancel = viewModel::cancelAddingCategory
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category, 
    context: Context,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(category.colorHex)),
                contentAlignment = Alignment.Center
            ) {
                CategoryIcon(
                    iconName = category.iconName,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            val catName = category.systemNameKey?.let { stringResource(getIdentifier(context, it)) } 
                    ?: category.customName 
                    ?: "Unknown"
            Text(
                text = catName, 
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddCategoryContent(
    uiState: CategoryUiState,
    onNameChange: (String) -> Unit,
    onColorChange: (Int) -> Unit,
    onIconChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.add_category), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.newCategoryName,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.category_name)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            enabled = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.select_color), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        val colors = listOf(
            0xFFF44336.toInt(), 0xFFEF5350.toInt(), 0xFFE91E63.toInt(), 0xFFF06292.toInt(),
            0xFF9C27B0.toInt(), 0xFFBA68C8.toInt(), 0xFF673AB7.toInt(), 0xFF9575CD.toInt(),
            0xFF3F51B5.toInt(), 0xFF7986CB.toInt(), 0xFF2196F3.toInt(), 0xFF64B5F6.toInt(),
            0xFF03A9F4.toInt(), 0xFF4FC3F7.toInt(), 0xFF00BCD4.toInt(), 0xFF4DD0E1.toInt(),
            0xFF009688.toInt(), 0xFF4DB6AC.toInt(), 0xFF4CAF50.toInt(), 0xFF81C784.toInt(),
            0xFF8BC34A.toInt(), 0xFFAED581.toInt(),
            0xFFFFEB3B.toInt(), 0xFFFFF176.toInt(), 0xFFFFC107.toInt(), 0xFFFFD54F.toInt(),
            0xFFFF9800.toInt(), 0xFFFFB74D.toInt(), 0xFFFF5722.toInt(), 0xFFFF8A65.toInt(),
            0xFF795548.toInt(), 0xFFA1887F.toInt(), 0xFF607D8B.toInt(), 0xFF90A4AE.toInt(),
            0xFF000000.toInt(), 0xFF757575.toInt()
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(colors) { color ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(color))
                        .clickable { onColorChange(color) },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.newCategoryColor == color) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(if (Color(color).luminance() > 0.5f) Color.Black else Color.White)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.select_icon), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(IconMapper.predefinedIcons) { (name, vector) ->
                val isSelected = uiState.newCategoryIcon == name
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onIconChange(name) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = vector,
                        contentDescription = name,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        OutlinedTextField(
            value = if (IconMapper.isEmoji(uiState.newCategoryIcon)) uiState.newCategoryIcon else "",
            onValueChange = { if (it.length <= 2) onIconChange(it) },
            label = { Text(stringResource(R.string.or_emoji)) },
            modifier = Modifier.width(120.dp),
            shape = RoundedCornerShape(12.dp)
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
                enabled = uiState.newCategoryName.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun getIdentifier(context: Context, name: String): Int {
    return context.resources.getIdentifier(name, "string", context.packageName)
}
